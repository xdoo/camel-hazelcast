package org.apache.camel.component.hazelcast.map;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.hazelcast.HazelcastComponent;
import org.apache.camel.component.hazelcast.HazelcastDefaultEndpoint;

public class HazelcastMapEndpoint extends HazelcastDefaultEndpoint {

	public HazelcastMapEndpoint(String uri, String cacheName, HazelcastComponent component) {
		super(uri, component, cacheName);	
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		return new HazelcastMapConsumer(this, processor, cacheName);
	}

	public Producer createProducer() throws Exception {
		return new HazelcastMapProducer(this, cacheName);
	}

}
