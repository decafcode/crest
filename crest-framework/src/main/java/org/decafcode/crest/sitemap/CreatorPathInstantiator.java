package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.decafcode.crest.framework.ResourcePath;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.ServletException;

abstract class CreatorPathInstantiator implements PathInstantiator {
    // java.lang.reflect.Executable would better be named "Callable"

    private final ImmutableList<CreatorParamWrapper> paramsW;

    CreatorPathInstantiator(ImmutableList<CreatorParamWrapper> paramsW) {
        this.paramsW = Objects.requireNonNull(paramsW, "paramsW");
    }

    @Override
    public ResourcePath create(
            ImmutableMap<String, String> pathParams,
            ImmutableMap<String, String> queryParams)
                    throws ReflectiveOperationException, ServletException {
        ArrayList<Object> paramsB = new ArrayList<>();
        ImmutableSet.Builder<String> missingParamsB = ImmutableSet.builder();

        for (CreatorParamWrapper paramW : paramsW) {
            ImmutableMap<String, String> src;

            switch (paramW.type()) {
                case PATH: src = pathParams; break;
                case QUERY: src = queryParams; break;
                default: throw new IllegalStateException();
            }

            String strValue = src.get(paramW.name());
            Optional<String> maybeStrValue = Optional.ofNullable(strValue);
            Optional<?> maybeValue = paramW.apply(maybeStrValue);

            if (paramW.optional()) {
                paramsB.add(maybeValue);
            } else {
                if (maybeValue.isPresent()) {
                    paramsB.add(maybeValue.get());
                } else {
                    paramsB.add(null); // never actually used
                    missingParamsB.add(paramW.name());
                }
            }
        }

        ImmutableSet<String> missingParams = missingParamsB.build();

        if (!missingParams.isEmpty()) {
            throw new MissingQueryParamsException(missingParams);
        }

        Object[] params = paramsB.toArray();

        return (ResourcePath) invokeCreator(params);
    }

    protected abstract Object invokeCreator(Object[] params)
            throws ReflectiveOperationException;
}
