/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.root.network;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.chaupal.jxta.root.network.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jxta.id.IDFactory;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroupID;
import net.jxta.refplatform.platform.NetworkManager.ConfigMode;

public class NetworkManagerPreferences extends AbstractPreferences<String, Object>{
	public NetworkManagerPreferences( IJp2pWritePropertySource<IJp2pProperties> source )
	{
		super( source );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getConfigMode()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getConfigMode()
	 */
	public ConfigMode getConfigMode( ){
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return ( ConfigMode ) source.getProperty( NetworkManagerProperties.CONFIG_MODE );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setConfigMode(net.jxta.platform.NetworkManager.ConfigMode)
	 */
	public void setConfigMode( ConfigMode mode ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( NetworkManagerProperties.CONFIG_MODE, mode );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setConfigMode(java.lang.String)
	 */
	public ConfigMode setConfigMode( String mstr ){
		ConfigMode mode = ConfigMode.valueOf(mstr );
		this.setConfigMode(mode );
		return mode;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getHomeFolder()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getHomeFolder()
	 */
	public URI getHomeFolder( ) throws URISyntaxException{
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return (URI)source.getProperty( NetworkManagerProperties.INSTANCE_HOME );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setHomeFolder(java.net.URI)
	 */
	public void setHomeFolder( URI homeFolder ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( NetworkManagerProperties.INSTANCE_HOME, homeFolder );
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
		source.setProperty( NetworkManagerProperties.INSTANCE_HOME, folder );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getPeerID()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getPeerID()
	 */
	public PeerID getPeerID() throws URISyntaxException{
		NetworkManagerPropertySource source = (NetworkManagerPropertySource) super.getSource();
		String name = AbstractJp2pPropertySource.getIdentifier( source );
		PeerID pgId = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( NetworkManagerProperties.PEER_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (PeerID) IDFactory.fromURI( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPeerID( PeerID peerID ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( NetworkManagerProperties.PEER_ID, peerID.toString() );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getInstanceName()
	 */
	public String getInstanceName(){
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return (String) source.getProperty( NetworkManagerProperties.INSTANCE_NAME );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setInstanceName(java.lang.String)
	 */
	public void setInstanceName( String name ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( NetworkManagerProperties.INSTANCE_NAME, name );
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
		if( !( id instanceof NetworkManagerProperties ))
			return null;
		NetworkManagerProperties props = (NetworkManagerProperties) id;
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		if( value == null )
			return null;
		switch( props ){
		case CONFIG_PERSISTENT:
			return source.setProperty(id, Boolean.parseBoolean( value ));
		case INSTANCE_NAME:
		case INFRASTRUCTURE_ID:
			return value;
		case PEER_ID:
			URI uri = null;
			try {
				uri = new URI( value );
				return IDFactory.fromURI( uri );
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return uri;
		case INSTANCE_HOME:
			this.setHomeFolder(value);
			return true;
		case CONFIG_MODE:
			return this.setConfigMode( value );
		default:
			return false;
		}
	}
}
