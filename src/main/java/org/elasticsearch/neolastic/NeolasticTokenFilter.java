/*
 * Copyright 2014 Martin Sturm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.elasticsearch.neolastic;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.neolastic.service.SynonymsService;

/**
 * Credits to http://www.opten.ch/blog/2014/05/30/writing-a-custom-synonym-token-filter-in-lucenenet/
 */
public class NeolasticTokenFilter extends TokenFilter {

    private final CharTermAttribute termAttribute;
    private final PositionIncrementAttribute incrementAttribute;

    private final SynonymsService synonymService;

    private final Stack<String> currentSynonyms = new Stack<String>();

    private State state;

    @Inject
    public NeolasticTokenFilter(TokenStream input, SynonymsService synonymService) {
        super(input);
        this.synonymService = synonymService;
        this.incrementAttribute = addAttribute(PositionIncrementAttribute.class);
        this.termAttribute = addAttribute(CharTermAttribute.class);
    }

    @Override
    public final boolean incrementToken() throws IOException {

        if (!currentSynonyms.empty()) {
            restoreState(state);
            termAttribute.setEmpty().append(currentSynonyms.pop());
            incrementAttribute.setPositionIncrement(0);

            return true;

        } else if (!input.incrementToken()) {
            return false;
        } else {

            String currentTerm = termAttribute.toString();

            if (currentTerm != null && !currentTerm.isEmpty()) {
                List<String> synonyms = synonymService.getSynonyms(currentTerm);

                if (synonyms.isEmpty()) {
                    return true;
                }

                currentSynonyms.addAll(synonyms);
            }

            state = captureState();

            return true;
        }
    }
}
