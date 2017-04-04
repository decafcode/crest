package org.decafcode.crest.framework;

public interface ResponseVisitor<E extends Throwable> {
    void visitCreated(CreatedResponse resp) throws E;

    void visitEmpty(EmptyResponse resp) throws E;

    <T> void visitEntity(EntityResponse<T> resp) throws E;

    void visitLocation(LocationResponse resp) throws E;

    void visitNotModified(NotModifiedResponse resp) throws E;

    void visitOptions(OptionsResponse resp) throws E;
}
