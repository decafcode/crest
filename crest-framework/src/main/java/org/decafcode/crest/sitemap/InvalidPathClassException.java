package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;

public class InvalidPathClassException extends IllegalArgumentException {
    public InvalidPathClassException(
            Class<? extends ResourcePath> pathClass,
            String message) {
        super(String.format("%s: %s", pathClass, message));
    }
}
