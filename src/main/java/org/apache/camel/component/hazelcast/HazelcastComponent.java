package org.apache.camel.component.hazelcast;

import java.util.Map;

import org.apache.camel.Endpoint;
import org.apache.camel.component.hazelcast.atomicnumber.HazelcastAtomicnumberEndpoint;
import org.apache.camel.component.hazelcast.instance.HazelcastInstanceEndpoint;
import org.apache.camel.component.hazelcast.map.HazelcastMapEndpoint;
import org.apache.camel.component.hazelcast.multimap.HazelcastMultimapEndpoint;
import org.apache.camel.impl.DefaultComponent;

import static org.apache.camel.util.ObjectHelper.removeStartingCharacters;

public class HazelcastComponent extends DefaultComponent {
	

	@Override
	protected Endpoint createEndpoint(String uri, String remaining,
			Map<String, Object> parameters) throws Exception {
		
		Endpoint endpoint = null;
		
		//check type of endpoint
		if(remaining.startsWith(HazelcastConstants.MAP_PREFIX)){
			//remaining is the cache name
			remaining = removeStartingCharacters(remaining.substring(HazelcastConstants.MAP_PREFIX.length()), '/');
			endpoint = new HazelcastMapEndpoint(uri, remaining, this);
		}
		
		if(remaining.startsWith(HazelcastConstants.MULTIMAP_PREFIX)){
			//remaining is the cache name
			remaining = removeStartingCharacters(remaining.substring(HazelcastConstants.MULTIMAP_PREFIX.length()), '/');
			endpoint = new HazelcastMultimapEndpoint(uri, remaining, this);
		}
		
		if(remaining.startsWith(HazelcastConstants.ATOMICNUMBER_PREFIX)){
			//remaining is the name of the atomic value
			remaining = removeStartingCharacters(remaining.substring(HazelcastConstants.INSTANCE_PREFIX.length()), '/');
			endpoint = new HazelcastAtomicnumberEndpoint(uri, this, remaining);
		}
		
		if(remaining.startsWith(HazelcastConstants.INSTANCE_PREFIX)){
			//remaining is anything (name it foo ;)
			remaining = removeStartingCharacters(remaining.substring(HazelcastConstants.INSTANCE_PREFIX.length()), '/');
			endpoint = new HazelcastInstanceEndpoint(uri,this);
		}
		
		if(endpoint == null){
			throw new IllegalArgumentException(
					String.format("Your URI does not provide a correct 'type' prefix. It should be something like 'hazelcast:[%s|%s|%s|%s]name' but is '%s'.",
							HazelcastConstants.MAP_PREFIX,
							HazelcastConstants.MULTIMAP_PREFIX,
							HazelcastConstants.ATOMICNUMBER_PREFIX,
							HazelcastConstants.INSTANCE_PREFIX,
							uri));
		}
		
		return endpoint;
	}

}
