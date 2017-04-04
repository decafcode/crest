package org.decafcode.crest.sitemap;

import org.decafcode.crest.framework.ResourcePath;

public class UnknownPathClassException extends IllegalArgumentException {
    public UnknownPathClassException(Class<? extends ResourcePath> pathClass) {
        super(pathClass.toString());
    }
}
