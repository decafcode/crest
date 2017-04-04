package org.decafcode.crest.sitemap;

import java.util.Optional;

final class PathNodeBuilderSlashState implements PathNodeBuilderState {
    private Optional<PathNodeBuilder> nonterminal = Optional.empty();

    @Override
    public PathNodeBuilder addBranch(String branch) {
        throw new RouteConflictException();
    }

    @Override
    public PathNodeBuilder addCapture(String value) {
        throw new RouteConflictException();
    }

    @Override
    public PathNodeBuilder addSlash() {
        if (!nonterminal.isPresent()) {
            nonterminal = Optional.of(new PathNodeBuilder());
        }

        return nonterminal.get();
    }

    @Override
    public PathNode build(Optional<PathInstantiator> terminal) {
        return new PathNodeSlash(
                terminal,
                nonterminal.map(PathNodeBuilder::build));
    }
}
