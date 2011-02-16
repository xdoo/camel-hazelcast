package org.apache.camel.component.hazelcast.atomicnumber;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.Exchange;
import org.apache.camel.component.hazelcast.HazelcastComponentHelper;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.impl.DefaultProducer;

import com.hazelcast.core.AtomicNumber;
import com.hazelcast.core.Hazelcast;

public class HazelcastAtomicnumberProducer extends DefaultProducer {

	private AtomicNumber atomicnumber;

	public HazelcastAtomicnumberProducer(Endpoint endpoint, String cacheName) {
		super(endpoint);
		
		this.atomicnumber = Hazelcast.getAtomicNumber(cacheName);
	}

	public void process(Exchange exchange) throws Exception {
		
		Map<String, Object> headers = exchange.getIn().getHeaders();
		
		//get header parameters
		int operation 	= -1;
		
		if(headers.containsKey(HazelcastConstants.OPERATION)){
			operation = (Integer) headers.get(HazelcastConstants.OPERATION);
		}
		
		switch (operation) {
		
		case HazelcastConstants.INCREMENT_OPERATION:
			this.increment(exchange);
			break;
			
		case HazelcastConstants.DECREMENT_OPERATION:
			this.decrement(exchange);
			break;
		
		case HazelcastConstants.SETVALUE_OPERATION:
			this.set(exchange);
			break;
			
		case HazelcastConstants.GET_OPERATION:
			this.get(exchange);
			break;
			
		case HazelcastConstants.DESTROY_OPERATION:
			this.destroy();
			break;
			
		default:
			throw new IllegalArgumentException(
					String.format("The value '%s' is not allowed for parameter '%s' on the ATOMICNUMBER.", operation, HazelcastConstants.OPERATION));
		}
		
		//finally copy headers
		HazelcastComponentHelper.copyHeaders(exchange);

	}

	private void get(Exchange exchange) {
		exchange.getOut().setBody(this.atomicnumber.get());
	}

	private void increment(Exchange exchange) {
		exchange.getOut().setBody(this.atomicnumber.incrementAndGet());
	}

	private void decrement(Exchange exchange) {
		exchange.getOut().setBody(this.atomicnumber.decrementAndGet());
	}

	private void set(Exchange exchange) {
		this.atomicnumber.set(exchange.getIn().getBody(Long.class));
	}

	private void destroy() {
		this.atomicnumber.destroy();
	}

}
