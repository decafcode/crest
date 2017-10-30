package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import org.decafcode.crest.framework.Route;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

final class ForwardSiteMapBuilder {
    private final Map<Class<?>, Function<String, ?>> readers
            = new HashMap<>();
    private final Set<Class<? extends ResourcePath>> pathClasses
            = new HashSet<>();

    ForwardSiteMapBuilder() {
        readers.put(Instant.class, Instant::parse);
        readers.put(Integer.class, Integer::parseInt);
        readers.put(Integer.TYPE, Integer::parseInt);
        readers.put(Long.class, Long::parseLong);
        readers.put(Long.TYPE, Long::parseLong);
        readers.put(Short.class, Short::parseShort);
        readers.put(Short.TYPE, Short::parseShort);
        readers.put(String.class, Function.identity());
    }

    <T> void addReader(Class<T> paramType, Function<String, T> reader) {
        if (readers.containsKey(paramType)) {
            throw new IllegalStateException(String.format(
                    "Duplicate reader registered for %s",
                    paramType));
        }

        readers.put(paramType, reader);
    }

    void addPathClass(Class<? extends ResourcePath> value) {
        if (pathClasses.contains(value)) {
            throw new IllegalStateException(String.format(
                    "Path class already registered: %s",
                    value));
        }

        pathClasses.add(value);
    }

    ForwardSiteMap build() {
        PathNodeBuilder root = new PathNodeBuilder();

        pathClasses.forEach(pathClass -> processPathClass(root, pathClass));

        return new ForwardSiteMap(root.build());
    }

    private void processPathClass(
            PathNodeBuilder root,
            Class<? extends ResourcePath> pathClass) {
        // TODO improve diagnostics

        PathInstantiator pi = null;

        Constructor<?> ctor = Arrays.stream(pathClass.getConstructors())
                .findFirst()
                .orElse(null);

        if (ctor != null) {
            pi = new ConstructorPathInstantiator(
                    ctor,
                    scanParams(pathClass, ctor));
        }

        Method staticMethod = Arrays.stream(pathClass.getMethods())
                .filter(x -> x.getName().equals("create"))
                .findFirst()
                .orElse(null);

        if (staticMethod != null) {
            pi = new StaticMethodPathInstantiator(
                    staticMethod,
                    scanParams(pathClass, staticMethod));
        }

        Method builder = Arrays.stream(pathClass.getMethods())
                .filter(x -> x.getName().equals("builder"))
                .filter(x -> x.getParameterCount() == 0)
                .findFirst()
                .orElse(null);

        if (builder != null) {
            pi = wrapBuilder(pathClass, builder);
        }

        if (pi == null) {
            throw new InvalidPathClassException(
                    pathClass,
                    "No instantiation mechanism available");
        }

        Route route = pathClass.getAnnotation(Route.class);

        if (route == null) {
            throw new InvalidPathClassException(pathClass, String.format(
                    "@%s annotation required",
                    Route.class.getSimpleName()));
        }

        ImmutableList<String> tokens = PathTokenizer.tokenize(route.value());
        PathNodeBuilder pos = root;

        for (String token : tokens) {
            if (token.equals("/")) {
                pos = pos.addSlash();
            } else if (token.startsWith("$")) {
                String varName = token.substring(1);

                pos = pos.addCapture(varName);
            } else {
                pos = pos.addBranch(token);
            }
        }

        pos.addTerminal(pi);
    }

    private ImmutableList<CreatorParamWrapper> scanParams(
            Class<? extends ResourcePath> pathClass,
            Executable callable) {
        Parameter[] params = callable.getParameters();
        ImmutableList.Builder<CreatorParamWrapper> wrappers
                = ImmutableList.builder();

        for (Parameter param : params) {
            Type paramType = param.getParameterizedType();
            boolean optional = false;

            if (paramType == Optional.class) {
                ParameterizedType ptype = (ParameterizedType) paramType;
                Type[] typeParams = ptype.getActualTypeArguments();

                paramType = typeParams[0];
                optional = true;
            }

            Class<?> paramClass;

            if (paramType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) paramType;

                paramClass = (Class<?>) pt.getRawType();
            } else {
                paramClass = (Class<?>) paramType;
            }

            Function<String, ?> converter = readers.get(paramClass);

            if (converter == null) {
                throw new InvalidPathClassException(pathClass, String.format(
                        "Don't know how to read a %s param",
                        paramType));
            }

            PathParam pathP = param.getAnnotation(PathParam.class);
            QueryParam queryP = param.getAnnotation(QueryParam.class);
            ParamType aargh; // too many things called "type" itt
            String name;

            if (pathP != null) {
                aargh = ParamType.PATH;
                name = pathP.value();
            } else if (queryP != null) {
                aargh = ParamType.QUERY;
                name = queryP.value();
            } else {
                throw new InvalidPathClassException(pathClass, String.format(
                        "%s: Param has neither a @%s nor a @%s annotation",
                        callable.getName(),
                        PathParam.class.getSimpleName(),
                        QueryParam.class.getSimpleName()));
            }

            wrappers.add(new CreatorParamWrapper(
                    name,
                    aargh,
                    optional,
                    converter));
        }

        return wrappers.build();
    }

    private PathInstantiator wrapBuilder(
            Class<? extends ResourcePath> pathClass,
            Method builderMethod) {
        Class<?> builderClass = builderMethod.getReturnType();

        Map<String, BuilderMethodWrapper> pathMethods = new HashMap<>();
        Map<String, BuilderMethodWrapper> queryMethods = new HashMap<>();

        for (Method method : builderClass.getMethods()) {
            PathParam pathP = method.getAnnotation(PathParam.class);
            QueryParam queryP = method.getAnnotation(QueryParam.class);

            if (pathP == null && queryP == null) {
                continue;
            }

            Class<?>[] paramTypes = method.getParameterTypes();

            if (paramTypes.length != 1) {
                throw new InvalidPathClassException(pathClass, String.format(
                        "%s: Builder methods must take exactly one parameter",
                        method.getName()));
            }

            Function<String, ?> reader = readers.get(paramTypes[0]);
            BuilderMethodWrapper wrapper = new BuilderMethodWrapper(
                    reader,
                    method);

            if (pathP != null) {
                pathMethods.put(pathP.value(), wrapper);
            }

            if (queryP != null) {
                queryMethods.put(queryP.value(), wrapper);
            }
        }

        Method buildMethod = Arrays.stream(builderClass.getMethods())
                .filter(x -> x.getName().equals("build"))
                .filter(x -> x.getParameterCount() == 0)
                .findFirst()
                .orElse(null);

        if (buildMethod == null) {
            throw new InvalidPathClassException(
                    pathClass,
                    "Builder lacks a build() method");
        }

        return new BuilderPathInstantiator(
                ImmutableMap.copyOf(pathMethods),
                ImmutableMap.copyOf(queryMethods),
                builderMethod,
                buildMethod);
    }
}
