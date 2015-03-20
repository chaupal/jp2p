package net.jp2p.chaupal.jxta.platform.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.jp2p.container.properties.AbstractPropertyFacade;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.jxta.transport.TransportPropertySource.TransportProperties;
import net.jxta.platform.NetworkConfigurator;

public class HttpPropertyFacade extends AbstractPropertyFacade<NetworkConfigurator> {

	public HttpPropertyFacade( String bundleId, NetworkConfigurator module) {
		super( bundleId, module);
	}

	@Override
	public Object getProperty( IJp2pProperties id) {
		NetworkConfigurator configurator = super.getModule();
		if(!( id instanceof TransportProperties ))
			return null;
		TransportProperties property = ( TransportProperties )id;
		switch( property ){
		case PUBLIC_ADDRESS:
			return configurator.getHttpPublicAddress();
		case PUBLIC_ADDRESS_EXCLUSIVE:
			return configurator.isHttpPublicAddressExclusive();
		case INCOMING_STATUS:
			return configurator.getHttpIncomingStatus();
		case INTERFACE_ADDRESS:
			return configurator.getHttpInterfaceAddress();
		case OUTGOING_STATUS:
			return configurator.getHttpOutgoingStatus();
		case PORT:
			return configurator.getHttp2Port();
		default:
			break;
		}
		return null;
	}

	@Override
	public Iterator<IJp2pProperties> propertyIterator() {
		Collection<IJp2pProperties> results = new ArrayList<IJp2pProperties>();
		for( TransportProperties nmp: TransportProperties.values())
			results.add( nmp );
		return results.iterator();
	}
}
