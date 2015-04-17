/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import java.util.Map;

import net.jp2p.container.builder.ICompositeBuilderListener;
import net.jp2p.container.builder.IContainerBuilder;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;

public interface IPropertySourceFactory extends ICompositeBuilderListener<Object>{

	/**
	 * Prepare the factory, by providing the necessary objects to embed the factory in the application
	 * @param componentName
	 * @param parentSource
	 * @param builder
	 * @param attributes
	 */
	public void prepare( IJp2pPropertySource<IJp2pProperties> parentSource, IContainerBuilder<Object> builder, Map<String, String> attributes );
	
	/**
	 * Get the component name that will be created
	 * @return
	 */
	public String getComponentName();
	
	/**
	 * Get the weight of the factory. By default, the context factory is zero, startup service is one
	 * @return
	 */
	public int getWeight();
	
	/**
	 * Returns true if the factory can create its product
	 * @return
	 */
	public boolean canCreate();

	/**
	 * Get the property source
	 * @return
	 */
	public IJp2pPropertySource<IJp2pProperties> getPropertySource();

	/**
	 * Step 1:
	 * Get the property source that is used for the factor
	 * @return
	 */
	public IJp2pPropertySource<IJp2pProperties> createPropertySource();
	
	/**
	 * Step 2:
	 * This method is called after the property sources have been created,
	 * to allow other factories to be added as well.
	 */
	public void extendContainer();
	
	/**
	 * Step 3:
	 * Parse the properties
	 */
	public void parseProperties();

}