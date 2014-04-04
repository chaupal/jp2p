/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import java.util.EventObject;

import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;

public class ComponentBuilderEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private BuilderEvents builderEvent;
	
	public ComponentBuilderEvent(IPropertySourceFactory factory, BuilderEvents factoryEvent ){
		super(factory);
		this.builderEvent = factoryEvent;
	}
	
	public IPropertySourceFactory getFactory() {
		return (IPropertySourceFactory) super.source;
	}

	public BuilderEvents getBuilderEvent() {
		return builderEvent;
	}

	@Override
	public String toString() {
		return builderEvent.toString() + ": "+ super.getSource().toString();
	}
}
