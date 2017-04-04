package org.decafcode.crest.framework;

public final class NotModifiedResponse extends Response {
    public static final class Builder
            extends Response.Builder<Builder> {
        @Override
        public Response build() {
            return new NotModifiedResponse(this);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    private NotModifiedResponse(Builder b) {
        super(b);
    }

    @Override
    public <X extends Throwable> void accept(
            ResponseVisitor<? extends X> acceptor) throws X {
        acceptor.visitNotModified(this);
    }
}
