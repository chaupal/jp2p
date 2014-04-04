/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.tcp;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jxta.refplatform.platform.NetworkConfigurator;

public class TcpConfiguration {

	public static final String S_TCP_CONFIGURATION = "Tcp Configuration";
	
	private NetworkConfigurator configurator;
	private boolean publicAddressExclusive = false;
	
	public TcpConfiguration( NetworkConfigurator configurator ) {
		this.configurator = configurator;
	}

	public int getStartPort(){
		return this.configurator.getTcpStartPort();
	}

	public void setStartPort( int port ){
		this.configurator.setTcpStartPort( port );
	}

	public int getPort(){
		return this.configurator.getTcpPort();
	}

	public void setPort( int port ){
		this.configurator.setTcpPort( port );
	}

	public int getEndPort(){
		return this.configurator.getTcpEndport();
	}

	public void setEndPort( int port ){
		this.configurator.setTcpEndPort( port );
	}

	public boolean getIncomingStatus(){
		return this.configurator.getTcpIncomingStatus();
	}

	public void setIncomingStatus( boolean enabled ){
		this.configurator.setTcpIncoming( enabled );
	}

	public String getInterfaceAddress(){
		return this.configurator.getTcpInterfaceAddress();
	}

	public void setInterfaceAddress( String address ){
		this.configurator.setTcpInterfaceAddress(address);
	}

	public boolean getOutgoingStatus(){
		return this.configurator.getTcpOutgoingStatus();
	}

	public void setOutgoingStatus( boolean enabled ){
		this.configurator.setTcpOutgoing( enabled );
	}

	public boolean getPublicAddressExclusive(){
		return this.publicAddressExclusive;
	}

	public void setPublicAddressExclusive( boolean exclusive ){
		this.publicAddressExclusive =  exclusive;
	}

	public String getPublicAddress(){
		return this.configurator.getTcpPublicAddress();
	}

	public void setPublicAddress( String address ){
		this.configurator.setTcpPublicAddress(address, this.publicAddressExclusive);
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
		case TCP_8ENABLED:
		case TCP_8INCOMING_STATUS:
		case TCP_8OUTGOING_STATUS:
		case TCP_8PUBLIC_ADDRESS_EXCLUSIVE:
			source.setProperty( property, Boolean.parseBoolean( value ));
			retval = true;
			break;
		case TCP_8END_PORT:
		case TCP_8PORT:
		case TCP_8START_PORT:
			source.setProperty( property, Integer.parseInt( value ));
			retval = true;
			break;
		case TCP_8INTERFACE_ADDRESS:
			source.setProperty( property, value );
			retval = true;
			break;
		default:
			break;
		}	
		return retval;
	}	
}
