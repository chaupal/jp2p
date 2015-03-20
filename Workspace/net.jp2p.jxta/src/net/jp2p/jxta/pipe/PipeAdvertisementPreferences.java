/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.pipe;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.jxta.pipe.PipePropertySource;
import net.jp2p.jxta.pipe.PipePropertySource.PipeServiceProperties;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.PipeID;

public class PipeAdvertisementPreferences extends AbstractPreferences<IJp2pProperties, String, Object>{

	private PeerGroup peergroup;
	
	public PipeAdvertisementPreferences( IJp2pWritePropertySource<IJp2pProperties> source, PeerGroup peergroup ) {
		super( source );
		this.peergroup = peergroup;
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
	public Object convertTo( IJp2pProperties id, String value ){
		if( !( id instanceof PipeServiceProperties ))
			return null;
		PipeServiceProperties pid = ( PipeServiceProperties )id;
		switch( pid ){
		case PIPE_ID:
			return PipeID.create( URI.create(value));
		case TYPE:
			return PipePropertySource.PipeServiceTypes.valueOf( value );
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
	public Object createDefaultValue( IJp2pProperties id ){
		if( !ManagedProperty.isCreated( super.getSource().getManagedProperty(id)))
			return super.getSource().getProperty(id);
		
		if( !( id instanceof PipeServiceProperties ))
			return null;
		PipeServiceProperties pid = ( PipeServiceProperties )id;
		switch( pid ){
		case PIPE_ID:
			return IDFactory.newPipeID( peergroup.getPeerGroupID() );
		default:
			break;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getPeerID()
	 */
	public PipeID getPipeID() throws URISyntaxException{
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		PipeID pgId = (PipeID) createDefaultValue( PipeServiceProperties.PIPE_ID );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( PipeServiceProperties.PIPE_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (PipeID) IDFactory.fromURI( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPipeID( PipeID pipeID ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( PipeServiceProperties.PIPE_ID, pipeID.toString() );
	}
	
}
