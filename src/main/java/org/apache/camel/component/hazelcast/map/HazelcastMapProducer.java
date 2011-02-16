package org.apache.camel.component.hazelcast.map;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.component.hazelcast.HazelcastComponentHelper;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.impl.DefaultProducer;
import org.apache.camel.util.ObjectHelper;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;
import com.hazelcast.query.SqlPredicate;

public class HazelcastMapProducer extends DefaultProducer {

	private IMap<String, Object> cache;

	public HazelcastMapProducer(HazelcastMapEndpoint endpoint, String cacheName) {
		super(endpoint);
		
		this.cache = Hazelcast.getMap(cacheName);
	}

	public void process(Exchange exchange) throws Exception {
		
		Map<String, Object> headers = exchange.getIn().getHeaders();
		
		//get header parameters
		String oid 		= null;
		int operation 	= -1;
		String query 	= null;
		
		if(headers.containsKey(HazelcastConstants.OBJECT_ID)){
			oid = (String) headers.get(HazelcastConstants.OBJECT_ID);
		}
		
		if(headers.containsKey(HazelcastConstants.OPERATION)){
			operation = (Integer) headers.get(HazelcastConstants.OPERATION);
		}
		
		if(headers.containsKey(HazelcastConstants.QUERY)){
			query = (String) headers.get(HazelcastConstants.QUERY);
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
			
		case HazelcastConstants.UPDATE_OPERATION:
			this.update(oid, exchange);
			break;
			
		case HazelcastConstants.QUERY_OPERATION:
			this.query(query, exchange);
			break;
			
		default:
			throw new IllegalArgumentException(
					String.format("The value '%s' is not allowed for parameter '%s' on the MAP cache.", operation, HazelcastConstants.OPERATION));
		}
		
		//finally copy headers
		HazelcastComponentHelper.copyHeaders(exchange);

	}

	/**
	 * query map with a sql like syntax (see http://www.hazelcast.com/)
	 * 
	 * @param query
	 * @param exchange
	 */
	private void query(String query, Exchange exchange) {
		exchange.getOut().setBody(this.cache.values(new SqlPredicate(query)));
	}

	/**
	 * update an object in your cache (the whole object
	 * will be replaced)
	 * 
	 * @param oid
	 * @param exchange
	 */
	private void update(String oid, Exchange exchange) {
		Object body = exchange.getIn().getBody();
		this.cache.lock(oid);
		this.cache.replace(oid, body);
		this.cache.unlock(oid);
	}
	
	/**
	 * remove an object from the cache
	 * 
	 * @param oid
	 */
	private void delete(String oid) {
		this.cache.remove(oid);
	}
	
	/**
	 * find an object by the given id and give
	 * it back
	 * 
	 * @param oid
	 * @param exchange
	 */
	private void get(String oid, Exchange exchange) {
		exchange.getOut().setBody(this.cache.get(oid));
	}

	/**
	 * put a new object into the cache
	 * 
	 * @param oid
	 * @param exchange
	 */
	private void put(String oid, Exchange exchange) {
		Object body = exchange.getIn().getBody();
		this.cache.put(oid, body);
	}
}
