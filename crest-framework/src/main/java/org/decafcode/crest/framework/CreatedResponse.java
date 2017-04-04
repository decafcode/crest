package org.decafcode.crest.framework;

import java.net.URI;
import java.util.Objects;

public final class CreatedResponse extends Response {
    public final static class Builder
            extends Response.Builder<CreatedResponse.Builder> {
        private URI uri;

        public final Builder uri(URI value) {
            uri = Objects.requireNonNull(value);

            return this;
        }

        @Override
        public final CreatedResponse build() {
            return new CreatedResponse(this);
        }
    }

    private final URI uri;

    public static Builder builder() {
        return new Builder();
    }

    private CreatedResponse(Builder b) {
        super(b);
        uri = Objects.requireNonNull(b.uri, "uri");
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> visitor) throws X {
        visitor.visitCreated(this);
    }

    public URI uri() {
        return uri;
    }
}
