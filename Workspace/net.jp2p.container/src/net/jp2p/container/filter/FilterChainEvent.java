/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.filter;

import java.util.EventObject;

import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;

public class FilterChainEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	
	private IComponentFactory<?> factory;
	
	public FilterChainEvent(IComponentFactoryFilter filter, IComponentFactory<?> factory ){
		super(filter);
		this.factory = factory;
	}
	
	public IComponentFactoryFilter getFilter() {
		return (IComponentFactoryFilter) super.source;
	}

	/**
	 * Get the factory thAat caused the event
	 * @return
	 */
	public IComponentFactory<?> getFactory() {
		return factory;
	}
}
