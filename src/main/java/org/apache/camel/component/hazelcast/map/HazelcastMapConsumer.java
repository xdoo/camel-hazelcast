package org.apache.camel.component.hazelcast.map;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.component.hazelcast.HazelcastDefaultConsumer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class HazelcastMapConsumer extends HazelcastDefaultConsumer {

	public HazelcastMapConsumer(Endpoint endpoint, Processor processor, String cacheName) {
		super(endpoint, processor, cacheName);
		
		IMap<String, Object> cache = Hazelcast.getMap(cacheName);
		cache.addEntryListener(new CamelEntryListener(), true);
	}

}
