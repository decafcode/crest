package org.decafcode.crest.framework;

import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;

public abstract class AbstractRequest implements Request {
    private final HttpServletRequest httpReq;

    protected AbstractRequest(HttpServletRequest httpReq) {
        this.httpReq = Objects.requireNonNull(httpReq, "httpReq");
    }

    @Override
    public Optional<String> ifMatch() {
        return Optional.ofNullable(httpReq.getHeader("If-Match"));
    }

    @Override
    public Optional<String> ifNoneMatch() {
        return Optional.ofNullable(httpReq.getHeader("If-None-Match"));
    }
}
