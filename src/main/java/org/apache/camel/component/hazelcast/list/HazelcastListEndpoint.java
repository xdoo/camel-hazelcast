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
package org.apache.camel.component.hazelcast.list;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.hazelcast.HazelcastDefaultEndpoint;

/**
 * Hazelcast List {@link Endpoint} implementation.
 * 
 * @author ipolyzos
 */
public class HazelcastListEndpoint extends HazelcastDefaultEndpoint {

    public HazelcastListEndpoint(String endpointUri, Component component, String cacheName) {
        super(endpointUri, component, cacheName);
    }

    @Override
    public Consumer createConsumer(Processor processor) throws Exception {
        return new HazelcastListConsumer(this, processor, cacheName);
    }

    @Override
    public Producer createProducer() throws Exception {
        return new HazelcastListProducer(this, cacheName);
    }

}