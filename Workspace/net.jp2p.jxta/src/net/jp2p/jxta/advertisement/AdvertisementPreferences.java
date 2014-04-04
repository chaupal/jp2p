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
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementDirectives;
import net.jp2p.jxta.advertisement.AdvertisementPropertySource.AdvertisementTypes;
import net.jp2p.jxta.advertisement.ModuleClassAdvertisementPropertySource.ModuleClassProperties;
import net.jp2p.jxta.advertisement.ModuleSpecAdvertisementPropertySource.ModuleSpecProperties;
import net.jp2p.jxta.peergroup.PeerGroupPropertySource.PeerGroupProperties;
import net.jp2p.jxta.pipe.PipePropertySource.PipeServiceProperties;
import net.jxta.id.ID;
import net.jxta.id.IDFactory;

public class AdvertisementPreferences extends AbstractPreferences<String, Object> implements IPropertyConvertor<String, Object>{

	public AdvertisementPreferences( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		Object value = super.getSource().getProperty(id);
		if( value != null )
			return value.toString();
		return null;
	}

	@Override
	public Object convertTo(IJp2pProperties id, String value) {
		if(!( isID( super.getSource(), id )))
			return value;
		try {
			return getCorrectID( super.getSource() );
		} catch (URISyntaxException e) {
			e.printStackTrace();
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
		
		return null;
	}
	
	/**
	 * Get the correct id, based on the relevant id property. returns null if nothing was entered 
	 * @param source
	 * @return
	 * @throws URISyntaxException
	 */
	public static ID getCorrectID( IJp2pPropertySource<IJp2pProperties> source ) throws URISyntaxException{
		AdvertisementTypes type = AdvertisementTypes.valueOf( StringStyler.styleToEnum( (String) source.getDirective( AdvertisementDirectives.TYPE )));
		String str = null;
		switch( type ){
		case MODULE_CLASS:
			str = (String) source.getProperty( ModuleClassProperties.MODULE_CLASS_ID );
			break;
		case MODULE_SPEC:
			str = (String) source.getProperty( ModuleSpecProperties.MODULE_SPEC_ID );
			break;
		case PEER:
			str = (String) source.getProperty( PeerGroupProperties.PEER_ID );
			break;
		case PEERGROUP:
			str = (String) source.getProperty( PeerGroupProperties.PEERGROUP_ID );
			break;
		case PIPE:
			str = (String) source.getProperty( PipeServiceProperties.PIPE_ID );
			break;
		default:
			break;
		}
		if( Utils.isNull(str))
			return null;
		return IDFactory.fromURI( new URI( str ));
	}

	/**
	 * Get the correct id, based on the relevant id property. returns null if nothing was entered 
	 * @param source
	 * @return
	 * @throws URISyntaxException
	 */
	public static boolean isID( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id ){
		AdvertisementTypes type = AdvertisementTypes.convertFrom( (String) source.getDirective( AdvertisementDirectives.TYPE ));
		switch( type ){
		case MODULE_CLASS:
			return ( ModuleClassProperties.MODULE_CLASS_ID.equals( id ));
		case MODULE_SPEC:
			return ( ModuleSpecProperties.MODULE_SPEC_ID.equals( id ) );
		case PEER:
			return ( PeerGroupProperties.PEER_ID.equals( id ) );
		case PEERGROUP:
			return ( PeerGroupProperties.PEERGROUP_ID.equals( id ) );
		case PIPE:
			return ( PipeServiceProperties.PIPE_ID.equals( id ) );
		default:
			break;
		}
		return false;
	}
}