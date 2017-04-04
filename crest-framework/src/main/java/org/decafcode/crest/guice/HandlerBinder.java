package org.decafcode.crest.guice;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.util.Types;
import java.util.Objects;
import org.decafcode.crest.framework.DeleteHandler;
import org.decafcode.crest.framework.GetHandler;
import org.decafcode.crest.framework.PostHandler;
import org.decafcode.crest.framework.PutHandler;
import org.decafcode.crest.framework.ResourcePath;

public final class HandlerBinder {
    private final Binder binder;

    public HandlerBinder(Binder binder) {
        this.binder = Objects.requireNonNull(binder, "binder");
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourcePath>
    LinkedBindingBuilder<DeleteHandler<?>> bindDelete(Class<T> pathType) {
        return (LinkedBindingBuilder<DeleteHandler<?>>) binder.bind(
                Key.get(Types.newParameterizedType(
                        DeleteHandler.class,
                        pathType)));
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourcePath>
    LinkedBindingBuilder<GetHandler<?>> bindGet(Class<T> pathType) {
        return (LinkedBindingBuilder<GetHandler<?>>) binder.bind(
                Key.get(Types.newParameterizedType(
                        GetHandler.class,
                        pathType)));
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourcePath>
    LinkedBindingBuilder<PostHandler<?>> bindPost(Class<T> pathType) {
        return (LinkedBindingBuilder<PostHandler<?>>) binder.bind(
                Key.get(Types.newParameterizedType(
                        PostHandler.class,
                        pathType)));
    }

    @SuppressWarnings("unchecked")
    public <T extends ResourcePath>
    LinkedBindingBuilder<PutHandler<?>> bindPut(Class<T> pathType) {
        return (LinkedBindingBuilder<PutHandler<?>>) binder.bind(
                Key.get(Types.newParameterizedType(
                        PutHandler.class,
                        pathType)));
    }
}
