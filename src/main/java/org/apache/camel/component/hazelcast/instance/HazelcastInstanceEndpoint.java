package org.apache.camel.component.hazelcast.instance;

import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.hazelcast.HazelcastComponent;
import org.apache.camel.component.hazelcast.HazelcastDefaultEndpoint;

public class HazelcastInstanceEndpoint extends HazelcastDefaultEndpoint {

	
	public HazelcastInstanceEndpoint(String uri, HazelcastComponent component) {
		super(uri, component);		
	}
	
	public Consumer createConsumer(Processor processor) throws Exception {
		return new HazelcastInstanceConsumer(this, processor);
	}

	public Producer createProducer() throws Exception {
		throw new UnsupportedOperationException(
				"You cannot send messages to this endpoint:" + getEndpointUri());

	}

}
