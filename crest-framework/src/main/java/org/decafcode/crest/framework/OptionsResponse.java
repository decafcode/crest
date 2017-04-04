package org.decafcode.crest.framework;

import com.google.common.collect.ImmutableSet;
import java.util.Objects;

public final class OptionsResponse extends Response {
    public final static class Builder extends Response.Builder {
        private ImmutableSet.Builder<String> allowedMethods
                = ImmutableSet.builder();

        public Builder addMethods(String... values) {
            Objects.requireNonNull(values, "values");

            allowedMethods.add(values);

            return this;
        }

        public Builder addMethods(Iterable<String> values) {
            Objects.requireNonNull(values, "values");

            allowedMethods.addAll(values);

            return this;
        }

        @Override
        public OptionsResponse build() {
            return new OptionsResponse(this);
        }
    }

    private final ImmutableSet<String> allowedMethods;

    public static Builder builder() {
        return new Builder();
    }

    private OptionsResponse(Builder b) {
        super(b);
        allowedMethods = b.allowedMethods.build();
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> visitor) throws X {
        visitor.visitOptions(this);
    }

    public ImmutableSet<String> allowedMethods() {
        return allowedMethods;
    }
}
