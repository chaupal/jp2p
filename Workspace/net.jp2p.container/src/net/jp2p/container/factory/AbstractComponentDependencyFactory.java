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

public abstract class AbstractComponentDependencyFactory<T extends Object, U extends Object> extends
		AbstractComponentFactory<T> {

	private IComponentFactoryFilter filter;
	private U dependency;

	protected AbstractComponentDependencyFactory( String componentName ) {
		super( componentName );
		this.filter = this.createFilter();
	}

	/**
	 * Get the dependency that must be provided in order to allow creation of the component
	 * @return
	 */
	protected U getDependency() {
		return dependency;
	}

	/**
	 * Set the correrct filter
	 * @return
	 */
	protected abstract IComponentFactoryFilter createFilter();
	
	public IComponentFactoryFilter getFilter() {
		return filter;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onNotifyChange(ComponentBuilderEvent<Object> event) {
		if( filter != null ){
			if( filter.accept(event)){
				IComponentFactory<T> factory = (IComponentFactory<T>) event.getFactory();
				dependency = (U) factory.getComponent();
				setCanCreate( dependency != null );
				if( super.canCreate() )
					startComponent();
			}
		}
	}

	/**
	 * returns true if the event is spawned from an immediate child
	 * @param event
	 * @return
	*/
	protected boolean isChildEvent( ComponentBuilderEvent<?> event ){
		return this.isChildFactory(event.getFactory() );
	}

	/**
	 * returns true if the event is spawned from the parent
	 * @param event
	 * @return
	*/
	protected boolean isParentEvent( ComponentBuilderEvent<?> event ){
		return this.isParentFactory(event.getFactory() );
	}

}