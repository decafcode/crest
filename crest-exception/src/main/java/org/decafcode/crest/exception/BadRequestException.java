package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class BadRequestException extends HttpStatusException {
    public BadRequestException() {
        super("Bad Request");
    }

    public BadRequestException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public BadRequestException(String message) {
        super(message);
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_BAD_REQUEST;
    }
}
