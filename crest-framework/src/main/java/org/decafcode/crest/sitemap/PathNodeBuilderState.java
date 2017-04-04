package org.decafcode.crest.sitemap;

import java.util.Optional;

interface PathNodeBuilderState {
    PathNodeBuilder addBranch(String branch);

    PathNodeBuilder addCapture(String value);

    PathNodeBuilder addSlash();

    PathNode build(Optional<PathInstantiator> terminal);
}
