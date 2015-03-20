/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.discovery;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.discovery.DiscoveryPropertySource.DiscoveryProperties;
import net.jxta.peer.PeerID;

public class DiscoveryPreferences extends AbstractPreferences<IJp2pProperties, String, Object>
{
	public DiscoveryPreferences( IJp2pWritePropertySource<IJp2pProperties> source )
	{
		super( source );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		Object value = super.getSource().getProperty(id);
		if( value != null )
			return value.toString();
		return null;
	}

	/**
	 * Get the correct property from the given string
	 * @param property
	 * @param value
	 * @return
	 * @throws URISyntaxException
	 */
	@Override
	public Object convertTo( IJp2pProperties key, String value ){
		if(!( key instanceof DiscoveryProperties ))
			return null;
		DiscoveryProperties id = (DiscoveryProperties) key;
		switch( id ){
		case ADVERTISEMENT_TYPE:
			return AdvertisementTypes.valueOf( StringStyler.styleToEnum(value));
		case ATTRIBUTE:
		case WILDCARD:
			return value;
		case COUNT:
		case THRESHOLD:
		case WAIT_TIME:
			return Integer.valueOf( value);
		case PEER_ID:
			return PeerID.create( URI.create( value ));
		default:
			return null;
		}
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		return new StringProperty( key );
	}
}
