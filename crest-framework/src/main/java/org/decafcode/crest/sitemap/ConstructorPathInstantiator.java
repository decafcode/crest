package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.util.Objects;

final class ConstructorPathInstantiator extends CreatorPathInstantiator {
    private final Constructor<?> ctor;

    public ConstructorPathInstantiator(
            Constructor<?> ctor,
            ImmutableList<CreatorParamWrapper> paramsW) {
        super(paramsW);
        this.ctor = Objects.requireNonNull(ctor, "ctor");
    }

    @Override
    protected Object invokeCreator(Object[] params)
            throws ReflectiveOperationException {
        return ctor.newInstance(params);
    }
}
