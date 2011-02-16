package org.apache.camel.component.hazelcast;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.impl.DefaultConsumer;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryListener;

public class HazelcastDefaultConsumer extends DefaultConsumer {

	protected String cacheName;

	public HazelcastDefaultConsumer(Endpoint endpoint, Processor processor, String cacheName) {
		super(endpoint, processor);
		
		this.cacheName = cacheName; 
	}
	
	/**
	 * 
	 * @author claus
	 *
	 */
	public class CamelEntryListener implements EntryListener<String, Object>{

		public void entryAdded(EntryEvent<String, Object> event) {
			this.sendExchange(HazelcastConstants.ADDED, event.getKey(), event.getValue());
		}

		public void entryEvicted(EntryEvent<String, Object> event) {
			this.sendExchange(HazelcastConstants.ENVICTED, event.getKey(), event.getValue());
		}

		public void entryRemoved(EntryEvent<String, Object> event) {
			this.sendExchange(HazelcastConstants.REMOVED, event.getKey(), event.getValue());
		}

		public void entryUpdated(EntryEvent<String, Object> event) {
			this.sendExchange(HazelcastConstants.UPDATED, event.getKey(), event.getValue());
		}
		
		private void sendExchange(String operation, String key, Object value){
			Exchange exchange = getEndpoint().createExchange();
			
			//set object to body
			exchange.getOut().setBody(value);
			
			//set headers
			exchange.getOut().setHeader(HazelcastConstants.OBJECT_ID, key);
			HazelcastComponentHelper.setListenerHeaders(
					exchange, 
					HazelcastConstants.CACHE_LISTENER, 
					operation, 
					cacheName);
			
			try {
				getProcessor().process(exchange);
			} catch (Exception e) {
				if (exchange.getException() != null) {
					getExceptionHandler().handleException(
							String.format("Error processing exchange for hazelcast consumer on object '%s' in cache '%s'.", key, cacheName), exchange,
					exchange.getException());
				}
			}
		}
		
	}

}
