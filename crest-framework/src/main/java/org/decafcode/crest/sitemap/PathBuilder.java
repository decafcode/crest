package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;

final class PathBuilder {
    private final PathInstantiator pi;
    private final ImmutableMap.Builder<String, String> pathParams;
    private final ImmutableMap.Builder<String, String> queryParams;

    PathBuilder(PathInstantiator pi) {
        this.pi = Objects.requireNonNull(pi, "pi");
        this.pathParams = ImmutableMap.builder();
        this.queryParams = ImmutableMap.builder();
    }

    PathBuilder putPathParam(String key, String val) {
        pathParams.put(key, val);

        return this;
    }

    PathBuilder putQueryParam(String key, String val) {
        queryParams.put(key, val);

        return this;
    }

    PathBuilder putQueryParams(Map<String, String> kvps) {
        queryParams.putAll(kvps);

        return this;
    }

    ResourcePath build() throws ServletException {
        try {
            return pi.create(pathParams.build(), queryParams.build());
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }
}
