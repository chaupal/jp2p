/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public interface IPropertyConvertor<T, U,V extends Object> {

	/**
	 * Get the id from a string representation
	 * @param key
	 * @return
	 */
	public T getIdFromString( String key );
	

	/**
	 * Convert the given id to value T
	 * @param id
	 * @return
	 */
	public U convertFrom( T id);

	/**
	 * Convert the given value T to the correct internal value
	 * @param id
	 * @param value
	 * @return
	 */
	public V convertTo( T id, U value );
	
	/**
	 * Convert the given value T to the correct internal value, and store it in the property source.
	 * returns true if the value is stored
	 * 
	 * @param id
	 * @param value
	 */
	public boolean setPropertyFromConverion( T id, U value);
}
