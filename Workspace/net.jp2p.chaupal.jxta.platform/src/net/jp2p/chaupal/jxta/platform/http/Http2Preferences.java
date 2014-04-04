/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.http;

import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.INetworkPreferences;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.container.partial.PartialPropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class Http2Preferences implements INetworkPreferences {

	public static final String S_HTTP2_CONFIGURATION = "http Configuration";
	
	private PartialPropertySource source;
	
	public Http2Preferences( PartialPropertySource source ) {
		this.source = source;
	}

	public int getStartPort(){
		return ( int )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8START_PORT);
	}

	public void setStartPort( int port ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8START_PORT, port );
	}

	public int getPort(){
		return ( int )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8PORT);
	}

	public void setPort( int port ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8PORT, port );
	}

	public int getEndPort(){
		return ( int )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8END_PORT);
	}

	public void setEndPort( int port ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8END_PORT, port );
	}

	public boolean getIncomingStatus(){
		return (boolean )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8INCOMING_STATUS );
	}

	public void setIncomingStatus( boolean enabled ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8INCOMING_STATUS, enabled );
	}

	public String getInterfaceAddress(){
		return ( String )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8INTERFACE_ADDRESS );
	}

	public void setInterfaceAddress( String address ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8INTERFACE_ADDRESS, address );
	}

	public boolean getOutgoingStatus(){
		return ( boolean )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8OUTGOING_STATUS );
	}

	public void setOutgoingStatus( boolean enabled ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8OUTGOING_STATUS, enabled );
	}

	public boolean getPublicAddressExclusive(){
		return ( boolean )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8PUBLIC_ADDRESS_EXCLUSIVE );
	}

	public void setPublicAddressExclusive( boolean exclusive ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8PUBLIC_ADDRESS_EXCLUSIVE, exclusive );
	}

	public String getPublicAddress(){
		return ( String )this.source.getProperty( NetworkConfiguratorProperties.HTTP2_8PUBLIC_ADDRESS );
	}

	public void setPublicAddress( String address ){
		this.source.setProperty( NetworkConfiguratorProperties.HTTP2_8PUBLIC_ADDRESS, address );
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
		case HTTP2_8ENABLED:
			configurator.setHttp2Enabled((boolean) value );
			break;
		case HTTP2_8INCOMING_STATUS:
			configurator.setHttp2Incoming((boolean) value );
			break;
		case HTTP2_8OUTGOING_STATUS:
			configurator.setHttp2Outgoing((boolean)value );
			break;
		case HTTP2_8PUBLIC_ADDRESS_EXCLUSIVE:
			configurator.setHttp2PublicAddress(( String )value, true );
			break;
		case HTTP2_8END_PORT:
			configurator.setHttp2EndPort(( Integer ) value );
			break;
		case HTTP2_8PORT:
			configurator.setHttp2Port(( Integer ) value );
			break;
		case HTTP2_8START_PORT:
			configurator.setHttp2StartPort(( Integer )value );
			break;
		case HTTP2_8INTERFACE_ADDRESS:
			configurator.setHttp2InterfaceAddress(( String )value );
			break;
		case HTTP2_8PUBLIC_ADDRESS:
			configurator.setHttp2PublicAddress(( String) value, false );
			break;			
		default:
			retval = false;
			break;
		}	
		return retval;
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
		case HTTP2_8INTERFACE_ADDRESS:
		case HTTP2_8PUBLIC_ADDRESS:
		case HTTP2_8PUBLIC_ADDRESS_EXCLUSIVE:
			return value;
		case HTTP2_8ENABLED:
		case HTTP2_8INCOMING_STATUS:
		case HTTP2_8OUTGOING_STATUS:
			return Boolean.parseBoolean( value );
		case HTTP2_8END_PORT:
		case HTTP2_8PORT:
		case HTTP2_8START_PORT:
			return Integer.parseInt( value );
		default:
			return null;
		}	
	}
}
