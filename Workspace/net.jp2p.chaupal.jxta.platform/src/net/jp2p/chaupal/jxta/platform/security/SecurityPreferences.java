/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.security;

import java.net.URI;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.INetworkPreferences;
import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.jxta.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jxta.platform.NetworkConfigurator;

public class SecurityPreferences implements INetworkPreferences{

	public static final String S_SCURITY_CONFIGURATION = "Security Configuration";
	
	private PartialPropertySource source;
	
	public SecurityPreferences( PartialPropertySource source ) {
		this.source = source;
	}
	
	@Override
	public String convertFrom(IJp2pProperties id) {
		return null;
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkPreferences#setPropertyFromString(net.osgi.jxse.network.NetworkConfigurationPropertySource.NetworkConfiguratorProperties, java.lang.String)
	 */
	@Override
	public Object convertTo( IJp2pProperties id, String value ){
		return convertStringToCorrectType( id, value);
	}

	@Override
	public boolean setPropertyFromConverion(IJp2pProperties id, String value) {
		Object val = convertStringToCorrectType( id, value);
		source.setProperty( id, val );
		return ( val != null );
	}

	/**
	 * Fill the configurator with the given properties from a key string
	 * @param configurator
	 * @param property
	 * @param value
	*/
	@Override
	public boolean fillConfigurator( NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		boolean retval = true;
		while( iterator.hasNext() ){
			NetworkConfiguratorProperties id = (NetworkConfiguratorProperties) iterator.next();
			retval &= fillConfigurator(configurator, id, source.getProperty(id));
		}
		return retval;
	}
	
	/**
	 * Fill the configurator with the given properties
	 * @param configurator
	 * @param property
	 * @param value
	 */
	public static boolean fillConfigurator( NetworkConfigurator configurator, NetworkConfiguratorProperties property, Object value ){
		boolean retval = true;
		switch( property ){
		case SECURITY_8AUTHENTICATION_TYPE:
			configurator.setAuthenticationType((String) value );
			break;
		case SECURITY_8CERTFICATE:
			configurator.setCertificate(( X509Certificate ) value );
			break;
		case SECURITY_8CERTIFICATE_CHAIN:
			configurator.setCertificateChain(( X509Certificate[] )value );
			break;
		case SECURITY_8KEY_STORE_LOCATION:
			configurator.setKeyStoreLocation(( URI )value );
			break;
		case SECURITY_8PASSWORD:
			configurator.setPassword(( String ) value );
			break;
		case SECURITY_8PRINCIPAL:
			configurator.setPrincipal(( String ) value );
			break;
		case SECURITY_8PRIVATE_KEY:
			configurator.setPrivateKey(( PrivateKey )value );
			break;
		default:
			retval = false;
			break;
		}	
		return retval;
	}

	/**
	 * Convert a given string value to the correct type
	 * @param source
	 * @param property
	 * @param value
	 */
	public static Object convertStringToCorrectType( IJp2pProperties property, String value ){
		if(!(property instanceof NetworkConfiguratorProperties ))
			return null;
		NetworkConfiguratorProperties id = (NetworkConfiguratorProperties) property;
		switch( id ){
		case SECURITY_8AUTHENTICATION_TYPE:
		case SECURITY_8PASSWORD:
		case SECURITY_8PRINCIPAL:
			return value;
		case SECURITY_8PRIVATE_KEY:
			return value;
		case SECURITY_8CERTFICATE:
			return value;
		case SECURITY_8CERTIFICATE_CHAIN:
			return value;
		case SECURITY_8KEY_STORE_LOCATION:
			return URI.create(value);
		default:
			return null;
		}	
	}
}