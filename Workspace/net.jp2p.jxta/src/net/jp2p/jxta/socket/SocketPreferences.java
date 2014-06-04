/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.jxta.socket.SocketPropertySource.SocketProperties;

public class SocketPreferences extends AbstractPreferences<String, Object>
{
	public SocketPreferences( IJp2pWritePropertySource<IJp2pProperties> source )
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
		if( !( id instanceof SocketProperties ))
			return null;
		SocketProperties props = (SocketProperties) id;
		if( value == null )
			return null;
		switch( props ){
		case BACKLOG:
		case TIME_OUT:
			return Integer.parseInt( value );
		default:
			return value;
		}
	}
}