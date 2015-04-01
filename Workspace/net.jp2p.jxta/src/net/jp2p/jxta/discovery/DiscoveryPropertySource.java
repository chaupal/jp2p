/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.discovery;

import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementDirectives;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupDirectives;

public class DiscoveryPropertySource extends AbstractJp2pWritePropertySource
{
	public static final String S_NAME = "Name";
	public static final String S_WILDCARD = "*";
	
	public static final int DEFAULT_COUNT = 20;
	public static final int DEFAULT_WAIT_TIME = 10000;
	public static final int DEFAULT_THRESHOLD = 1;
	
	public enum DiscoveryMode{
		ONE_SHOT,
		CONTINUOUS,
		COUNT;

	@Override
	public String toString() {
		return StringStyler.prettyString( super.toString() );
	}}

	public enum DiscoveryProperties implements IJp2pProperties{
		MODE,
		WAIT_TIME,
		PEER_ID,
		ATTRIBUTE,
		WILDCARD,
		ADVERTISEMENT_TYPE,
		COUNT,
		COUNTER,
		FOUND,
		THRESHOLD;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public DiscoveryPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		this( JxtaComponents.DISCOVERY_SERVICE.toString(), parent );
	}


	public DiscoveryPropertySource( String componentName, IJp2pPropertySource<IJp2pProperties> parent) {
		super( componentName, parent );
		this.fillDefaultValues( parent );
		setDirectiveFromParent( PeerGroupDirectives.PEERGROUP, this );
	}

	protected void fillDefaultValues( IJp2pPropertySource<IJp2pProperties> parent) {
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.MODE, DiscoveryMode.COUNT, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.COUNT, DEFAULT_COUNT, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.WAIT_TIME, DEFAULT_WAIT_TIME, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.PEER_ID, null, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.ATTRIBUTE, S_NAME, false ));
		AdvertisementTypes type = AdvertisementTypes.convertFrom( parent.getDirective( AdvertisementDirectives.TYPE ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.ADVERTISEMENT_TYPE, type, false ));
		String wildcard = "*";
		switch( type ){
		case PEER:
		case PEERGROUP:
		case PIPE:
			String wc = parent.getDirective( Directives.NAME );
			if( !Utils.isNull( wc ))
				wildcard = wc;
		default:
			break;
		}
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.WILDCARD, wildcard, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.THRESHOLD, DEFAULT_THRESHOLD, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.COUNTER, 0, S_RUNTIME, false ));
		this.setManagedProperty( new ManagedProperty<IJp2pProperties, Object>( this, DiscoveryProperties.FOUND, 0, S_RUNTIME, false ));
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		if( AdvertisementPropertySource.AdvertisementDirectives.isValidDirective( id.name()))
			return super.setDirective(PeerGroupDirectives.valueOf( id.name() ), value );
		return super.setDirective(id, value);
	}

	@Override
	public IPropertyConvertor<IJp2pProperties, String, Object> getConvertor() {
		return new Convertor( this );
	}

	/**
	 * Converts properties to and from Strings
	 * @author Kees
	 *
	 */
	private class Convertor extends SimplePropertyConvertor{

		public Convertor(IJp2pPropertySource<IJp2pProperties> source) {
			super(source);
		}

		@Override
		public DiscoveryProperties getIdFromString(String key) {
			return DiscoveryProperties.valueOf( key );
		}

		@Override
		public String convertFrom(IJp2pProperties id) {
			DiscoveryProperties property = ( DiscoveryProperties )id;
			Object retval = getProperty( property );
			switch( property ){
			case WAIT_TIME:
			case COUNT:
			case COUNTER:
			case THRESHOLD:
			case FOUND:
				if( retval == null )
					return "0";
				return retval.toString();
			default:
				break;
			}
			return super.convertFrom(id);
		}

		@Override
		public Object convertTo(IJp2pProperties id, String value) {
			DiscoveryProperties property = ( DiscoveryProperties )id;
			switch( property ){
			case WAIT_TIME:
			case COUNT:
			case COUNTER:
			case THRESHOLD:
			case FOUND:
				if( Utils.isNull( value ))
					return Integer.valueOf( 0 );
				return Integer.valueOf( value );
			default:
				break;
			}
			return super.convertTo(id, value);
		}		
	}
	
}