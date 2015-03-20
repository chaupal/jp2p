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
import net.jp2p.container.properties.IJp2pWritePropertySource;

public abstract class AbstractPersistedProperty<T extends IJp2pProperties, U,V extends Object> implements
		IPersistedProperties<T,U,V> {

	private  IJp2pPropertySource<T> source;

	protected AbstractPersistedProperty() {}

	protected AbstractPersistedProperty( IJp2pWritePropertySource<T> source ) {
		this.setPropertySource(source);
	}

	/**
	 * set the property source
	 * @param source
	 */
	protected void setPropertySource( IJp2pWritePropertySource<T> source ){
		this.source = source;
	}

	protected IJp2pPropertySource<T> getSource() {
		return source;
	}
}
