package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Method;
import java.util.Objects;

final class StaticMethodPathInstantiator extends CreatorPathInstantiator {
    private final Method sMethod;

    public StaticMethodPathInstantiator(
            Method sMethod,
            ImmutableList<CreatorParamWrapper> paramsW) {
        super(paramsW);
        this.sMethod = Objects.requireNonNull(sMethod, "sMethod");
    }

    @Override
    protected Object invokeCreator(Object[] params)
            throws ReflectiveOperationException {
        return sMethod.invoke(null, params);
    }
}
