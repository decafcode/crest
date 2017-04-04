package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableMap;
import org.decafcode.crest.framework.ResourcePath;
import java.net.URI;
import java.util.Objects;

final class ReverseSiteMap {
    private final ImmutableMap<Class<? extends ResourcePath>, PathWriter>
            writers;

    ReverseSiteMap(
            ImmutableMap<Class<? extends ResourcePath>, PathWriter> writers) {
        this.writers = Objects.requireNonNull(writers, "writers");
    }

    URI write(ResourcePath path) {
        Class<? extends ResourcePath> pathClass = path.pathClass();
        PathWriter writer = writers.get(pathClass);

        if (writer == null) {
            throw new UnknownPathClassException(pathClass);
        }

        return writer.write(path);
    }
}
