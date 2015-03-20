/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.seeds;

import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.INetworkPreferences;
import net.jp2p.chaupal.jxta.platform.seeds.ISeedInfo.SeedTypes;
import net.jp2p.container.persistence.AbstractPreferences;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.ManagedProperty;
import net.jxta.platform.NetworkConfigurator;

public class SeedConvertor extends AbstractPreferences<IJp2pProperties, String, Object> implements INetworkPreferences{

	public static final String S_SEEDS = "Seeds";

	public SeedConvertor( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		ISeedInfo value = (ISeedInfo) super.getSource().getProperty(id);
		return value.toString();
	}

	/* (non-Javadoc)
	 * @see net.osgi.jxse.network.INetworkPreferences#setPropertyFromString(net.osgi.jxse.network.NetworkConfigurationPropertySource.NetworkConfiguratorProperties, java.lang.String)
	 */
	@Override
	public Object convertTo( IJp2pProperties key, String value ){
		SeedInfo seedInfo = new SeedInfo();
		seedInfo.parse( key.toString(), value );
		return seedInfo;
	}	

	/**
	 * Create a default value if this is requested as attribute and adds it to the source if it is not present 
	 * @param id
	 * @return
	 */
	@Override
	public Object createDefaultValue( IJp2pProperties key ){
		return null;
	}

	/**
	 * Fill the configurator with the given properties from a key string
	 * @param configurator
	 * @param property
	 * @param value
	*/
	@Override
	public boolean fillConfigurator( NetworkConfigurator configurator ){
		Iterator<IJp2pProperties> iterator = super.getSource().propertyIterator();
		boolean retval = true;
		while( iterator.hasNext() ){
			IJp2pProperties id = iterator.next();
			retval &= fillConfigurator(configurator, id, super.getSource().getManagedProperty(id));
		}
		return retval;
	}

	/**
	 * Fill the configurator with the given properties
	 * @param configurator
	 * @param property
	 * @param value
	 */
	public static boolean fillConfigurator( NetworkConfigurator configurator, IJp2pProperties key, ManagedProperty<IJp2pProperties,Object> property ){
		boolean retval = true;
		Object value = property.getValue();
		if( value == null )
			return false;
		if(!( value instanceof ISeedInfo ))
			return false;
		ISeedInfo si = ( ISeedInfo )value;	
		if( si.getSeedType().equals( SeedTypes.RDV ))
			configurator.addRdvSeedingURI( si.getUri().getPath() );
		if( si.getSeedType().equals( SeedTypes.RELAY ))
			configurator.addRelaySeedingURI( si.getUri().getPath() );
		return retval;
	}
}
