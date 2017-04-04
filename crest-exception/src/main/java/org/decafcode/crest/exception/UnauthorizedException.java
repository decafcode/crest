package org.decafcode.crest.exception;

import java.util.Collections;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class UnauthorizedException extends HttpStatusException {
    private String type;

    public UnauthorizedException(String type) {
        super("Unauthorized");
        this.type = type;
    }

    @Override
    public Map<String, String> extraHeaders() {
        return Collections.singletonMap("WWW-Authenticate", type);
    }

    @Override
    public final int statusCode() {
        return HttpServletResponse.SC_UNAUTHORIZED;
    }
}
