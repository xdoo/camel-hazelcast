package org.apache.camel.component.hazelcast.multimap;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.hazelcast.HazelcastComponentHelper;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.impl.DefaultProducer;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MultiMap;

public class HazelcastMultimapProducer extends DefaultProducer {

	private MultiMap<Object, Object> cache;

	public HazelcastMultimapProducer(Endpoint endpoint, String cacheName) {
		super(endpoint);
		
		this.cache = Hazelcast.getMultiMap(cacheName);
	}

	public void process(Exchange exchange) throws Exception {
		
		Map<String, Object> headers = exchange.getIn().getHeaders();
		
		//get header parameters
		String oid 		= null;
		int operation 	= -1;
		
		if(headers.containsKey(HazelcastConstants.OBJECT_ID)){
			oid = (String) headers.get(HazelcastConstants.OBJECT_ID);
		}
		
		if(headers.containsKey(HazelcastConstants.OPERATION)){
			operation = (Integer) headers.get(HazelcastConstants.OPERATION);
		}
		
		switch (operation) {
		case HazelcastConstants.PUT_OPERATION:
			this.put(oid, exchange);
			break;
			
		case HazelcastConstants.GET_OPERATION:
			this.get(oid, exchange);
			break;
			
		case HazelcastConstants.DELETE_OPERATION:
			this.delete(oid);
			break;
			
		case HazelcastConstants.REMOVEVALUE_OPERATION:
			this.removevalue(oid, exchange);
			break;
			
		default:
			throw new IllegalArgumentException(
					String.format("The value '%s' is not allowed for parameter '%s' on the MULTIMAP cache.", operation, HazelcastConstants.OPERATION));
		}
		
		//finally copy headers
		HazelcastComponentHelper.copyHeaders(exchange);

	}

	private void put(String oid, Exchange exchange) {
		Object body = exchange.getIn().getBody();
		this.cache.put(oid, body);
	}

	private void get(String oid, Exchange exchange) {
		exchange.getOut().setBody(this.cache.get(oid));
	}

	private void delete(String oid) {
		this.cache.remove(oid);
	}
	
	private void removevalue(String oid, Exchange exchange) {
		this.cache.remove(oid, exchange.getIn().getBody());
	}

}
