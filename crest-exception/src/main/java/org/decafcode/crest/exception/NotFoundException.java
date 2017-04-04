package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class NotFoundException extends HttpStatusException {
    public NotFoundException() {
        super("Not Found");
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_NOT_FOUND;
    }
}
