package org.decafcode.crest.framework;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Optional;

public interface Request {
    <T> T authenticate(Class<T> xclass);

    <T> T extract(Class<T> xclass) throws IOException, ServletException;

    Optional<String> ifMatch();

    Optional<String> ifNoneMatch();
}
