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
import net.jp2p.chaupal.jxta.platform.security.SecurityFactory;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.factory.AbstractDependencyFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.jxta.factory.IJxtaComponents.JxtaPlatformComponents;
import net.jxta.exception.ConfiguratorException;
import net.jxta.platform.NetworkConfigurator;
import net.jxta.platform.NetworkManager;

public class NetworkConfigurationFactory extends AbstractDependencyFactory<NetworkConfigurator, IJp2pComponent<NetworkManager>> {

	private Collection<IJp2pPropertySource<IJp2pProperties>> sources;
	
	public  NetworkConfigurationFactory() {
		super( JxtaPlatformComponents.NETWORK_CONFIGURATOR.toString());
		sources = new ArrayList<IJp2pPropertySource<IJp2pProperties>>();
	}
	
	@Override
	protected NetworkConfigurationPropertySource onCreatePropertySource() {
		NetworkConfigurationPropertySource source = new NetworkConfigurationPropertySource( (NetworkManagerPropertySource) super.getParentSource() );
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
		sources.add( event.getFactory().getPropertySource());
		super.notifyChange(event);
	}

	protected void fillConfigurator( NetworkConfigurator configurator ){
		for( IJp2pPropertySource<IJp2pProperties> source: this.sources ){
			String name = StringStyler.styleToEnum( source.getComponentName());
			JxtaPlatformComponents comp = JxtaPlatformComponents.valueOf( name ); 
			switch( comp ){
			case NETWORK_CONFIGURATOR:
				NetworkConfigurationPropertySource.fillNetworkConfigurator( (NetworkConfigurationPropertySource) source, configurator);
				break;				
			default:
				break;
			}
		}
	}

	/**
	 * If a security service is not found, then include one
	 */
	@Override
	public void extendContainer() {
		IContainerBuilder<Object> builder = super.getBuilder();
		String comp = JxtaPlatformComponents.SECURITY.toString();
		IPropertySourceFactory security = builder.getFactory( comp );
		if( security != null )
			return;
		security = new SecurityFactory();
		security.prepare( super.getPropertySource(), builder, null );
		security.createPropertySource();
		builder.addFactory( security );
		super.extendContainer();	
	}
	

	
	@Override
	public IJp2pComponent<NetworkConfigurator> getComponent() {
		return super.getComponent();
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
		} catch (Exception e) {
			Logger log = Logger.getLogger( this.getClass().getName() );
			log.log( Level.SEVERE, e.getMessage() );
			e.printStackTrace();
			return null;
		}
		return new Jp2pComponentNode<NetworkConfigurator>( properties, configurator );
	}

	@Override
	protected synchronized IJp2pComponent<NetworkConfigurator> createComponent() {
		IJp2pComponent<NetworkConfigurator> configurator = super.createComponent();
		try {
			configurator.getModule().save();
		} catch (ConfiguratorException | IOException e) {
			e.printStackTrace();
		}
		return configurator;
		
	}
	
	
}