package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import org.decafcode.crest.exception.NotFoundException;
import org.decafcode.crest.framework.ResourcePath;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletException;

final class ForwardSiteMap {
    private final PathNode root;

    ForwardSiteMap(PathNode root) {
        this.root = Objects.requireNonNull(root, "root");
    }

    ResourcePath readPath(String uriPath, Map<String, String> queryParams)
            throws ServletException {
        if (uriPath == null || !uriPath.startsWith("/")) {
            throw new IllegalArgumentException();
        }

        ImmutableList<String> tokens = PathTokenizer.tokenize(uriPath);

        PathBuilder pathBuilder = root
                .traverse(tokens, 0)
                .orElseThrow(NotFoundException::new);

        pathBuilder = pathBuilder.putQueryParams(queryParams);

        return pathBuilder.build();
    }
}
