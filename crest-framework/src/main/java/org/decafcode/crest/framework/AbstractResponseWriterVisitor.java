package org.decafcode.crest.framework;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractResponseWriterVisitor
        implements ResponseVisitor<IOException> {
    private final HttpServletResponse httpResp;

    protected AbstractResponseWriterVisitor(HttpServletResponse httpResp) {
        this.httpResp = Objects.requireNonNull(httpResp, "httpResp");
    }

    @Override
    public void visitCreated(CreatedResponse resp) throws IOException {
        httpResp.setStatus(HttpServletResponse.SC_CREATED);
        httpResp.setHeader("Location", resp.uri().toString());
        httpResp.setContentLength(0);
        resp.extraHeaders().forEach(httpResp::setHeader);
    }

    @Override
    public void visitEmpty(EmptyResponse resp) throws IOException {
        httpResp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        resp.extraHeaders().forEach(httpResp::setHeader);
    }

    @Override
    public <T> void visitEntity(EntityResponse<T> resp) throws IOException {
        // Override me! but call super.visitEntity(resp) too.

        resp.etag().ifPresent(etag -> {
            httpResp.setHeader("ETag", "\"" + etag + "\"");
        });

        resp.maxAge().ifPresent(maxAge -> {
            httpResp.setHeader(
                    "Cache-Control",
                    String.format("max-age=%d", maxAge.getSeconds()));
        });

        resp.extraHeaders().forEach(httpResp::setHeader);
    }

    @Override
    public void visitLocation(LocationResponse resp) throws IOException {
        httpResp.setStatus(resp.statusCode());
        httpResp.setHeader("Location", resp.target().toString());
        httpResp.setContentLength(0);
        resp.extraHeaders().forEach(httpResp::setHeader);
    }

    @Override
    public void visitNotModified(NotModifiedResponse resp) throws IOException {
        httpResp.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
        resp.extraHeaders().forEach(httpResp::setHeader);
    }

    @Override
    public void visitOptions(OptionsResponse resp) throws IOException {
        httpResp.setHeader("Allow", String.join(", ", resp.allowedMethods()));
        resp.extraHeaders().forEach(httpResp::setHeader);
    }
}
