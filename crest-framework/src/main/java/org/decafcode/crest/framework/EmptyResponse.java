package org.decafcode.crest.framework;

public final class EmptyResponse extends Response {
    public final static class Builder
            extends Response.Builder<EmptyResponse.Builder> {
        @Override
        public EmptyResponse build() {
            return new EmptyResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public EmptyResponse() {
        super(new Builder());
    }

    private EmptyResponse(Builder b) {
        super(b);
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> visitor) throws X {
        visitor.visitEmpty(this);
    }
}
