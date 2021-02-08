/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

public abstract class AbstractDependencyFactory<T extends Object, U extends Object> extends
		AbstractComponentFactory<T> {

	
	protected AbstractDependencyFactory(String componentName) {
		super(componentName);
	}

	private U dependency;

	/**
	 * Get the dependency that must be provided in order to allow creation of the cpomponent
	 * @return
	 */
	protected U getDependency() {
		return dependency;
	}

	/**
	 * Get the component name of the dependency
	 * @return
	 */
	protected abstract boolean isCorrectFactory( IComponentFactory<?> factory );
	
	@SuppressWarnings("unchecked")
	@Override
	public void onNotifyChange(ComponentBuilderEvent event) {
		switch( event.getBuilderEvent()){
		case COMPONENT_CREATED:
			IComponentFactory<?> factory = (IComponentFactory<?>) event.getFactory();
			if( !this.isCorrectFactory( factory ))
				return;
			dependency = (U) factory.createComponent();
			super.setCanCreate( dependency != null );
			if( super.canCreate() )
				super.startComponent();
			break;
		default:
			break;
		}
	}
}