/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.seeds;

import java.util.Iterator;

import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationFactory;
import net.jp2p.chaupal.jxta.platform.seeds.ISeedInfo.SeedTypes;
import net.jp2p.chaupal.platform.INetworkConfigurator;
import net.jp2p.container.factory.AbstractPropertySourceFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;

public class SeedListFactory extends AbstractPropertySourceFactory{
	
	public SeedListFactory() {
		super(JxtaPlatformComponents.SEED_LIST.toString());
	}

	@Override
	protected SeedListPropertySource onCreatePropertySource() {
		return new SeedListPropertySource( super.getComponentName(), super.getParentSource() );
	}

	@Override
	protected IJp2pDirectives onConvertDirective( String key, String value ) {
		String str = StringStyler.styleToEnum( key );
		if( SeedListPropertySource.SeedListDirectives.isValidDirective( str ))
			return SeedListPropertySource.SeedListDirectives.valueOf( str );
		Directives directive = Directives.valueOf( str );
		if( Directives.TYPE.equals( directive )){
			super.setComponentName( super.getComponentName() + "." + value );
		}
		return super.onConvertDirective(key, value );
	}

	@Override
	public void notifyChange(ComponentBuilderEvent event) {
		if( !BuilderEvents.COMPONENT_CREATED.equals( event.getBuilderEvent()))
			return;
		String name = StringStyler.styleToEnum( event.getFactory().getComponentName() );
		if( !JxtaPlatformComponents.isComponent( name ))
			return;
		if( !JxtaPlatformComponents.NETWORK_CONFIGURATOR.equals( JxtaPlatformComponents.valueOf( name )))
			return;
		NetworkConfigurationFactory factory = (NetworkConfigurationFactory) event.getFactory();
		INetworkConfigurator configurator = factory.createComponent().getModule();
		SeedListPropertySource source = (SeedListPropertySource) super.getPropertySource();
		
		SeedTypes type = SeedListPropertySource.getSeedListType( source );
		switch( type ){
		case RELAY:
			SeedListPropertySource.fillRelayNetworkConfigurator( source, configurator);
		default:
			SeedListPropertySource.fillRendezvousNetworkConfigurator( source, configurator);
		}
		fillSeeds( source, configurator );
		super.notifyChange(event);
	}

	/**
	 * Fill the configurator with the seeds
	 * @param configurator
	 * @param source
	 */
	private static void fillSeeds( SeedListPropertySource source, INetworkConfigurator configurator) {
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