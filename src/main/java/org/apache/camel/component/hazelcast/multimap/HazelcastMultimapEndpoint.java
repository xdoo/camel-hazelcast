package org.apache.camel.component.hazelcast.multimap;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.hazelcast.HazelcastComponent;
import org.apache.camel.component.hazelcast.HazelcastDefaultEndpoint;

public class HazelcastMultimapEndpoint extends HazelcastDefaultEndpoint {

	public HazelcastMultimapEndpoint(String uri, String cacheName, HazelcastComponent component) {
		super(uri, component, cacheName);	
	}
	
	public Consumer createConsumer(Processor processor) throws Exception {
		return new HazelcastMultimapConsumer(this, processor, cacheName);
	}

	public Producer createProducer() throws Exception {
		return new HazelcastMultimapProducer(this, cacheName);
	}

}
