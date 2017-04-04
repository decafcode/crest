package org.decafcode.crest.sitemap;

import java.util.Objects;
import java.util.Optional;

final class PathNodeBuilderCaptureState implements PathNodeBuilderState {
    private final String varName;

    private Optional<PathNodeBuilder> nonterminal = Optional.empty();

    PathNodeBuilderCaptureState(String varName) {
        this.varName = Objects.requireNonNull(varName, "varName");
    }

    @Override
    public PathNodeBuilder addBranch(String branch) {
        throw new RouteConflictException();
    }

    @Override
    public PathNodeBuilder addCapture(String value) {
        if (!varName.equals(value)) {
            throw new RouteConflictException();
        }

        if (!nonterminal.isPresent()) {
            nonterminal = Optional.of(new PathNodeBuilder());
        }

        return nonterminal.get();
    }

    @Override
    public PathNodeBuilder addSlash() {
        throw new RouteConflictException();
    }

    @Override
    public PathNode build(Optional<PathInstantiator> terminal) {
        return new PathNodeCapture(
                terminal,
                nonterminal.map(PathNodeBuilder::build),
                varName);
    }
}
