package org.apache.camel.component.hazelcast.instance;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.hazelcast.HazelcastComponentHelper;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.impl.DefaultConsumer;
import org.apache.camel.impl.DefaultEndpoint;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.MembershipEvent;
import com.hazelcast.core.MembershipListener;

public class HazelcastInstanceConsumer extends DefaultConsumer {

	public HazelcastInstanceConsumer(DefaultEndpoint endpoint, Processor processor) {
		super(endpoint, processor);
		
		Hazelcast.getCluster().addMembershipListener(new HazelcastMembershipListener());
	}

	class HazelcastMembershipListener implements MembershipListener{

		public void memberAdded(MembershipEvent event) {
			this.sendExchange(event, HazelcastConstants.ADDED);
		}

		public void memberRemoved(MembershipEvent event) {
			this.sendExchange(event, HazelcastConstants.REMOVED);
		}
		
		private void sendExchange(MembershipEvent event, String action){
			Exchange exchange = getEndpoint().createExchange();
			
			HazelcastComponentHelper.setListenerHeaders(
					exchange, 
					HazelcastConstants.INSTANCE_LISTENER, 
					action);
			
			//instance listener header values
			exchange.getOut().setHeader(HazelcastConstants.INSTANCE_HOST, event.getMember().getInetSocketAddress().getHostName());
			exchange.getOut().setHeader(HazelcastConstants.INSTANCE_PORT, event.getMember().getInetSocketAddress().getPort());
			
			try {
				getProcessor().process(exchange);
			} catch (Exception e) {
				if (exchange.getException() != null) {
					getExceptionHandler().handleException(
							"Error processing exchange for hazelcast consumer on your Hazelcast cluster.", exchange,
					exchange.getException());
				}
			}
		}
		
	}

}
