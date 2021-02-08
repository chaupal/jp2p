package net.jp2p.chaupal.jxta.platform.configurator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.chaupal.platform.INetworkConfigurator;
import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;

public class NetworkConfiguratorPropertyFacade extends AbstractPropertyFacade<INetworkConfigurator> {

	public NetworkConfiguratorPropertyFacade( String bundleId, INetworkConfigurator module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		INetworkConfigurator configurator = super.getModule();
		if(!( id instanceof NetworkConfiguratorProperties ))
			return null;
		NetworkConfiguratorProperties property = ( NetworkConfiguratorProperties )id;
		switch( property ){
		case HOME:
			return configurator.getHome();
		case CONFIG_MODE:
			return configurator.getMode();
		case NAME:
			return configurator.getName();
		case PEER_ID:
			return configurator.getPeerID();
		case STORE_HOME:
			return configurator.getStoreHome();
		default:
			break;
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
