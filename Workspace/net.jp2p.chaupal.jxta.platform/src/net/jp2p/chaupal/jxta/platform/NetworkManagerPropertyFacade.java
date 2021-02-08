package net.jp2p.chaupal.jxta.platform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.chaupal.platform.INetworkManager;
import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;

public class NetworkManagerPropertyFacade extends AbstractPropertyFacade<INetworkManager> {

	public NetworkManagerPropertyFacade( String bundleId, INetworkManager module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		INetworkManager manager = super.getModule();
		if(!( id instanceof NetworkManagerProperties ))
			return null;
		NetworkManagerProperties property  = ( NetworkManagerProperties )id;
		switch( property ){
		case CONFIG_PERSISTENT:
			return manager.isConfigPersistent();
		case INFRASTRUCTURE_ID:
			return manager.getInfrastructureID();
		case INSTANCE_HOME:
			return manager.getInstanceHome();
		case INSTANCE_NAME:
			return manager.getInstanceName();
		case CONFIG_MODE:
			return manager.getMode().ordinal();
		case PEER_ID:
			return manager.getPeerID();
		}
		return null;
	}

	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Collection<IJp2pProperties> results = new ArrayList<IJp2pProperties>();
		for( NetworkManagerProperties nmp: NetworkManagerProperties.values())
			results.add( nmp );
		return results.iterator();
	}
}
