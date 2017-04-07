package org.decafcode.crest.framework;

import java.io.IOException;
import javax.servlet.ServletException;

public interface PatchHandler<T extends ResourcePath> {
    Response patch(
            T path,
            Request req) throws IOException, ServletException;
}
