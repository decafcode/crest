package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.ServletException;

public interface DeleteHandler<T extends ResourcePath> {
    Response delete(
            T path,
            Request req) throws IOException, ServletException;
}
