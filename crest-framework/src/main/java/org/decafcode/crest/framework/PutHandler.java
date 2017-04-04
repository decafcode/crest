package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.ServletException;

public interface PutHandler<T extends ResourcePath> {
    Response put(
            T path,
            Request req) throws IOException, ServletException;
}
