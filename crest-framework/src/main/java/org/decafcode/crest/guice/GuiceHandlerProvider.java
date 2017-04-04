package org.decafcode.crest.guice;

import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.util.Types;
import java.util.Objects;
import javax.inject.Inject;
import org.decafcode.crest.framework.DeleteHandler;
import org.decafcode.crest.framework.GetHandler;
import org.decafcode.crest.framework.HandlerProvider;
import org.decafcode.crest.framework.PostHandler;
import org.decafcode.crest.framework.PutHandler;
import org.decafcode.crest.framework.ResourcePath;

public final class GuiceHandlerProvider implements HandlerProvider {
    private final Injector g;

    @Inject
    GuiceHandlerProvider(Injector g) {
        this.g = Objects.requireNonNull(g);
    }

    private static Key<?> keyGet(Class<? extends ResourcePath> pathClass) {
        return Key.get(Types.newParameterizedType(
                GetHandler.class,
                pathClass));
    }

    private static Key<?> keyPut(Class<? extends ResourcePath> pathClass) {
        return Key.get(Types.newParameterizedType(
                PutHandler.class,
                pathClass));
    }

    private static Key<?> keyPost(Class<? extends ResourcePath> pathClass) {
        return Key.get(Types.newParameterizedType(
                PostHandler.class,
                pathClass));
    }

    private static Key<?> keyDelete(Class<? extends ResourcePath> pathClass) {
        return Key.get(Types.newParameterizedType(
                DeleteHandler.class,
                pathClass));
    }

    @Override
    public boolean hasGetHandler(Class<? extends ResourcePath> pathClass) {
        return g.getExistingBinding(keyGet(pathClass)) != null;
    }

    @Override
    public boolean hasPutHandler(Class<? extends ResourcePath> pathClass) {
        return g.getExistingBinding(keyPut(pathClass)) != null;
    }

    @Override
    public boolean hasPostHandler(Class<? extends ResourcePath> pathClass) {
        return g.getExistingBinding(keyPost(pathClass)) != null;
    }

    @Override
    public boolean hasDeleteHandler(Class<? extends ResourcePath> pathClass) {
        return g.getExistingBinding(keyDelete(pathClass)) != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourcePath> GetHandler<T> getGetHandler(
            Class<T> pathClass) {
        try {
            return (GetHandler<T>) g.getInstance(keyGet(pathClass));
        } catch (ConfigurationException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourcePath> PutHandler<T> getPutHandler(
            Class<T> pathClass) {
        try {
            return (PutHandler<T>) g.getInstance(keyPut(pathClass));
        } catch (ConfigurationException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourcePath> PostHandler<T> getPostHandler(
            Class<T> pathClass) {
        try {
            return (PostHandler<T>) g.getInstance(keyPost(pathClass));
        } catch (ConfigurationException e) {
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends ResourcePath> DeleteHandler<T> getDeleteHandler(
            Class<T> pathClass) {
        try {
            return (DeleteHandler<T>) g.getInstance(keyDelete(pathClass));
        } catch (ConfigurationException e) {
            return null;
        }
    }
}
