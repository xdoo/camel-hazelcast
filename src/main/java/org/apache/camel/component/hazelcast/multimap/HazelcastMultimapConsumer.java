package org.apache.camel.component.hazelcast.multimap;

import org.apache.camel.Endpoint;
import org.apache.camel.Processor;
import org.apache.camel.component.hazelcast.HazelcastDefaultConsumer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.MultiMap;

public class HazelcastMultimapConsumer extends HazelcastDefaultConsumer {

	public HazelcastMultimapConsumer(Endpoint endpoint, Processor processor, String cacheName) {
		super(endpoint, processor, cacheName);
		
		MultiMap<String, Object> cache = Hazelcast.getMultiMap(cacheName);
		cache.addEntryListener(new CamelEntryListener(), true);
	}

}
