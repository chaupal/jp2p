/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.seeds;

import java.util.Iterator;

import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.platform.NetworkConfigurator;

public class SeedListFactory extends AbstractPropertySourceFactory{
	
	public SeedListFactory() {
		super(JxtaPlatformComponents.SEED_LIST.toString());
	}

	@Override
	protected SeedListPropertySource onCreatePropertySource() {
		return new SeedListPropertySource( super.getComponentName(), super.getParentSource() );
	}

	/**
	 * Fill the configurator with the seeds
	 * @param configurator
	 * @param source
	 */
	public static void fillSeeds( NetworkConfigurator configurator, SeedListPropertySource source) {
		Iterator<IJp2pProperties> iterator = source.propertyIterator();
		while( iterator.hasNext() ){
			IJp2pProperties key = iterator.next();
			if(!( source.getProperty(key) instanceof SeedInfo ))
				continue;
			SeedInfo seedInfo = ( SeedInfo ) source.getProperty( key);
			switch( seedInfo.getSeedType() ){
			case RDV:
				configurator.addSeedRendezvous( seedInfo.getUri() );
				break;
			case RELAY:
				configurator.addRelaySeedingURI( seedInfo.getUri() );	
				break;
			}
		}
	}
}