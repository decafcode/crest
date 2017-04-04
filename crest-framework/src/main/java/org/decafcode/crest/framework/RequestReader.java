package org.decafcode.crest.framework;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface RequestReader {
    Request read(HttpServletRequest req) throws IOException, ServletException;
}
