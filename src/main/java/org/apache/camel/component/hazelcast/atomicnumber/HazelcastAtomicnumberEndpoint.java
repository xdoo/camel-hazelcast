package org.apache.camel.component.hazelcast.atomicnumber;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.component.hazelcast.HazelcastDefaultEndpoint;

public class HazelcastAtomicnumberEndpoint extends HazelcastDefaultEndpoint {

	public HazelcastAtomicnumberEndpoint(String uri, Component component, String cacheName) {
		super(uri, component, cacheName);
	}

	public Consumer createConsumer(Processor processor) throws Exception {
		throw new UnsupportedOperationException(
				"You cannot send messages to this endpoint:" + getEndpointUri());
	}

	public Producer createProducer() throws Exception {
		return new HazelcastAtomicnumberProducer(this, cacheName);
	}

}
