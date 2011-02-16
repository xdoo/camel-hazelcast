package org.apache.camel.component.hazelcast;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.CamelTestSupport;

public class TestHazelcastAtomicnumberProducer extends CamelTestSupport {
	
	public void testSet(){
		template.sendBody("direct:set", 4711);
		
		long body = template.requestBody("direct:get", null, Long.class);
		assertEquals(4711, body);
	}
	
	public void testGet(){		
		template.sendBody("direct:set", 1234);
		
		long body = template.requestBody("direct:get", null, Long.class);
		assertEquals(1234, body);
	}
	
	public void testIncrement(){
		template.sendBody("direct:set", 10);
		
		long body = template.requestBody("direct:increment", null, Long.class);
		assertEquals(11, body);
	}
	
	public void testDecrement(){
		template.sendBody("direct:set", 10);
		
		long body = template.requestBody("direct:decrement", null, Long.class);
		assertEquals(9, body);
	}
	
	public void testDestroy(){
		fail();
	}
	
	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				
				from("direct:set")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.SETVALUE_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.ATOMICNUMBER_PREFIX));
				
				from("direct:get")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.GET_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.ATOMICNUMBER_PREFIX));
				
				from("direct:increment")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.INCREMENT_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.ATOMICNUMBER_PREFIX));
				
				from("direct:decrement")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.DECREMENT_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.ATOMICNUMBER_PREFIX));
				
				from("direct:destroy")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.DESTROY_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.ATOMICNUMBER_PREFIX));
                		
			}
		};
	}
	
}
