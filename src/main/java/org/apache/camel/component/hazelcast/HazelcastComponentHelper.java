package org.apache.camel.component.hazelcast;

import java.util.Date;
import java.util.Map;

import org.apache.camel.Exchange;

public class HazelcastComponentHelper {

	public static void copyHeaders(Exchange ex){
		//get in headers
		Map<String, Object> headers = ex.getIn().getHeaders();
		
		//delete item id
		if(headers.containsKey(HazelcastConstants.OBJECT_ID)){
			headers.remove(HazelcastConstants.OBJECT_ID);
		}
		
		if(headers.containsKey(HazelcastConstants.OPERATION)){
			headers.remove(HazelcastConstants.OPERATION);
		}
		
		//set out headers
		ex.getOut().setHeaders(headers);
	}
	
	public static void setListenerHeaders(Exchange ex, String listenerType, String listenerAction, String cacheName){
		ex.getOut().setHeader(HazelcastConstants.CACHE_NAME, cacheName);
		HazelcastComponentHelper.setListenerHeaders(ex, listenerType, listenerAction);
	}
	
	public static void setListenerHeaders(Exchange ex, String listenerType, String listenerAction){
		
		ex.getOut().setHeader(HazelcastConstants.LISTENER_ACTION, listenerAction);
		ex.getOut().setHeader(HazelcastConstants.LISTENER_TYPE, listenerType);
		ex.getOut().setHeader(HazelcastConstants.LISTENER_TIME, new Date().getTime());
	}
	
}
