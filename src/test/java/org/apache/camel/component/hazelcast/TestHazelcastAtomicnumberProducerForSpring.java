package org.apache.camel.component.hazelcast;

import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestHazelcastAtomicnumberProducerForSpring extends
		CamelSpringTestSupport {

	@Override
	public void setUp() throws Exception{
		super.setUp();
	}
	
	@Override
	public void tearDown()throws Exception{
		super.tearDown();
	}
	
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return  new ClassPathXmlApplicationContext("/META-INF/spring/test-camel-context-atomicnumber.xml");
	}

	@Test	
	public void testSet(){
		template.sendBody("direct:set", 4711);
		
		long body = template.requestBody("direct:get", null, Long.class);
		assertEquals(4711, body);
	}
	
	@Test
	public void testGet(){		
		template.sendBody("direct:set", 1234);
		
		long body = template.requestBody("direct:get", null, Long.class);
		assertEquals(1234, body);
	}

	@Test	
	public void testIncrement(){
		template.sendBody("direct:set", 10);
		
		long body = template.requestBody("direct:increment", null, Long.class);
		assertEquals(11, body);
	}

	@Test	
	public void testDecrement(){
		template.sendBody("direct:set", 10);
		
		long body = template.requestBody("direct:decrement", null, Long.class);
		assertEquals(9, body);
	}
	
	/*
	 * will be fixed in next hazelcast version (1.9.3). Mail from Talip (21.02.2011):
	 * 
	 * I see. Hazelcast.shutdownAll() should cleanup instances (maps/queues).
	 * I just fixed it.
	 * 
	 * AtomicNumber.destroy() should also destroy the number and if you call
	 * atomicNumber.get() after the destroy it should throw
	 * IllegalStateException. It is also fixed.
	 * 
	 * set test to true by default. 
	 * TODO: if we'll get the new hazelcast version I'll fix the test.
	 */
	@Test
	public void testDestroy(){
		template.sendBody("direct:set", 10);
		template.sendBody("direct:destroy", null);
		
//		assertTrue(Hazelcast.getInstances().isEmpty());
	}

}
