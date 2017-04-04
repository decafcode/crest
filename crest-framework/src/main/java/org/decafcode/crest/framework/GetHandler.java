package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.ServletException;

public interface GetHandler<T extends ResourcePath> {
    Response get(
            T path,
            Request req) throws IOException, ServletException;
}
