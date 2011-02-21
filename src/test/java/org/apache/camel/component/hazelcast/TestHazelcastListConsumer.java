package org.apache.camel.component.hazelcast;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.hazelcast.core.Hazelcast;

/**
 *
 * @author ipolyzos
 *
 */
public class TestHazelcastListConsumer extends CamelTestSupport{
	@Test
	public void add() throws InterruptedException {
		MockEndpoint out = getMockEndpoint("mock:added");
		out.expectedMessageCount(1);

		List<String> list = Hazelcast.getList("mm");
		list.clear();

		list.add("foo");

		assertMockEndpointsSatisfied(2000, TimeUnit.MILLISECONDS);

		this.checkHeaders(out.getExchanges().get(0).getIn().getHeaders(), HazelcastConstants.ADDED);
	}

	@Test
	public void remove() throws InterruptedException {
		MockEndpoint out = getMockEndpoint("mock:removed");

		out.expectedMessageCount(1);

		List<String> list = Hazelcast.getList("mm");
		list.clear();

		list.add("foo");
		list.remove("foo");

		assertMockEndpointsSatisfied(2000, TimeUnit.MILLISECONDS);
		this.checkHeaders(out.getExchanges().get(0).getIn().getHeaders(), HazelcastConstants.REMOVED);
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from(String.format("hazelcast:%smm", HazelcastConstants.LIST_PREFIX))
					.log("object...")
					.choice()
						.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.ADDED))
							.log("...added").to("mock:added")
						.when(header(HazelcastConstants.LISTENER_ACTION).isEqualTo(HazelcastConstants.REMOVED))
							.log("...removed").to("mock:removed").otherwise().log("fail!");

			}
		};
	}

	private void checkHeaders(Map<String, Object> headers, String action) {
		assertEquals(action, headers.get(HazelcastConstants.LISTENER_ACTION));
		assertEquals(HazelcastConstants.CACHE_LISTENER, headers.get(HazelcastConstants.LISTENER_TYPE));
		assertEquals(null, headers.get(HazelcastConstants.OBJECT_ID));
		assertNotNull(headers.get(HazelcastConstants.LISTENER_TIME));
	}
}
