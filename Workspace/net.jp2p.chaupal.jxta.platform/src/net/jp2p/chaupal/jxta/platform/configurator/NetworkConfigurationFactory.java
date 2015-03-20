/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.configurator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.jp2p.chaupal.jxta.platform.NetworkManagerPropertySource;
import net.jp2p.chaupal.jxta.platform.configurator.NetworkConfigurationPropertySource.NetworkConfiguratorProperties;
import net.jp2p.chaupal.jxta.platform.http.HttpPropertySource;
import net.jp2p.chaupal.jxta.platform.seeds.SeedListFactory;
import net.jp2p.chaupal.jxta.platform.seeds.SeedListPropertySource;
import net.jp2p.chaupal.jxta.platform.tcp.TcpPropertySource;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponent;
import net.jp2p.container.factory.AbstractDependencyFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.exception.ConfiguratorException;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public class NetworkConfigurationFactory extends AbstractDependencyFactory<NetworkConfigurator, IJp2pComponent<NetworkManager>> {

	private Collection<SeedListPropertySource> seedlists;
	private Collection<IJp2pPropertySource<IJp2pProperties>> sources;
	
	public  NetworkConfigurationFactory() {
		super( JxtaPlatformComponents.NETWORK_CONFIGURATOR.toString());
		sources = new ArrayList<IJp2pPropertySource<IJp2pProperties>>();
	}
	
	@Override
	protected NetworkConfigurationPropertySource onCreatePropertySource() {
		NetworkConfigurationPropertySource source = new NetworkConfigurationPropertySource( (NetworkManagerPropertySource) super.getParentSource() );
		seedlists = new ArrayList<SeedListPropertySource>();
		SeedListPropertySource slps = new SeedListPropertySource( source, source.getClass() );
		if( slps.hasSeeds() )
			seedlists.add(slps);
		return source;
	}
	
	@Override
	protected boolean isCorrectFactory(IComponentFactory<?> factory) {
		if(!( factory.getComponent() instanceof IJp2pComponent ))
			return false;
		IJp2pComponent<?> component = (IJp2pComponent<?>) factory.getComponent();
		return ( component.getModule() instanceof NetworkManager);
	}

	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		String name = StringStyler.styleToEnum( event.getFactory().getComponentName() );
		if( !JxtaPlatformComponents.isComponent( name ))
			return;
		switch( event.getBuilderEvent() ){
		case PROPERTY_SOURCE_CREATED:
			switch( JxtaPlatformComponents.valueOf( name )){
			case SEED_LIST:
				seedlists.add( (SeedListPropertySource) event.getFactory().getPropertySource() );
				break;
			default:
				sources.add( event.getFactory().getPropertySource());
				break;
			}
			break;
		default:
			break;
		}
		super.notifyChange(event);
	}

	protected void fillConfigurator( NetworkConfigurator configurator ){
		for( IJp2pPropertySource<IJp2pProperties> source: this.sources ){
			String name = StringStyler.styleToEnum( source.getComponentName());
			JxtaPlatformComponents comp = JxtaPlatformComponents.valueOf( name ); 
			switch( comp ){
			case HTTP:
				HttpPropertySource.fillHttpNetworkConfigurator((HttpPropertySource) source, configurator);
				break;
			case HTTP2:
				HttpPropertySource.fillHttp2NetworkConfigurator((HttpPropertySource) source, configurator);
				break;
			case TCP:
				TcpPropertySource.fillTcpNetworkConfigurator((TcpPropertySource) source, configurator);
				break;
			default:
				break;
			}
		}
	}
	@Override
	protected IJp2pComponent<NetworkConfigurator> onCreateComponent( IJp2pPropertySource<IJp2pProperties> properties) {
		NetworkConfigurator configurator = null;
		try {
			NetworkManager manager = super.getDependency().getModule();
			configurator = manager.getConfigurator();
			URI home = (URI) super.getPropertySource().getProperty( NetworkConfiguratorProperties.HOME );
			if( home != null )
				configurator.setHome( new File( home ));
			this.fillConfigurator(configurator );
			configurator.clearRelaySeeds();
			configurator.clearRendezvousSeeds();
			for( SeedListPropertySource source: this.seedlists )
				SeedListFactory.fillSeeds(configurator, source);
			configurator.save();
		} catch (IOException | ConfiguratorException e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
			return null;
		}
		return new Jp2pComponent<NetworkConfigurator>( properties, configurator );
	}
	
	
}