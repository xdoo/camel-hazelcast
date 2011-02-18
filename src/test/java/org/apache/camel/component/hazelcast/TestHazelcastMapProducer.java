/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.hazelcast;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.CamelTestSupport;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.IMap;

public class TestHazelcastMapProducer extends CamelTestSupport {

	public void testPut() throws InterruptedException{
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.clear();
		
		template.sendBodyAndHeader("direct:put", "my-foo", HazelcastConstants.OBJECT_ID, "4711");
		
		assertTrue(map.containsKey("4711"));
		assertEquals("my-foo", map.get("4711"));
		
		map.clear();
	}
	
	public void testUpdate(){
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.clear();
		
		template.sendBodyAndHeader("direct:put", "my-foo", HazelcastConstants.OBJECT_ID, "4711");
		
		assertTrue(map.containsKey("4711"));
		assertEquals("my-foo", map.get("4711"));
		
		template.sendBodyAndHeader("direct:update", "my-fooo", HazelcastConstants.OBJECT_ID, "4711");
		assertEquals("my-fooo", map.get("4711"));
		
		map.clear();
	}
	
	public void testGet(){
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.clear();
		
		map.put("4711", "my-foo");
		
		template.sendBodyAndHeader("direct:get", null, HazelcastConstants.OBJECT_ID, "4711");
		String body = consumer.receiveBody("seda:out", 5000, String.class);
		
		assertEquals("my-foo", body);
		
		map.clear();
	}
	
	public void testDelete(){
		IMap<String, Object> map = Hazelcast.getMap("foo");
		map.clear();
		
		map.put("4711", "my-foo");
		assertEquals(1, map.size());
		
		template.sendBodyAndHeader("direct:delete", null, HazelcastConstants.OBJECT_ID, "4711");
		assertEquals(0, map.size());
		
		map.clear();
	}
	
	public void testQuery(){
		fail();
	}
	
	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				
				from("direct:put")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.PUT_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX));
				
				from("direct:update")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.UPDATE_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX));
				
				from("direct:get")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.GET_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX))
				.to("seda:out");
				
				from("direct:delete")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.DELETE_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX));
				
				from("direct:query")
				.setHeader(HazelcastConstants.OPERATION, constant(HazelcastConstants.DELETE_OPERATION))
				.to(String.format("hazelcast:%sfoo", HazelcastConstants.MAP_PREFIX))
				.to("seda:out");
                		
			}
		};
	}
	
}
