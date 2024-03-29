/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.hazelcast;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

public class TestHazelcastErrorMessages extends CamelTestSupport {

    @Test
    public void testUriPrefix() {
        RouteBuilder builder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("direct:prefix").to("hazelcast:error:foo");

            }
        };

        try {
            context.addRoutes(builder);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains(
                    "Your URI does not provide a correct 'type' prefix. It should be anything like "
                            + "'hazelcast:[map:|multimap:|atomicvalue:|instance:|queue:|seda:|list:]name' but is 'hazelcast://error:foo"));
        }
    }

    @Test
    public void testAtomicNumberConsumer() {
        RouteBuilder builder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("hazelcast:atomicvalue:foo").to("seda:out");

            }
        };

        try {
            context.addRoutes(builder);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("You cannot send messages to this endpoint: hazelcast://atomicvalue:foo"));
        }
    }

    @Test
    public void testInstanceProducer() {
        RouteBuilder builder = new RouteBuilder() {

            @Override
            public void configure() throws Exception {

                from("direct:foo").to("hazelcast:instance:foo");

            }
        };

        try {
            context.addRoutes(builder);
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("You cannot send messages to this endpoint: hazelcast://instance:foo"));
        }
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

            }
        };
    }

}
