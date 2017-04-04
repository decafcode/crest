package org.decafcode.crest.sitemap;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

final class BuilderMethodWrapper {
    private final Function<String, ?> reader;
    private final Method method;

    BuilderMethodWrapper(
            Function<String, ?> reader,
            Method method) {
        this.reader = Objects.requireNonNull(reader, "reader");
        this.method = Objects.requireNonNull(method, "method");
    }

    Object invokeBuilderMethod(Object builder, String strValue)
            throws ReflectiveOperationException {
        Object value = reader.apply(strValue);

        return method.invoke(builder, value);
    }
}
