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

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.de.GermanAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.Version;
import org.elasticsearch.neolastic.service.SynonymsService;

/**
 * Created by sturmm on 13.08.14.
 */
public class NeolasticAnalyser extends Analyzer {

    private final Version version;
    private final SynonymsService synonymsService;

    public NeolasticAnalyser(Version version, SynonymsService synonymsService) {
        this.version = version;
        this.synonymsService = synonymsService;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        final Tokenizer tok = new StandardTokenizer(version, reader);
        TokenStream filter = new LowerCaseFilter(version, tok);

        filter = new StopFilter(version, filter, GermanAnalyzer.getDefaultStopSet());

        return new TokenStreamComponents(tok, new NeolasticTokenFilter(filter, synonymsService));
    }
}
