/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory.filter;

import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;

public class FactoryFilter<T,U extends Object> extends AbstractBuilderEventFilter<T> {
	
	private String componentName;
	
	public FactoryFilter( BuilderEvents event, String componentName, IComponentFactory<T> factory) {
		super(event, factory);
		this.componentName = componentName;
	}

	@Override
	public IComponentFactory<T> getFactory() {
		return super.getFactory();
	}

	@Override
	protected boolean onCorrectBuilderEvent(ComponentBuilderEvent<?> event) {
		return ( componentName.equals( event.getFactory().getComponentName()));		
	}
}