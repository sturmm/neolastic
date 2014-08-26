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

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.AbstractIndexComponent;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenFilterFactoryFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.index.mapper.MapperService;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.elasticsearch.neolastic.service.SynonymsService;

/**
 * Created by sturmm on 11.08.14.
 */
public class NeolasticComponent extends AbstractIndexComponent {

    @Inject
    public NeolasticComponent(Index index,
                              @IndexSettings Settings settings,
                              IndicesAnalysisService indicesAnalysisService,
                              MapperService mapper,
                              final SynonymsService synonymsService) {
        super(index, settings);

        indicesAnalysisService.analyzerProviderFactories().put(
                "neo_synonyms",
                new PreBuiltAnalyzerProviderFactory("neo_synonyms",
                        AnalyzerScope.INDICES, new NeolasticAnalyser(
                        Lucene.ANALYZER_VERSION, synonymsService)
                )
        );

        indicesAnalysisService.tokenFilterFactories().put(
                "neo_synonyms_filter",
                new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
                    @Override
                    public String name() {
                        return "neo_synonyms_filter";
                    }

                    @Override
                    public TokenStream create(TokenStream tokenStream) {
                        return new NeolasticTokenFilter(tokenStream, synonymsService);
                    }
                })
        );
    }

}
