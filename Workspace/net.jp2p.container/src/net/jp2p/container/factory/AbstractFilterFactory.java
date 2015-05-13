/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import net.jp2p.container.factory.AbstractComponentFactory;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.filter.IComponentFactoryFilter;

public abstract class AbstractFilterFactory<T extends Object> extends
		AbstractComponentFactory<T> {

	private IComponentFactoryFilter filter;

	protected AbstractFilterFactory( String componentName ) {
		super( componentName );
		filter = this.createFilter();
	}

	/**
	 * Set the correrct filter
	 * @return
	 */
	protected abstract IComponentFactoryFilter createFilter();
	
	public IComponentFactoryFilter getFilter() {
		return filter;
	}

	@Override
	public void notifyChange(ComponentBuilderEvent<Object> event) {
		if( filter != null ){
			if( filter.accept(event)){
				setCanCreate( true );
				if( super.canCreate() )
					startComponent();
			}
		}
		super.notifyChange(event);
	}
}