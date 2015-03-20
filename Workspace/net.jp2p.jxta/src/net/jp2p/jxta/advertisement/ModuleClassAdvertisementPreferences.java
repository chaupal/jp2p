/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.advertisement;

import java.net.URI;
import java.net.URISyntaxException;

import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringProperty;
import net.jp2p.jxta.advertisement.ModuleClassAdvertisementPropertySource.ModuleClassProperties;
import net.jxta.id.IDFactory;
import net.jxta.peergroup.core.ModuleClassID;

public class ModuleClassAdvertisementPreferences extends AbstractPreferences<IJp2pProperties, String, Object>{

	public ModuleClassAdvertisementPreferences( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		if( ModuleClassProperties.isValidProperty(key))
			return ModuleClassProperties.valueOf(key);
		return new StringProperty( key );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkPreferences#setPropertyFromString(net.osgi.jxse.network.NetworkConfigurationPropertySource.NetworkConfiguratorProperties, java.lang.String)
	 */
	@Override
	public Object convertTo( IJp2pProperties id, String value ){
		Object retval = this.convertPipeValue(id, value);
		if( retval != null )
			return retval;
		if( !( id instanceof ModuleClassProperties ))
			return null;
		ModuleClassProperties pid = ( ModuleClassProperties )id;
		switch( pid ){
		case MODULE_CLASS_ID:
			URI uri = URI.create(value);
			try {
				return IDFactory.fromURI( uri );
			} catch (URISyntaxException e) {
				throw new RuntimeException( e );
			}
		default:
			break;
		}
		return null;
	}	

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkPreferences#setPropertyFromString(net.osgi.jxse.network.NetworkConfigurationPropertySource.NetworkConfiguratorProperties, java.lang.String)
	 */
	protected Object convertPipeValue( IJp2pProperties id, String value ){
		if( !( id instanceof ModuleClassProperties ))
			return null;
		ModuleClassProperties msid = ( ModuleClassProperties )id;
		switch( msid ){
		case MODULE_CLASS_ID:
			URI uri = URI.create(value);
			try {
				return IDFactory.fromURI( uri );
			} catch (URISyntaxException e) {
				throw new RuntimeException( e );
			}
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
		if( ManagedProperty.isCreated( super.getSource().getManagedProperty(id)))
			return null;
		
		if( !( id instanceof ModuleClassProperties ))
			return null;
		ModuleClassProperties pid = ( ModuleClassProperties )id;
		switch( pid ){
		case MODULE_CLASS_ID:
			return IDFactory.newModuleClassID();
		default:
			break;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#getPeerID()
	 */
	public ModuleClassID getModuleClassID() throws URISyntaxException{
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		ModuleClassID pgId = (ModuleClassID) createDefaultValue( ModuleClassProperties.MODULE_CLASS_ID );
		ManagedProperty<IJp2pProperties, Object> property = source.getOrCreateManagedProperty( ModuleClassProperties.MODULE_CLASS_ID, pgId.toString(), false );
		String str = (String) property.getValue();
		URI uri = new URI( str );
		return (ModuleClassID) IDFactory.fromURI( uri );
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkManagerPropertySource#setPeerID(net.jxta.peer.PeerID)
	 */
	public void setPipeID( ModuleClassID mcid ){
		IJp2pWritePropertySource<IJp2pProperties> source = (IJp2pWritePropertySource<IJp2pProperties>) super.getSource();
		source.setProperty( ModuleClassProperties.MODULE_CLASS_ID, mcid.toString() );
	}
	
}
