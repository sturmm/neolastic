package org.elasticsearch.plugin;

import org.elasticsearch.util.EmbeddedElasticsearchServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class NeolasticPluginTest {

    private static EmbeddedElasticsearchServer server;

    @BeforeClass
    public static void setup() {
        server = new EmbeddedElasticsearchServer();
    }

    @AfterClass
    public void tearDown() throws Exception {
        server.shutdown();
    }

    @Test
    public void loop() throws Exception {


        while (true) {
            Thread.sleep(100);
        }
    }


}