/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator;

import java.io.File;
import java.net.URI;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource;
import net.jp2p.chaupal.jxta.root.network.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jxta.peer.PeerID;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager.ConfigMode;

public class OverviewConfiguration {

	public static final String S_OVERVIEW = "Overview";

	private NetworkConfigurator configurator;
	private String description;
	
	public OverviewConfiguration( NetworkConfigurator configurator ) {
		this.configurator = configurator;
	}

	public ConfigMode getMode(){
		return ConfigMode.values()[ this.configurator.getMode() ];
	}

	public void setConfigMode( ConfigMode mode ){
		this.configurator.setMode( mode.ordinal() );
	}

	public File getHome(){
		return this.configurator.getHome();
	}

	public void setHome( File home ){
		this.configurator.setHome( home );
	}

	public URI getStoreHome(){
		return this.configurator.getStoreHome();
	}

	public void setStoreHome( URI storeHome ){
		this.configurator.setStoreHome( storeHome);
	}

	public String getDescription(){
		return this.description;
	}

	public void setDescription( String description ){
		this.description = description;
		this.configurator.setDescription(description);	
	}

	public String getName(){
		return this.configurator.getName();
	}

	public void setName( String name ){
		this.configurator.setName( name );	
	}

	public PeerID getPeerID(){
		return this.configurator.getPeerID();
	}

	public void setPeerID( PeerID peerId ){
		this.configurator.setPeerID( peerId );
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
		URI uri = null;
		switch( property ){
		case CONFIG_MODE:
			source.setProperty( property, ConfigMode.valueOf( value ));
			retval = true;
			break;
		case STORE_HOME:
			uri = URI.create(value);
			source.setProperty( property, uri);
			retval = true;
			break;
		case PEER_ID:
			uri = URI.create(value);
			source.setProperty( property, PeerID.create(uri ));
			retval = true;
			break;
		case DESCRIPTION:
		case HOME:
		case NAME:
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
	public static void fillConfigurator( NetworkConfigurator configurator, NetworkConfiguratorProperties property, Object value ){
		switch( property ){
		case CONFIG_MODE:
			configurator.setMode(( int ) value );
			break;
		case PEER_ID:
			configurator.setPeerID(( PeerID ) value );
			break;
		case DESCRIPTION:
			configurator.setDescription(( String )value );
			break;
		case HOME:
			configurator.setHome(( File )value );
			break;
		case STORE_HOME:
			configurator.setStoreHome(( URI ) value );
			break;
		case NAME:
			configurator.setName(( String ) value );
			break;
		default:
			break;
		}	
	}

}
