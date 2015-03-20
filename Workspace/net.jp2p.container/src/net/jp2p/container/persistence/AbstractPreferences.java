/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.persistence;

import java.net.URISyntaxException;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.ManagedProperty;

public abstract class AbstractPreferences<T extends IJp2pProperties, U,V extends Object> implements IPropertyConvertor<T,U,V> {

	private IJp2pPropertySource<T> source;

	protected AbstractPreferences( IJp2pWritePropertySource<T> source ) {
		this.source = source;
	}

	protected IJp2pPropertySource<T> getSource() {
		return source;
	}
	
	/**
	 * Create a default value for the given id
	 * @param id
	 * @return
	 */
	public Object createDefaultValue( T id ){
		boolean create = ManagedProperty.isCreated( source.getManagedProperty(id));
		if( !create )
			return null;
		ManagedProperty<T, Object> property = source.getManagedProperty(id);
		if( property != null )
			return property.getDefaultValue();
		return null;
	}
	
	/**
	 * Set the correct property from the given string
	 * @param property
	 * @param value
	 * @return
	 * @throws URISyntaxException
	 */
	@Override
	public boolean setPropertyFromConverion( T id, U value ){
		ManagedProperty<T,Object> property = this.source.getManagedProperty(id);
		Object converted = this.convertTo( id, value);
		if( converted == null )
			return false;
		return property.setValue( converted);
	}

	@Override
	public T getIdFromString(String key) {
		return source.getConvertor().getIdFromString(key);
	}

}
