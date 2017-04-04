package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public interface ResponseWriter {
    void writeResponse(Response resp, HttpServletResponse httpResp)
            throws IOException;
}
