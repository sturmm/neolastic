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

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;
import org.elasticsearch.index.settings.IndexSettings;
import org.elasticsearch.neolastic.NeolasticAnalyser;
import org.elasticsearch.neolastic.service.SynonymsService;

/**
 * Created by sturmm on 19.08.14.
 */
public class NeolasticAnalyzerProvider extends AbstractIndexAnalyzerProvider<NeolasticAnalyser> {

    private NeolasticAnalyser analyser;
    private String name;

    @Inject
    public NeolasticAnalyzerProvider(Index index,
                                     @IndexSettings Settings indexSettings,
                                     SynonymsService synonymsService,
                                     @Assisted String name,
                                     @Assisted Settings settings) {
        super(index, indexSettings, name, settings);

        this.name = name;
        analyser = new NeolasticAnalyser(version, synonymsService);
    }

    @Override
    public NeolasticAnalyser get() {
        return analyser;
    }
}
