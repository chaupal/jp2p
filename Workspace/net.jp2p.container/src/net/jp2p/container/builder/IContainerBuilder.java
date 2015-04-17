/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public interface IContainerBuilder<T extends Object> {

	/**
	 * Get the bundle id for this container
	 * @return
	 */
	public String getBundleID();
	
	public abstract boolean addFactory( IPropertySourceFactory factory);

	public abstract boolean removeFactory(IPropertySourceFactory factory);

	public abstract IPropertySourceFactory getFactory(String name);

	public IPropertySourceFactory[] getFactories();
	
	/**
	 * Returns the factory who'se source matched the given one
	 * @param source
	 * @return
	 */
	public abstract IPropertySourceFactory getFactory(
			IJp2pPropertySource<?> source);

	/**
	 * Returns true if all the factorys have been completed
	 * @return
	 */
	public abstract boolean isCompleted();

	/**
	 * List the factorys that did not complete
	 * @return
	 */
	public abstract String listModulesNotCompleted();

	/**
	 * Perform a request for updating the container
	 * @param event
	 */
	public abstract void updateRequest(ComponentBuilderEvent<?> event);

	/** Add a factory with the given component name to the container. use the given component name and parent,
	 * if 'createsource' is true, the property source is immediately created, and 'blockcreation' means that
	 * the builder will not create the factory. instead the parent factory should provide for this 
	 * 
	 * @param componentName
	 * @param createSource
	 * @param blockCreation
	 * @return
	 */
	public abstract IPropertySourceFactory addFactoryToContainer(
			String componentName,
			IJp2pPropertySource<IJp2pProperties> parentSource,
			boolean createSource, boolean blockCreation);

	/**
	 * Get or create a corresponding factory for a child component of the given source, with the given component name.
	 * @param source: the source who should have a child source
	 * @param componentName: the required component name of the child
	 * @param createSource: create the property source immediately
	 * @param blockCreation: do not allow the builder to create the component
	 * @return
	 */
	public abstract IPropertySourceFactory getOrCreateChildFactory(
			IJp2pPropertySource<IJp2pProperties> source, String componentName,
			boolean createSource, boolean blockCreation);
	
	/**
	 * Create the container
	 * @return
	 */
	public IJp2pContainer<T> createContainer();
}