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

public interface IPersistedProperties<T extends IJp2pProperties ,U,V extends Object> {

	/**
	 * set the convertor for this property
	 * @param context
	 */
	public void setConvertor( IPropertyConvertor<T,U,V> convertor );
	
	/**
	 * Clear the property
	 * @param source
	 */
	public void clear( IJp2pPropertySource<T> source );
	
	/**
	 * Get the persisted property with the given id, if it exists
	 * @param id
	 * @return
	 */
	public abstract U getProperty( IJp2pPropertySource<T> source, T id);

	public boolean setProperty(IJp2pPropertySource<T> source, T id, U value);

}