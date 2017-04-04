package org.decafcode.crest.sitemap;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

final class CreatorParamWrapper {
    private final String name;
    private final ParamType type;
    private final boolean optional;
    private final Function<String, ?> reader;

    CreatorParamWrapper(
            String name,
            ParamType type,
            boolean optional,
            Function<String, ?> reader) {
        this.name = Objects.requireNonNull(name, "name");
        this.type = Objects.requireNonNull(type, "type");
        this.optional = optional;
        this.reader = Objects.requireNonNull(reader, "reader");
    }

    String name() {
        return name;
    }

    ParamType type() {
        return type;
    }

    boolean optional() {
        return optional;
    }

    Optional<?> apply(Optional<String> maybe) {
        return maybe.map(reader);
    }
}
