/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;

public abstract class AbstractPropertyConvertor<T,U extends Object> implements
		IPropertyConvertor<T,U> {

	private  IJp2pWritePropertySource<IJp2pProperties> source;

	protected AbstractPropertyConvertor() {}

	protected AbstractPropertyConvertor( IJp2pWritePropertySource<IJp2pProperties> source ) {
		this.setPropertySource(source);
	}

	/**
	 * set the property source
	 * @param source
	 */
	public void setPropertySource( IJp2pWritePropertySource<IJp2pProperties> source ){
		this.source = source;
	}

	protected IJp2pPropertySource<IJp2pProperties> getSource() {
		return source;
	}

	@Override
	public boolean setPropertyFromConverion(IJp2pProperties id, T value) {
		source.getOrCreateManagedProperty(id, this.convertTo(id, value), false);
		return false;
	}

}
