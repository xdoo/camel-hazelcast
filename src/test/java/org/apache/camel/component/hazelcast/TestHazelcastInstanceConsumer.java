package org.apache.camel.component.hazelcast;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class TestHazelcastInstanceConsumer extends CamelTestSupport {

	public void testAddInstance() throws InterruptedException{
		
		MockEndpoint added = getMockEndpoint("mock:added");
		added.setExpectedMessageCount(2);
		
		Hazelcast.newHazelcastInstance(null);
		Hazelcast.newHazelcastInstance(null);
		
		assertMockEndpointsSatisfied(5000, TimeUnit.MILLISECONDS);
		
		//check headers
		Exchange ex = added.getExchanges().get(0);
		Map<String, Object> headers = ex.getIn().getHeaders();
		
		this.checkHeaders(headers, HazelcastConstants.ADDED);
	}
	
	public void testRemoveInstance() throws InterruptedException{
		
		MockEndpoint removed = getMockEndpoint("mock:removed");
		removed.setExpectedMessageCount(1);
		
		HazelcastInstance h1 = Hazelcast.newHazelcastInstance(null);
		
		//TODO --> check how an instance can be killed...
		h1.shutdown();
		
		assertMockEndpointsSatisfied(5000, TimeUnit.MILLISECONDS);
		
		//check headers
		Exchange ex = removed.getExchanges().get(0);
		Map<String, Object> headers = ex.getIn().getHeaders();
		
		this.checkHeaders(headers, HazelcastConstants.REMOVED);
	}
	
	
	
	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(String.format("hazelcast:%sfoo", HazelcastConstants.INSTANCE_PREFIX))
				.log("instance...")
				.choice()
                	.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.ADDED))
                		.log("...added")
                		.to("mock:added")
                	.otherwise()
                		.log("...removed")
                		.to("mock:removed");
			}
		};
	}
	
	private void checkHeaders(Map<String, Object> headers, String action){
		assertEquals(action, headers.get(HazelcastConstants.LISTENER_ACTION));
		assertEquals(HazelcastConstants.INSTANCE_LISTENER, headers.get(HazelcastConstants.LISTENER_TYPE));
		assertNotNull(headers.get(HazelcastConstants.LISTENER_TIME));
		assertNotNull(headers.get(HazelcastConstants.INSTANCE_HOST));
		assertNotNull(headers.get(HazelcastConstants.INSTANCE_PORT));
	}
}
