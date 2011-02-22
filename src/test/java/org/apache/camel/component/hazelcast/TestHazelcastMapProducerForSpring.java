package org.apache.camel.component.hazelcast;

import java.io.Serializable;
import java.util.Collection;

import org.apache.camel.test.CamelSpringTestSupport;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class TestHazelcastMapProducerForSpring extends CamelSpringTestSupport implements Serializable {

	private IMap<String, Object> map;
	
	@Override
	public void setUp() throws Exception{
		super.setUp();
		
		this.map = Hazelcast.getMap("foo");
		this.map.clear();
	}
	
	@Override
	public void tearDown()throws Exception{
		super.tearDown();
		
		this.map.clear();
	}
	
	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return  new ClassPathXmlApplicationContext("/META-INF/spring/test-camel-context-map.xml");
	}
	
	public void testPut() throws InterruptedException{
		template.sendBodyAndHeader("direct:put", "my-foo", HazelcastConstants.OBJECT_ID, "4711");
		
		assertTrue(map.containsKey("4711"));
		assertEquals("my-foo", map.get("4711"));
	}
	
	public void testUpdate(){
		template.sendBodyAndHeader("direct:put", "my-foo", HazelcastConstants.OBJECT_ID, "4711");
		
		assertTrue(map.containsKey("4711"));
		assertEquals("my-foo", map.get("4711"));
		
		template.sendBodyAndHeader("direct:update", "my-fooo", HazelcastConstants.OBJECT_ID, "4711");
		assertEquals("my-fooo", map.get("4711"));
	}
	
	public void testGet(){
		map.put("4711", "my-foo");
		
		template.sendBodyAndHeader("direct:get", null, HazelcastConstants.OBJECT_ID, "4711");
		String body = consumer.receiveBody("seda:out", 5000, String.class);
		
		assertEquals("my-foo", body);
	}
	
	public void testDelete(){
		map.put("4711", "my-foo");
		assertEquals(1, map.size());
		
		template.sendBodyAndHeader("direct:delete", null, HazelcastConstants.OBJECT_ID, "4711");
		assertEquals(0, map.size());
	}
	
	public void testQuery(){		
		map.put("1", new Dummy("alpha", 1000));
		map.put("2", new Dummy("beta", 2000));
		map.put("3", new Dummy("gamma", 3000));
		
		String q1 = "bar > 1000";
		String q2 = "foo LIKE alp%";
		
		template.sendBodyAndHeader("direct:query", null, HazelcastConstants.QUERY, q1);
		Collection<Dummy> b1 = consumer.receiveBody("seda:out", 5000, Collection.class);
		
		assertNotNull(b1);
		assertEquals(2, b1.size());
		
		template.sendBodyAndHeader("direct:query", null, HazelcastConstants.QUERY, q2);
		Collection<Dummy> b2 = consumer.receiveBody("seda:out", 5000, Collection.class);
		
		assertNotNull(b2);
		assertEquals(1, b2.size());
	}
	
	public class Dummy implements Serializable{

		private static final long serialVersionUID = 3688457704655925278L;
		
		public Dummy(String foo, int bar){
			this.foo = foo;
			this.bar = bar;
		}
		
		private String foo;
		private int bar;
		
		
		public String getFoo() {
			return foo;
		}
		public void setFoo(String foo) {
			this.foo = foo;
		}
		public int getBar() {
			return bar;
		}
		public void setBar(int bar) {
			this.bar = bar;
		}
		
	}

}
