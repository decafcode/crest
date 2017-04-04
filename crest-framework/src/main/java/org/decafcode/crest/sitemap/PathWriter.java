package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

final class PathWriter {
    private final URI rootUri;
    private final ImmutableList<PathSegment> segments;
    private final ImmutableMap<String, Method> qpGetters;

    PathWriter(
            URI rootUri,
            ImmutableList<PathSegment> segments,
            ImmutableMap<String, Method> qpGetters) {
        this.rootUri = Objects.requireNonNull(rootUri, "rootUri");
        this.segments = Objects.requireNonNull(segments, "segments");
        this.qpGetters = Objects.requireNonNull(qpGetters, "qpGetters");
    }

    URI write(ResourcePath path) {
        StringBuilder pathStr = new StringBuilder();

        for (PathSegment segment : segments) {
            pathStr.append(segment.emit(path));
        }

        StringBuilder queryStr = new StringBuilder();

        for (Map.Entry<String, Method> kvp : qpGetters.entrySet()) {
            Method getter = kvp.getValue();
            Object value;

            try {
                value = getter.invoke(path);
            } catch (ReflectiveOperationException e) {
                throw new Error(e);
            }

            if (value instanceof Optional) {
                Optional<?> optional = (Optional<?>) value;

                if (!optional.isPresent()) {
                    continue;
                }

                value = optional.get();
            }

            if (queryStr.length() != 0) {
                queryStr.append('&');
            }

            String name = kvp.getKey();

            queryStr.append(name);
            queryStr.append('=');

            String rawString = value.toString();
            String escapedString;

            try {
                escapedString = URLEncoder.encode(rawString, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(e);
            }

            queryStr.append(escapedString);
        }

        URI result = rootUri.resolve(pathStr.substring(1));

        if (queryStr.length() == 0) {
            return result;
        }

        try {
            return new URI(result.toString() + "?" + queryStr.toString());
        } catch (URISyntaxException e) {
            throw new IllegalStateException(e);
        }
    }
}
