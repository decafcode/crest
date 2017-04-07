package org.decafcode.crest.framework;

public interface HandlerProvider {
    boolean hasGetHandler(Class<? extends ResourcePath> pathClass);

    boolean hasPutHandler(Class<? extends ResourcePath> pathClass);

    boolean hasPatchHandler(Class<? extends ResourcePath> pathClass);

    boolean hasPostHandler(Class<? extends ResourcePath> pathClass);

    boolean hasDeleteHandler(Class<? extends ResourcePath> pathClass);

    <T extends ResourcePath> GetHandler<T> getGetHandler(
            Class<T> pathClass);

    <T extends ResourcePath> PutHandler<T> getPutHandler(
            Class<T> pathClass);

    <T extends ResourcePath> PatchHandler<T> getPatchHandler(
            Class<T> pathClass);

    <T extends ResourcePath> PostHandler<T> getPostHandler(
            Class<T> pathClass);

    <T extends ResourcePath> DeleteHandler<T> getDeleteHandler(
            Class<T> pathClass);
}
