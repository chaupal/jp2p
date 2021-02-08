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
package net.jp2p.chaupal.jxta.platform;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.chaupal.jxse.core.id.Jp2pIDFactory;
import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource.NetworkManagerProperties;
import net.jp2p.chaupal.peer.IJp2pPeerID;
import net.jp2p.chaupal.platform.INetworkManager;
import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.AbstractJp2pPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroupID;

public class NetworkManagerPreferences extends AbstractPreferences<IJp2pProperties, String, Object>{
	public NetworkManagerPreferences( NetworkManagerPropertySource source )
	{
		super( source );
	}

	@Override
	public NetworkManagerProperties getIdFromString(String key) {
		return NetworkManagerProperties.valueOf( key );
	}


	/* (non-Javadoc)
	 * @see net.osgi.jxta.preferences.IJxtaPreferences#getConfigMode()
	 */
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getConfigMode()
	 */
	public INetworkManager.ConfigModes getConfigMode( ){
		IJp2pPropertySource<IJp2pProperties> source = super.getSource();
		return ( INetworkManager.ConfigModes ) source.getProperty( NetworkManagerProperties.CONFIG_MODE );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setConfigMode(net.jxta.platform.NetworkManager.ConfigMode)
	 */
	public void setConfigMode( INetworkManager.ConfigModes mode ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( NetworkManagerProperties.CONFIG_MODE, mode );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setConfigMode(java.lang.String)
	 */
	public INetworkManager.ConfigModes setConfigMode( String mstr ){
		INetworkManager.ConfigModes mode = INetworkManager.ConfigModes.valueOf(mstr );
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
	public IJp2pPeerID getPeerID() throws URISyntaxException{
		NetworkManagerPropertySource source = (NetworkManagerPropertySource) super.getSource();
		String name = AbstractJp2pPropertySource.getIdentifier( source );
		ID pgId = IDFactory.newPeerID( PeerGroupID.defaultNetPeerGroupID, name.getBytes() );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( NetworkManagerProperties.PEER_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (IJp2pPeerID) Jp2pIDFactory.create( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPeerID( IJp2pPeerID peerID ){
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
				return Jp2pIDFactory.create( uri );
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
