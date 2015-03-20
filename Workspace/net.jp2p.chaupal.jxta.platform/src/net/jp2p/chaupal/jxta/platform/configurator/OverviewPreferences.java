/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.INetworkPreferences;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public class OverviewPreferences extends AbstractPreferences<IJp2pProperties, String, Object> implements INetworkPreferences{

	public static final String S_OVERVIEW = "Overview";

	public OverviewPreferences( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		Object value = super.getSource().getProperty(id);
		if( value != null )
			return value.toString();
		return null;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkPreferences#setPropertyFromString(net.osgi.jxse.network.NetworkConfigurationPropertySource.NetworkConfiguratorProperties, java.lang.String)
	 */
	@Override
	public Object convertTo( IJp2pProperties key, String value ){
		if(!( key instanceof NetworkConfiguratorProperties ))
			return null;
		NetworkConfiguratorProperties id = (NetworkConfiguratorProperties) key;
		switch( id ){
		case CONFIG_MODE:
			return NetworkManager.ConfigMode.valueOf( value );
		case STORE_HOME:
			return  URI.create(value);
		case PEER_ID:
			URI uri = URI.create(value);
			try {
				return IDFactory.fromURI( uri );
			} catch (URISyntaxException e) {
				throw new RuntimeException( e );
			}
		case DESCRIPTION:
		case HOME:
		case NAME:
			return value;
		default:
			break;
		}
		return null;
	}	

	/**
	 * Create a default value if this is requested as attribute and adds it to the source if it is not present 
	 * @param id
	 * @return
	 */
	@Override
	public Object createDefaultValue( IJp2pProperties key ){
		if( !ManagedProperty.isCreated( super.getSource().getManagedProperty(key)))
			return null;
		
		if(!( key instanceof NetworkConfiguratorProperties ))
			return null;
		NetworkConfiguratorProperties id = (NetworkConfiguratorProperties) key;
		Object value = null;
		switch( id ){
		case PEER_ID:
			PeerGroupPropertySource source = (PeerGroupPropertySource) super.getSource();
			String name = AbstractJp2pPropertySource.getIdentifier( source);
			value = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
			break;
		default:
			break;
		}
		if( value != null )
			((AbstractJp2pWritePropertySource) super.getSource()).getOrCreateManagedProperty(id, value, false );
		return null;
	}

	/**
	 * Fill the configurator with the given properties from a key string
	 * @param configurator
	 * @param property
	 * @param value
	*/
	@Override
	public boolean fillConfigurator( NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = super.getSource().propertyIterator();
		boolean retval = true;
		while( iterator.hasNext() ){
			IJp2pProperties id = iterator.next();
			retval &= fillConfigurator(configurator, id, super.getSource().getManagedProperty(id));
		}
		return retval;
	}

	/**
	 * Fill the configurator with the given properties
	 * @param configurator
	 * @param property
	 * @param value
	 */
	public static boolean fillConfigurator( NetworkConfigurator configurator, IJp2pProperties key, ManagedProperty<IJp2pProperties,Object> property ){
		boolean retval = true;
		Object value = property.getValue();
		if( value == null )
			return false;
		if(!( key instanceof NetworkConfiguratorProperties ))
			return false;
		NetworkConfiguratorProperties id = (NetworkConfiguratorProperties) key;		
		switch( id ){
		case CONFIG_MODE:
			configurator.setMode((( NetworkManager.ConfigMode )value).ordinal() );
			break;
		case PEER_ID:
			configurator.setPeerID(( PeerID ) value );
			break;
		case DESCRIPTION:
			configurator.setDescription(( String )value );
			break;
		case HOME:
			configurator.setHome(( File )value );
			break;
		case STORE_HOME:
			configurator.setStoreHome(( URI ) value );
			break;
		case NAME:
			configurator.setName(( String ) value );
			break;
		default:
			retval = false;
			break;
		}	
		return retval;
	}
}
