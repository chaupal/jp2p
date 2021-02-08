/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory.filter;

import net.jp2p.container.builder.ICompositeBuilderListener.BuilderEvents;
import net.jp2p.container.factory.IComponentFactory;

public class ComponentCreateFilter<T, U extends Object> extends
		AbstractComponentFilter<T, U> {

	private String componentName;
	
	public ComponentCreateFilter(BuilderEvents event, String componentName, IComponentFactory<T> factory ) {
		super(event, factory);
		this.componentName = componentName;
	}


	@Override
	public U getComponent() {
		return super.getComponent();
	}


	@Override
	protected boolean checkComponent(IComponentFactory<U> factory) {
		return ( componentName.equals( factory.getComponentName()));
	}

}
