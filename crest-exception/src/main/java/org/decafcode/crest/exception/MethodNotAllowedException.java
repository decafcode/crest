package org.decafcode.crest.exception;

import javax.servlet.http.HttpServletResponse;

public class MethodNotAllowedException extends HttpStatusException {
    public MethodNotAllowedException() {
        super("Method Not Allowed");
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_METHOD_NOT_ALLOWED;
    }
}
