package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableSet;
import org.decafcode.crest.exception.BadRequestException;

public class MissingQueryParamsException extends BadRequestException {
    public MissingQueryParamsException(ImmutableSet<String> paramNames) {
        // TODO
    }
}
