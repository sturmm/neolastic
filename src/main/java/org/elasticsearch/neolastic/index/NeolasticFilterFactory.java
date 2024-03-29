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
package org.elasticsearch.neolastic.index;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.neolastic.NeolasticTokenFilter;
import org.elasticsearch.neolastic.service.SynonymsService;


public class NeolasticFilterFactory extends AbstractTokenFilterFactory {

    private final SynonymsService synonymsService;

    @Inject
    public NeolasticFilterFactory(Index index,
                                  @IndexSettings Settings indexSettings,
                                  @Assisted String name,
                                  SynonymsService synonymsService,
                                  @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.synonymsService = synonymsService;
    }

    @Override public TokenStream create(TokenStream tokenStream) {
        return new NeolasticTokenFilter(tokenStream, synonymsService);
    }
}
