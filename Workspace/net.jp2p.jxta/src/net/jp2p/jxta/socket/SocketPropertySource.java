/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;

public class SocketPropertySource extends AdvertisementPropertySource{

	public static final int DEFAULT_SOCKET_TIME_OUT = 30000;
	public static final int DEFAULT_BACK_LOG = 20;
	
	/**
	 * Properties specific for pipe services
	 * @author Kees
	 *
	 */
	public enum SocketProperties implements IJp2pProperties{
		BACKLOG,
		TIME_OUT,
		TYPE;
	
		public static boolean isValidProperty( String str ){
			if( Utils.isNull( str ))
				return false;
			for( SocketProperties dir: values() ){
				if( dir.name().equals( str ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public enum SocketTypes{
		CLIENT,
		SERVER,
		MULTICAST;
		
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}	
	}

	public SocketPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( JxtaComponents.JXSE_SOCKET_SERVICE.toString(), parent);
		super.setProperty( SocketProperties.TIME_OUT, DEFAULT_SOCKET_TIME_OUT );
		super.setProperty( SocketProperties.BACKLOG, DEFAULT_BACK_LOG );
		super.setDirective( Directives.CREATE, Boolean.TRUE.toString() );
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( PeerGroupPropertySource.PeerGroupDirectives.isValidDirective( id.name()))
			return super.setDirective(PeerGroupPropertySource.PeerGroupDirectives.valueOf( id.name()), value );
		return super.setDirective(id, value);
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		if( SocketProperties.isValidProperty(key))
			return SocketProperties.valueOf(key);
		return super.getIdFromString(key);
	}

	@Override
	public boolean validate(IJp2pProperties id, Object value) {
		return SocketProperties.isValidProperty(id.toString());	
	}	
}