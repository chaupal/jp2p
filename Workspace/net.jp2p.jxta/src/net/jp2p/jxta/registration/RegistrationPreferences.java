/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.registration;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.discovery.DiscoveryPropertySource.DiscoveryMode;
import net.jp2p.jxta.registration.RegistrationPropertySource.RegistrationProperties;
import net.jxta.peer.PeerID;

public class RegistrationPreferences extends AbstractPreferences<String,Object>
{
	public RegistrationPreferences( IJp2pWritePropertySource<IJp2pProperties> source )
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
	public Object convertTo( IJp2pProperties id, String value ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		if(!( id instanceof IJp2pProperties ))
			return null;
		RegistrationProperties key = (RegistrationProperties) id;
		switch( key ){
		case ATTRIBUTE:
		case WILDCARD:
			return source.setProperty(id, value);
		case THRESHOLD:
		case WAIT_TIME:
			return source.setProperty(id, Integer.valueOf( value));
		case DISCOVERY_MODE:
			return source.setProperty(id, DiscoveryMode.valueOf( value ));
		case PEER_ID:
			return source.setProperty(id, PeerID.create( URI.create( value )));
		default:
			return false;
		}
	}
}
