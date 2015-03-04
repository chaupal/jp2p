/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.rdvrel;

import java.util.List;
import java.util.Set;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.jxta.network.configurator.NetworkConfigurationPropertySource;
import net.jp2p.jxta.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jxta.platform.NetworkConfigurator;

public class RdvRelayConfiguration {

	public static final String S_RDV_RELAY_CONFIGURATION = "Rendezvous and Relay Configuration";

	private NetworkConfigurator configurator;
	private boolean publicAddressExclusive = false;
	
	public RdvRelayConfiguration( NetworkConfigurator configurator ) {
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
		case USE_ONLY_RELAY_SEEDS:
		case USE_ONLY_RENDEZVOUS_SEEDS:
			source.setProperty( property, Boolean.parseBoolean( value ));
			retval = true;
			break;
		case RELAY_8MAX_CLIENTS:
		case RENDEZVOUS_8MAX_CLIENTS:
			source.setProperty( property, Integer.parseInt( value ));
			retval = true;
			break;
		case RELAY_8SEED_URIS:
		case RENDEZVOUS_8SEED_URIS:
			//TODO source.setProperty( property, Boolean.parseBoolean( value ));
			retval = true;
			break;
		case RELAY_8SEEDING_URIS:
		case RENDEZVOUS_8SEEDING_URIS:
			//TODO
			source.setProperty( property, value );
			retval = true;
			break;
		default:
			break;
		}
		return retval;
	}	

	/**
	 * Fill the configurator with the given properties
	 * @param configurator
	 * @param property
	 * @param value
	 */
	@SuppressWarnings("unchecked")
	public static void fillConfigurator( NetworkConfigurator configurator, NetworkConfiguratorProperties property, Object value ){
		switch( property ){
		case USE_ONLY_RELAY_SEEDS:
			configurator.setUseOnlyRelaySeeds((boolean) value );
			break;
		case RELAY_8MAX_CLIENTS:
			configurator.setRelayMaxClients(( int )value );
			break;
		case RELAY_8SEEDING_URIS:
			configurator.setRelaySeedingURIs(( Set<String> )value );
			break;
		case RELAY_8SEED_URIS:
			configurator.setRelaySeedURIs(( List<String> ) value );
			break;
		case USE_ONLY_RENDEZVOUS_SEEDS:
			configurator.setUseOnlyRendezvousSeeds((boolean) value );
			break;
		case RENDEZVOUS_8MAX_CLIENTS:
			configurator.setRendezvousMaxClients(( int ) value );
			break;
		case RENDEZVOUS_8SEED_URIS:
			configurator.setRendezvousSeeds(( Set<String> ) value );
			break;
		case RENDEZVOUS_8SEEDING_URIS:
			configurator.setRendezvousSeedingURIs(( List<String> )value );
			break;
		default:
			break;
		}	
	}

}
