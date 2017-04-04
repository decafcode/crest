package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;
import java.lang.reflect.Method;
import java.util.Objects;

final class PathSegmentVariable implements PathSegment {
    private final Method getter;

    PathSegmentVariable(Method getter) {
        this.getter = Objects.requireNonNull(getter, "getter");
    }

    @Override
    public String emit(ResourcePath path) {
        try {
            return getter.invoke(path).toString();
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }
}
