package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.decafcode.crest.framework.ResourcePath;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;

final class BuilderPathInstantiator implements PathInstantiator {
    private final ImmutableMap<String, BuilderMethodWrapper> pathMethods;
    private final ImmutableMap<String, BuilderMethodWrapper> queryMethods;
    private final Method builderMethod;
    private final Method buildMethod;

    BuilderPathInstantiator(
            ImmutableMap<String, BuilderMethodWrapper> pathMethods,
            ImmutableMap<String, BuilderMethodWrapper> queryMethods,
            Method builderMethod,
            Method buildMethod) {
        this.pathMethods = Objects.requireNonNull(pathMethods, "pathMethods");
        this.queryMethods = Objects.requireNonNull(queryMethods,
                "queryMethods");
        this.builderMethod = Objects.requireNonNull(builderMethod,
                "builderMethod");
        this.buildMethod = Objects.requireNonNull(buildMethod, "buildMethod");
    }

    @Override
    public ResourcePath create(
            ImmutableMap<String, String> pathParams,
            ImmutableMap<String, String> queryParams)
                    throws ReflectiveOperationException, ServletException {
        Object builder = builderMethod.invoke(null);

        for (Map.Entry<String, BuilderMethodWrapper> kvp
                : pathMethods.entrySet()) {
            String paramName = kvp.getKey();
            BuilderMethodWrapper methodW = kvp.getValue();
            String strValue = pathParams.get(paramName);

            if (strValue == null) {
                throw new IllegalStateException();
            }

            builder = methodW.invokeBuilderMethod(builder, strValue);
        }

        ImmutableSet.Builder<String> unknownParamsB = ImmutableSet.builder();

        for (String queryKey : queryParams.keySet()) {
            if (!queryMethods.containsKey(queryKey)) {
                unknownParamsB.add(queryKey);
            }
        }

        ImmutableSet<String> unknownParams = unknownParamsB.build();

        if (!unknownParams.isEmpty()) {
            throw new UnknownQueryParamsException(unknownParams);
        }

        ImmutableSet.Builder<String> missingParamsB = ImmutableSet.builder();

        for (Map.Entry<String, BuilderMethodWrapper> kvp
                : queryMethods.entrySet()) {
            String paramName = kvp.getKey();
            BuilderMethodWrapper methodW = kvp.getValue();
            String strValue = queryParams.get(paramName);

            if (strValue != null) {
                methodW.invokeBuilderMethod(builder, strValue);
            }
        }

        ImmutableSet<String> missingParams = missingParamsB.build();

        if (!missingParams.isEmpty()) {
            throw new MissingQueryParamsException(missingParams);
        }

        return (ResourcePath) buildMethod.invoke(builder);
    }
}
