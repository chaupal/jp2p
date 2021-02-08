/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.peergroup;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementProperties;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;

public class PeerGroupPreferences extends AbstractPreferences<IJp2pProperties, String, Object>
{
	public PeerGroupPreferences( IJp2pWritePropertySource<IJp2pProperties> source )
	{
		super( source );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getPeerID()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getPeerID()
	 */
	public PeerID getPeerID() throws URISyntaxException{
		PeerGroupPropertySource source = (PeerGroupPropertySource) super.getSource();
		String name = PeerGroupPropertySource.getIdentifier( source );
		PeerID pgId = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( PeerGroupProperties.PEER_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (PeerID) IDFactory.fromURI( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPeerID( PeerID peerID ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( PeerGroupProperties.PEER_ID, peerID.toString() );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getPeerID()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getPeerID()
	 */
	public PeerGroupID getPeerGroupID() throws URISyntaxException{
		PeerGroupAdvertisementPropertySource source = (PeerGroupAdvertisementPropertySource) super.getSource();
		String name = (String) source.getProperty( AdvertisementProperties.NAME );
		PeerGroupID pgId = IDFactory.newPeerGroupID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( PeerGroupProperties.PEERGROUP_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (PeerGroupID) IDFactory.fromURI( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPeerGroupID( PeerGroupID peerGroupID ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( PeerGroupProperties.PEERGROUP_ID, peerGroupID.toString() );
	}

	public URI getHomeFolder( ) throws URISyntaxException{
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return (URI)source.getProperty( PeerGroupProperties.STORE_HOME );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setHomeFolder(java.net.URI)
	 */
	public void setHomeFolder( URI homeFolder ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( PeerGroupProperties.STORE_HOME, homeFolder );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setHomeFolder(java.lang.String)
	 */
	public void setHomeFolder( String homeFolder ){
		String folder = homeFolder;
		String[] split = homeFolder.split("[$]");
		if( split.length > 1 ){
			  folder = System.getProperty( split[0] ) + split[1]; 
		}
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( PeerGroupProperties.STORE_HOME, folder );
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
		if( !( id instanceof PeerGroupProperties ))
			return null;
		PeerGroupProperties props = (PeerGroupProperties) id;
		if( value == null )
			return null;
		URI uri;
		switch( props ){
		case PEER_ID:
			try {
				uri = new URI( value );
				return (PeerID) IDFactory.fromURI( uri );
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			break;
		case STORE_HOME:
			this.setHomeFolder(value);
			return true;
		case GROUP_ID:
			try {
				return new URI( value );
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			break;			
		default:
			return value;
		}
		return null;
	}
}
