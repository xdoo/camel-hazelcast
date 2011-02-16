package org.apache.camel.component.hazelcast;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;

public abstract class HazelcastDefaultEndpoint extends DefaultEndpoint {

	protected String cacheName;
	
	public HazelcastDefaultEndpoint(String endpointUri, Component component, String cacheName) {
		super(endpointUri, component);
		
		this.cacheName = cacheName;
	}
	
	public HazelcastDefaultEndpoint(String endpointUri, Component component) {
		super(endpointUri, component);
	}

	public abstract Consumer createConsumer(Processor processor) throws Exception;

	public abstract Producer createProducer() throws Exception;

	public boolean isSingleton() {
		return true;
	}

}
