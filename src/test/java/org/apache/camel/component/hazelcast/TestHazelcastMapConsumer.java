package org.apache.camel.component.hazelcast;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.CamelTestSupport;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class TestHazelcastMapConsumer extends CamelTestSupport {

	public void testAdd() throws InterruptedException{
		MockEndpoint out = getMockEndpoint("mock:added");
		out.expectedMessageCount(1);
		
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.put("4711", "my-foo");
		
		assertMockEndpointsSatisfied(5000, TimeUnit.MILLISECONDS);
		
		this.checkHeaders(out.getExchanges().get(0).getIn().getHeaders(), HazelcastConstants.ADDED);
	}
	
	public void testEnvict(){
		fail(); //TODO --> implement test
	}
	
	public void testUpdate() throws InterruptedException{
		MockEndpoint out = getMockEndpoint("mock:updated");
		out.expectedMessageCount(1);
		
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.replace("4711", "my-fooo");
		
		assertMockEndpointsSatisfied(5000, TimeUnit.MILLISECONDS);
		
		this.checkHeaders(out.getExchanges().get(0).getIn().getHeaders(), HazelcastConstants.UPDATED);
	}
	
	public void testRemove() throws InterruptedException{
		MockEndpoint out = getMockEndpoint("mock:removed");
		out.expectedMessageCount(1);
		
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.remove("4711");
		
		assertMockEndpointsSatisfied(5000, TimeUnit.MILLISECONDS);
		this.checkHeaders(out.getExchanges().get(0).getIn().getHeaders(), HazelcastConstants.REMOVED);
	}
	
	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX))
				.log("object...")
				.choice()
                	.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.ADDED))
                		.log("...added")
                		.to("mock:added")
                	.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.ENVICTED))
                		.log("...envicted")
                		.to("mock:envicted")
                	.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.UPDATED))
                		.log("...updated")
                		.to("mock:updated")
                	.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.REMOVED))
                		.log("...removed")
                		.to("mock:removed")
                	.otherwise()
                		.log("fail!");
                		
			}
		};
	}
	
	private void checkHeaders(Map<String, Object> headers, String action){
		assertEquals(action, headers.get(HazelcastConstants.LISTENER_ACTION));
		assertEquals(HazelcastConstants.CACHE_LISTENER, headers.get(HazelcastConstants.LISTENER_TYPE));
		assertEquals("4711", headers.get(HazelcastConstants.OBJECT_ID));
		assertNotNull(headers.get(HazelcastConstants.LISTENER_TIME));
	}
}
