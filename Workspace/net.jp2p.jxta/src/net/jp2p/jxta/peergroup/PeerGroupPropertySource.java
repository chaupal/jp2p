/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;

public class PeerGroupPropertySource extends AdvertisementPropertySource
{
	public static final String S_NET_PEER_GROUP = "NetPeerGroup";

	public enum PeerGroupProperties implements IJp2pProperties{
		NAME,
		DESCRIPTION,
		GROUP_ID,
		PEER_ID,
		STORE_HOME,
		PEER_NAME,
		PEERGROUP_ID;

		/**
		 * Returns true if the given property is valid for this enumeration
		 * @param property
		 * @return
		 */
		public static boolean isValidProperty( IJp2pProperties property ){
			if( property == null )
				return false;
			for( PeerGroupProperties prop: values() ){
				if( prop.equals( property ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public enum PeerGroupDirectives implements IJp2pDirectives{
		TYPE,
		PEERGROUP,
		PUBLISH;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		public static boolean isValidDirective( String str ){
			if( Utils.isNull( str ))
				return false;
			for( PeerGroupDirectives dir: values() ){
				if( dir.name().equals( str ))
					return true;
			}
			return false;
		}

	}

	public PeerGroupPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		this( JxtaComponents.PEERGROUP_SERVICE.toString(), parent );
		this.fillDefaultValues();
	}

	public PeerGroupPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName,parent );
		this.fillDefaultValues();
	}

	protected void fillDefaultValues() {
		setDirectiveFromParent( Directives.AUTO_START, this );
		super.setDirective( Directives.CREATE, Boolean.TRUE.toString() );
		IJp2pWritePropertySource<IJp2pProperties> parent = (IJp2pWritePropertySource<IJp2pProperties>) super.getParent();
		this.setDirective( Directives.NAME, parent.getDirective( Directives.NAME ));
		String name = (String) super.getProperty( PeerGroupProperties.NAME );
		if( Utils.isNull( name ))
			name = (String) super.getDirective( IJp2pDirectives.Directives.NAME );
		if(!Utils.isNull( name ))
			super.setProperty( PeerGroupProperties.NAME, name );
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( PeerGroupDirectives.isValidDirective( id.name()))
			return super.setDirective(PeerGroupDirectives.valueOf( id.name()), value );
		return super.setDirective(id, value);
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	private static class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public PeerGroupProperties getIdFromString(String key) {
			return PeerGroupProperties.valueOf( key );
		}
	}
}