package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import org.decafcode.crest.framework.Route;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

final class ReverseSiteMapBuilder {
    private final Set<Class<? extends ResourcePath>> pathClasses
            = new HashSet<>();

    private URI rootUri;

    void rootUri(URI rootUri) {
        this.rootUri = Objects.requireNonNull(rootUri, "rootUri");
    }

    void addPathClass(Class<? extends ResourcePath> pathClass) {
        if (pathClasses.contains(pathClass)) {
            throw new IllegalStateException(String.format(
                    "Path class already registered: %s",
                    pathClass));
        }

        pathClasses.add(pathClass);
    }

    ReverseSiteMap build() {
        Objects.requireNonNull(rootUri, "rootUri");

        ImmutableMap.Builder<Class<? extends ResourcePath>, PathWriter> writers
                = ImmutableMap.builder();

        for (Class<? extends ResourcePath> pathClass : pathClasses) {
            PathWriter writer = processPathClass(pathClass);

            writers.put(pathClass, writer);
        }

        return new ReverseSiteMap(writers.build());
    }

    private PathWriter processPathClass(
            Class<? extends ResourcePath> pathClass) {
        Route route = pathClass.getAnnotation(Route.class);

        if (route == null) {
            throw new InvalidPathClassException(pathClass, String.format(
                    "Path class lacks a @%s annotation",
                    Route.class.getSimpleName()));
        }

        String path = route.value();

        //
        // Build path segments
        //

        ImmutableList.Builder<PathSegment> segments = ImmutableList.builder();
        ImmutableList<String> tokens = PathTokenizer.tokenize(path);
        StringBuilder ss = new StringBuilder();

        for (String token : tokens) {
            if (token.startsWith("$")) {
                String varName = token.substring(1);
                Method getter = getGetter(pathClass, varName);

                if (ss.length() > 0) {
                    segments.add(new PathSegmentStatic(ss.toString()));
                    ss = new StringBuilder();
                }

                segments.add(new PathSegmentVariable(getter));
            } else {
                ss.append(token);
            }
        }

        if (ss.length() > 0) {
            segments.add(new PathSegmentStatic(ss.toString()));
        }

        //
        // Find query params
        //

        ImmutableMap.Builder<String, Method> qpGetters
                = ImmutableMap.builder();

        for (Method method : pathClass.getMethods()) {
            QueryParam queryParam = method.getAnnotation(QueryParam.class);

            if (queryParam == null) {
                continue;
            }

            String varName = queryParam.value();

            qpGetters.put(varName, method);
        }

        return new PathWriter(rootUri, segments.build(), qpGetters.build());
    }

    private static Method getGetter(
            Class<? extends ResourcePath> pathClass,
            String varName) {
        for (Method method : pathClass.getMethods()) {
            PathParam pathParam = method.getAnnotation(PathParam.class);

            if (pathParam == null) {
                continue;
            }

            if (method.getParameterCount() != 0) {
                throw new InvalidPathClassException(pathClass, String.format(
                        "%s: Getter must not take any arguments",
                        method.getName()));
            }

            if (method.getReturnType() == Optional.class) {
                throw new InvalidPathClassException(pathClass, String.format(
                        "%s: Path params cannot be optional",
                        method.getName()));
            }

            if (!pathParam.value().equals(varName)) {
                continue;
            }

            return method;
        }

        Method[] durr = pathClass.getMethods();

        throw new InvalidPathClassException(pathClass, String.format(
                "No getter for path param %s",
                varName));
    }
}
