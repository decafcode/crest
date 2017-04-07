package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class ForbiddenException extends HttpStatusException {
    public ForbiddenException() {
        super("Forbidden");
    }

    public ForbiddenException(String message) {
        super(message);
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_FORBIDDEN;
    }
}
