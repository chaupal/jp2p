/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.xml;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import net.jp2p.container.builder.ContainerBuilder;
import net.jp2p.container.builder.ICompositeBuilder;
import net.jp2p.container.builder.ICompositeBuilderListener;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.builder.IFactoryBuilder;
import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.context.IJp2pServiceManager;
import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ChaupalContainer;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.ContainerFactory;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;
import net.jp2p.container.sequencer.Jp2pBundleSequencer;
import net.jp2p.container.xml.XMLFactoryBuilder;

/**
 * The container builder sees that all the factories that are needed to build the container are present.
 * First it loads all the required factories from the composite builders, then it appends the collection
 * with facotries that are also needed,
 * @author Kees
 *
 */
public class XMLContainerBuilder implements ICompositeBuilder<ChaupalContainer>{

	private String bundle_id;
	private Class<?> clss;
	private Collection<ICompositeBuilder<ContainerFactory>> builders;
	private IJp2pServiceManager manager;
	private Jp2pBundleSequencer<Object> sequencer;
	private boolean completed = false;
	private ChaupalContainer container;
	private DeveloperModes mode;
	
	private Collection<ICompositeBuilderListener<Object>> listeners;
	
	public XMLContainerBuilder( String bundle_id, Class<?> clss, DeveloperModes mode, IJp2pServiceManager manager, Jp2pBundleSequencer<Object> sequencer) {
		this.bundle_id = bundle_id;
		this.clss = clss;	
		this.manager = manager;
		this.mode = mode;
		this.sequencer = sequencer;
		builders = new ArrayList<ICompositeBuilder<ContainerFactory>>();
		this.listeners = new ArrayList<ICompositeBuilderListener<Object>>();
	}
	
	@Override
	public void build() {
		
		//First register all the discovered builders
		IContainerBuilder<Object> containerBuilder = new ContainerBuilder( mode );
		try {
			this.extendBuilders( containerBuilder, clss);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ICompositeBuilderListener<?> listener = new ICompositeBuilderListener<Object>(){

			@Override
			public void notifyChange(ComponentBuilderEvent event) {
				for( ICompositeBuilderListener<Object> listener: listeners )
					listener.notifyChange(event);
				
			}	
		};
		
		//Build the container from the resource files
		for( ICompositeBuilder<ContainerFactory> builder: this.builders){
			builder.addListener(listener);
			builder.build();
			builder.removeListener(listener);
		}

		//Extend the container with factories that are also needed
		this.extendContainer( containerBuilder );
		this.notifyPropertyCreated( containerBuilder);
		
		//Last create the container and the components
		this.completed = true;
		this.container = (ChaupalContainer) containerBuilder.createContainer();
	}

	@Override
	public boolean isCompleted() {
		return this.completed;
	}
	
	public final ChaupalContainer getContainer() {
		return container;
	}

	/**
	 * Allow additional builders to extend the primary builder, by looking at resources with the
	 * similar name and location, for instance provided by fragments
	 * @param clss
	 * @param containerBuilder
	 * @throws IOException
	 */
	private void extendBuilders( IContainerBuilder<Object> builder, Class<?> clss ) throws IOException{
		Enumeration<URL> enm = clss.getClassLoader().getResources( IFactoryBuilder.S_DEFAULT_LOCATION );
		while( enm.hasMoreElements()){
			URL url = enm.nextElement();
			builders.add( new XMLFactoryBuilder( bundle_id, url, clss, mode, builder, manager, sequencer ));
		}
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void addListener(ICompositeBuilderListener<?> listener) {
		this.listeners.add((ICompositeBuilderListener<Object>) listener);
	}

	@Override
	public void removeListener(ICompositeBuilderListener<?> listener) {
		this.listeners.remove(listener);
	}

	/**
	 * Notify that the property sources have been created after parsing the XML
	 * file. This allows for more fine-grained tuning of the property sources.
	 * Factories can add additional resource if they need it, and properties can be filled in,
	 * which corresponded with other factories.
	 * The extension consists of three steps:
	 * 
	 * Step 1: perform the early start
	 * Step 2: Extend the properties with others
	 * Step 3: parse the properties
	 * @param node
	 */
	private void extendContainer( IContainerBuilder<Object> containerBuilder){
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
	private void notifyPropertyCreated( IContainerBuilder<Object> containerBuilder){
		for( IPropertySourceFactory factory: containerBuilder.getFactories() ){
			containerBuilder.updateRequest( new ComponentBuilderEvent(factory, BuilderEvents.PROPERTY_SOURCE_CREATED));
		}
	}

}
