/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public abstract class AbstractPropertyConvertor<T extends IJp2pProperties, U,V extends Object> implements
		IPropertyConvertor<T,U,V> {

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
	public boolean setPropertyFromConverion( T id, U value) {
		source.getOrCreateManagedProperty(id, this.convertTo(id, value), false);
		return false;
	}

}
