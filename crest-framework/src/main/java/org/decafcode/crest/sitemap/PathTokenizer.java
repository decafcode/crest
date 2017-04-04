package org.decafcode.crest.sitemap;

import com.google.common.collect.ImmutableList;

final class PathTokenizer {
    private PathTokenizer() {}

    static ImmutableList<String> tokenize(String path) {
        String[] bits = path.split("/");
        ImmutableList.Builder<String> tokens = ImmutableList.builder();

        char[] chars = path.toCharArray();
        int start = 0;
        int pos = 0;

        while (pos < chars.length) {
            char c = path.charAt(pos);

            if (c == '/') {
                if (pos > start) {
                    tokens.add(String.valueOf(chars, start, pos - start));
                }

                tokens.add("/");
                start = pos + 1;
            }

            pos++;
        }

        if (pos > start) {
            tokens.add(String.valueOf(chars, start, pos - start));
        }

        return tokens.build();
    }
}
