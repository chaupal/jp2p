/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.http;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class HttpConfiguration {

	public static final String S_HTTP_CONFIGURATION = "Http Configuration";

	private NetworkConfigurator configurator;
	private boolean publicAddressExclusive = false;
	
	public HttpConfiguration( NetworkConfigurator configurator ) {
		this.configurator = configurator;
	}

	public int getStartPort(){
		return this.configurator.getHttp2StartPort();
	}

	public void setStartPort( int port ){
		this.configurator.setHttp2StartPort( port );
	}

	public int getPort(){
		return this.configurator.getHttpPort();
	}

	public void setPort( int port ){
		this.configurator.setHttp2Port( port );
	}

	public int getEndPort(){
		return this.configurator.getHttp2EndPort();
	}

	public void setEndPort( int port ){
		this.configurator.setHttp2EndPort( port );
	}

	public boolean getIncomingStatus(){
		return this.configurator.getHttp2IncomingStatus();
	}

	public void setIncomingStatus( boolean enabled ){
		this.configurator.setHttp2Incoming( enabled );
	}

	public String getInterfaceAddress(){
		return this.configurator.getHttp2InterfaceAddress();
	}

	public void setInterfaceAddress( String address ){
		this.configurator.setHttp2InterfaceAddress(address);
	}

	public boolean getHttpOutgoingStatus(){
		return this.configurator.getHttp2OutgoingStatus();
	}

	public void setOutgoingStatus( boolean enabled ){
		this.configurator.setHttp2Outgoing( enabled );
	}

	public boolean getPublicAddressExclusive(){
		return this.publicAddressExclusive;
	}

	public void setPublicAddressExclusive( boolean exclusive ){
		this.publicAddressExclusive =  exclusive;
	}

	public String getPublicAddress(){
		return this.configurator.getHttp2PublicAddress();
	}

	public void setHttpPublicAddress( String address ){
		this.configurator.setHttp2PublicAddress(address, this.publicAddressExclusive);
	}

	/**
	 * Create the correct type for the given property
	 * @param factory
	 * @param property
	 * @param value
	 */
	public static boolean addStringProperty( NetworkConfigurationFactory factory, NetworkConfiguratorProperties property, String value ){
		boolean retval = false;
		NetworkConfigurationPropertySource source = (NetworkConfigurationPropertySource) factory.getPropertySource();
		switch( property ){
		case HTTP_8ENABLED:
		case HTTP_8INCOMING_STATUS:
		case HTTP_8OUTGOING_STATUS:
		case HTTP_8TO_PUBLIC_ADDRESS_EXCLUSIVE:
			source.setProperty( property, Boolean.parseBoolean( value ));
			retval = true;
			break;
		case HTTP_8PORT:
			source.setProperty( property, Integer.parseInt( value ));
			retval = true;
			break;
		case HTTP_8PUBLIC_ADDRESS_EXCLUSIVE:
		case HTTP_8PUBLIC_ADDRESS:
		case HTTP_8INTERFACE_ADDRESS:	
			source.setProperty( property, value );
			retval = true;
			break;
		default:
			break;
		}
		return retval;
	}	
}
