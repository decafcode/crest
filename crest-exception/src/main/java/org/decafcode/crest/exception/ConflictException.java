package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class ConflictException extends HttpStatusException {
    public ConflictException() {
        super("Conflict");
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_CONFLICT;
    }
}
