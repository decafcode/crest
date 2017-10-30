package org.decafcode.crest.exception;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;

public abstract class HttpStatusException extends ServletException {
    public HttpStatusException(String message) {
        super(Objects.requireNonNull(message, "message"));
    }

    public HttpStatusException(String message, Throwable cause) {
        super(Objects.requireNonNull(message, "message"), cause);
    }

    public Map<String, String> extraHeaders() {
        return Collections.emptyMap();
    }

    public abstract int statusCode();
}
