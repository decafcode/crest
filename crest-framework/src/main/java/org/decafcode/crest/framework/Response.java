package org.decafcode.crest.framework;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;

public abstract class Response {
    public static abstract class Builder<T extends Builder> {
        private final ImmutableMap.Builder<String, String> extraHeaders
                = ImmutableMap.builder();

        Builder() {}

        @SuppressWarnings("unchecked")
        public final T addHeader(String key, String val) {
            Objects.requireNonNull(key, "key");
            Objects.requireNonNull(val, "val");

            extraHeaders.put(key, val);

            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public final T addHeaders(Map<String, String> vals) {
            Objects.requireNonNull(vals, "vals");

            extraHeaders.putAll(vals);

            return (T) this;
        }

        public abstract Response build();
    }

    private final ImmutableMap<String, String> extraHeaders;

    Response(Builder<?> b) {
        extraHeaders = b.extraHeaders.build();
    }

    public final ImmutableMap<String, String> extraHeaders() {
        return extraHeaders;
    }

    public abstract <X extends Throwable> void accept(
            ResponseVisitor<? extends X> acceptor) throws X;
}
