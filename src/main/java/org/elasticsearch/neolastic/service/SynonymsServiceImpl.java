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
package org.elasticsearch.neolastic.service;

import java.util.Iterator;
import java.util.List;

import org.elasticsearch.common.collect.ImmutableList;
import org.elasticsearch.common.collect.ImmutableMap;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;

/**
 * Created by sturmm on 19.08.14.
 */
public class SynonymsServiceImpl implements SynonymsService {

    private final Neo4jTemplate template;
    private final String cypherQuery;

    @Inject
    public SynonymsServiceImpl(Settings settings) {
        final String serverUrl = settings.get("neo4j.synonyms.server.url", "http://localhost:7474/db/data/");
        cypherQuery = settings
                .get("neo4j.synonyms.query", "MATCH (n:Term {lower: {value}})-[:SYNONYM|IS_A*1..3]->(m:Term) RETURN m.lower");

        template = new Neo4jTemplate((GraphDatabase) new SpringRestGraphDatabase(serverUrl));
    }

    @Override
    public List<String> getSynonyms(String term) {
        ImmutableMap<String, Object> params = ImmutableMap.<String, Object>builder().put("value", term).build();

        Iterator<String> iterator = template.query(cypherQuery, params).to(String.class).iterator();

        final ImmutableList<String> synonyms = ImmutableList.copyOf(iterator);

        return synonyms;
    }
}
