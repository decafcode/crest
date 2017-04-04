package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;
import java.util.Objects;

final class PathSegmentStatic implements PathSegment {
    private final String value;

    PathSegmentStatic(String value) {
        this.value = Objects.requireNonNull(value, "value");
    }

    @Override
    public String emit(ResourcePath path) {
        return value;
    }
}
