/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import net.jp2p.container.ContainerFactory;
import net.jp2p.container.Jp2pContainer;
import net.jp2p.container.builder.ContainerBuilder;
import net.jp2p.container.builder.ICompositeBuilder;
import net.jp2p.container.builder.ICompositeBuilderListener;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.context.ContextLoader;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;

public class XMLContainerBuilder implements ICompositeBuilder<Jp2pContainer>{

	private String plugin_id;
	private Class<?> clss;
	private Collection<ICompositeBuilder<ContainerFactory>> builders;
	private ContextLoader contexts;
	
	private Collection<ICompositeBuilderListener<?>> listeners;
	
	public XMLContainerBuilder( String plugin_id, Class<?> clss, ContextLoader contexts) {
		this.plugin_id = plugin_id;
		this.clss = clss;	
		this.contexts = contexts;
		builders = new ArrayList<ICompositeBuilder<ContainerFactory>>();
		this.listeners = new ArrayList<ICompositeBuilderListener<?>>();
	}
	
	@Override
	public Jp2pContainer build() {
		
		//First register all the discovered builders
		ContainerBuilder containerBuilder = new ContainerBuilder();
		try {
			this.extendBuilders(clss, containerBuilder);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Build the container from the resource files
		for( ICompositeBuilder<ContainerFactory> builder: this.builders){
			this.addListenersToBuilder(builder);
			builder.build();
			this.removeListenersFromBuilder(builder);
		}

		//Extend the container with factories that are also needed
		this.extendContainer( containerBuilder );
		this.notifyPropertyCreated( containerBuilder);
		
		//Last create the container and the components
		ContainerFactory factory = (ContainerFactory) containerBuilder.getFactory( Jp2pContext.Components.JP2P_CONTAINER.toString() );
		return factory.createComponent();
	}

	/**
	 * Allow additional builders to extend the primary builder, by looking at resources with the
	 * similar name and location, for instance provided by fragments
	 * @param clss
	 * @param containerBuilder
	 * @throws IOException
	 */
	private void extendBuilders( Class<?> clss, IContainerBuilder containerBuilder ) throws IOException{
		Enumeration<URL> enm = clss.getClassLoader().getResources( IFactoryBuilder.S_DEFAULT_LOCATION );
		while( enm.hasMoreElements()){
			URL url = enm.nextElement();
			builders.add( new XMLFactoryBuilder( plugin_id, url, clss, containerBuilder, contexts ));
		}
	}
	

	@Override
	public void addListener(ICompositeBuilderListener<?> listener) {
		this.listeners.add(listener);
	}

	@Override
	public void removeListener(ICompositeBuilderListener<?> listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Delay the addition of builders until the parsing starts
	 * @param builder
	 */
	private final void addListenersToBuilder(ICompositeBuilder<?> builder) {
		for( ICompositeBuilderListener<?> listener: this.listeners )
			builder.addListener(listener);
	}

	/**
	 * Remove the listeners from the builder
	 * @param builder
	 */
	private void removeListenersFromBuilder(ICompositeBuilder<?> builder) {
		for( ICompositeBuilderListener<?> listener: this.listeners )
			builder.removeListener(listener);
	}

	/**
	 * Notify that the property sources have been created after parsing the XML
	 * file. This allows for more fine-grained tuning of the property sources.
	 * Factories can add additional resource if they need it, and properties can be filled in,
	 * which are corresponded with other factories.
	 * The extension consists of three steps:
	 * 
	 * Step 1: perform the early start
	 * Step 2: Extend the properties with others
	 * Step 3: parse the properties
	 * @param node
	 */
	private void extendContainer( ContainerBuilder containerBuilder){
		IPropertySourceFactory[] factories = containerBuilder.getFactories();
		for( IPropertySourceFactory factory: factories ){
			if( factory instanceof IComponentFactory<?>)
				((AbstractComponentFactory<?>) factory).earlyStart();
		}
		for( IPropertySourceFactory factory: factories ){
			factory.extendContainer();
		}
		for( IPropertySourceFactory factory: factories ){
			factory.parseProperties();
		}
	}

	/**
	 * Notify that the property sources have been created after parsing the XML
	 * file. This allows for more fine-grained tuning of the property sources
	 * @param node
	 */
	private void notifyPropertyCreated( ContainerBuilder containerBuilder){
		for( IPropertySourceFactory factory: containerBuilder.getFactories() ){
			containerBuilder.updateRequest( new ComponentBuilderEvent<Object>(factory, BuilderEvents.PROPERTY_SOURCE_CREATED));
		}
	}

}
