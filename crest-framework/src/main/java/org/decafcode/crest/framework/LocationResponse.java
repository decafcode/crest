package org.decafcode.crest.framework;

import java.net.URI;
import java.util.Objects;

public final class LocationResponse extends Response {
    public final static class Builder
            extends Response.Builder<LocationResponse.Builder> {
        private Integer statusCode;
        private URI target;

        public LocationResponse.Builder statusCode(int value) {
            if (value < 300 || value > 399) {
                throw new IllegalArgumentException(String.format(
                        "Redirect must use a 3xx status code (got %d)",
                        value));
            }

            statusCode = value;

            return this;
        }

        public LocationResponse.Builder target(URI value) {
            target = Objects.requireNonNull(value);

            return this;
        }

        @Override
        public LocationResponse build() {
            return new LocationResponse(this);
        }
    }

    private final int statusCode;
    private final URI target;

    public static Builder builder() {
        return new Builder();
    }

    private LocationResponse(Builder b) {
        super(b);
        statusCode = Objects.requireNonNull(b.statusCode, "statusCode");
        target = Objects.requireNonNull(b.target, "target");
    }

    public int statusCode() {
        return statusCode;
    }

    public URI target() {
        return target;
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> visitor) throws X {
        visitor.visitLocation(this);
    }
}
