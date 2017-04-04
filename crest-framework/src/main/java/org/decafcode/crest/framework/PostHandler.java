package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.ServletException;

public interface PostHandler<T extends ResourcePath> {
    Response post(
            T path,
            Request req) throws IOException, ServletException;
}
