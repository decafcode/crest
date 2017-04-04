package org.decafcode.crest.framework;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

public final class EntityResponse<T> extends Response {
    public static final class Builder<T>
            extends Response.Builder<EntityResponse.Builder<T>> {
        private T entity;
        private Optional<String> etag = Optional.empty();
        private Optional<Duration> maxAge = Optional.empty();

        public Builder<T> entity(T value) {
            entity = Objects.requireNonNull(value);

            return this;
        }

        public Builder<T> etag(String value) {
            etag = Optional.of(value);

            return this;
        }

        public Builder<T> maxAge(Duration value) {
            maxAge = Optional.of(value);

            return this;
        }

        @Override
        public EntityResponse<T> build() {
            return new EntityResponse<>(this);
        }
    }

    private final T entity;
    private final Optional<String> etag;
    private final Optional<Duration> maxAge;

    public static <T> EntityResponse.Builder<T> builder() {
        return new Builder<>();
    }

    private EntityResponse(Builder<T> b) {
        super(b);
        entity = Objects.requireNonNull(b.entity, "entity");
        etag = b.etag;
        maxAge = b.maxAge;
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> visitor) throws X {
        visitor.visitEntity(this);
    }

    public T entity() {
        return entity;
    }

    public Optional<String> etag() {
        return etag;
    }

    public Optional<Duration> maxAge() {
        return maxAge;
    }
}
