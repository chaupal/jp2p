/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;

public interface IPersistedProperties<T,U extends Object> {

	/**
	 * set the convertor for this property
	 * @param context
	 */
	public void setConvertor( IPropertyConvertor<T,U> convertor );
	
	/**
	 * Clear the property
	 * @param source
	 */
	public void clear( IJp2pPropertySource<IJp2pProperties> source );
	
	/**
	 * Get the persisted property with the given id, if it exists
	 * @param id
	 * @return
	 */
	public abstract T getProperty( IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id);

	public boolean setProperty(IJp2pPropertySource<IJp2pProperties> source, IJp2pProperties id, T value);

}