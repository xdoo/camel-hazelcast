/**
 * Copyright 2011 Ioannis Polyzos
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */
package org.apache.camel.component.hazelcast;

import org.apache.camel.EndpointInject;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelSpringTestSupport;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Spring xml configuration support tests.
 *
 * Based on {@link http://camel.apache.org/spring.html}
 *
 * @author ipolyzos
 */
//@Ignore("Tests should run manually.")
public class TestHazelcastSpringSupport extends CamelSpringTestSupport {

	@EndpointInject(uri = "mock:result")
	private MockEndpoint mock;

	@Test
	public void simpleSend() throws Exception {
		mock.expectedMessageCount(1);

		template.sendBody("hazelcast:seda:foo", "test");

		assertMockEndpointsSatisfied();
	}

	@Override
	protected ClassPathXmlApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("org/apache/camel/component/hzlq/test/seda-test-route.xml");
	}
}
