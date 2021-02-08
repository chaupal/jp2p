/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public class WritePropertySourceWrapper<T extends Object> extends PropertySourceWrapper<T> implements
		IJp2pWritePropertySource<T> {

	public WritePropertySourceWrapper(IJp2pPropertySource<T> source,
			boolean parentIsSource) {
		super(source, parentIsSource);
	}

	public WritePropertySourceWrapper(IJp2pPropertySource<T> source) {
		super(source);
	}

	@Override
	public ManagedProperty<T, Object> getOrCreateManagedProperty(T id,
			Object value, boolean derived) {
		IJp2pPropertySource<T> source = super.getSource();
		if(!(source instanceof IJp2pPropertySource))
			return super.getManagedProperty(id);
		IJp2pWritePropertySource<T> ws = (IJp2pWritePropertySource<T>) source;
		return ws.getOrCreateManagedProperty(id, value, derived);
	}

	@Override
	public boolean setProperty(T id, Object value) {
		IJp2pPropertySource<T> source = super.getSource();
		if(!(source instanceof IJp2pPropertySource))
			return false;
		IJp2pWritePropertySource<T> ws = (IJp2pWritePropertySource<T>) source;
		return ws.setProperty(id, value );
	}

	@Override
	public boolean setDirective(IJp2pDirectives id, String value) {
		IJp2pPropertySource<T> source = super.getSource();
		if(!(source instanceof IJp2pPropertySource))
			return false;
		IJp2pWritePropertySource<T> ws = (IJp2pWritePropertySource<T>) source;
		return ws.setDirective(id, value);
	}

}
