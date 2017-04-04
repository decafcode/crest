package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class UnsupportedMediaTypeException extends HttpStatusException {
    public UnsupportedMediaTypeException() {
        super("Unsupported media type");
    }

    @Override
    public int statusCode() {
        return HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE;
    }
}
