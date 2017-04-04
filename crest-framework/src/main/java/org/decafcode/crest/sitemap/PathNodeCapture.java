package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;
import java.util.Objects;
import java.util.Optional;

final class PathNodeCapture implements PathNode {
    private final Optional<PathInstantiator> terminal;
    private final Optional<PathNode> nonterminal;
    private final String varName;

    PathNodeCapture(
            Optional<PathInstantiator> terminal,
            Optional<PathNode> nonterminal,
            String varName) {
        this.terminal = Objects.requireNonNull(terminal, "terminal");
        this.nonterminal = Objects.requireNonNull(nonterminal, "nonterminal");
        this.varName = Objects.requireNonNull(varName, "varName");
    }

    @Override
    public Optional<PathBuilder> traverse(
            ImmutableList<String> tokens,
            int pos) {
        if (pos >= tokens.size()) {
            return terminal.map(PathBuilder::new);
        }

        return nonterminal
                .flatMap(next -> next.traverse(tokens, pos + 1))
                .map(result -> result.putPathParam(varName, tokens.get(pos)));
    }
}
