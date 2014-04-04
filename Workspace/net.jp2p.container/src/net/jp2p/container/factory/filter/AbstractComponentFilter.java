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

public abstract class AbstractComponentFilter<T,U extends Object> extends AbstractBuilderEventFilter<T> {
	
	public static final String S_ERR_INVALID_EVENT = "The event is not valid; should be CreateComponent or StartComponent";
	
	//The component that has to be created or started
	private U component;
	
	public AbstractComponentFilter( BuilderEvents event, IComponentFactory<T> factory) {
		super(event, factory);
		if(!BuilderEvents.COMPONENT_CREATED.equals(event) && 
				!BuilderEvents.COMPONENT_STARTED.equals(event ) && 
				!BuilderEvents.FACTORY_COMPLETED.equals(event ))
			throw new IllegalArgumentException( S_ERR_INVALID_EVENT );
	}

	protected U getComponent() {
		return component;
	}

	/**
	 * Returns true if the component is correct
	 * @param component
	 * @return
	 */
	protected abstract boolean checkComponent( IComponentFactory<U> factory );
	
	@SuppressWarnings("unchecked")
	@Override
	protected boolean onCorrectBuilderEvent(ComponentBuilderEvent<?> event) {
		if( ((IComponentFactory<T>) event.getFactory()).getComponent() == null )
			return false;
		if( this.checkComponent((IComponentFactory<U>) event.getFactory() )){
			component = (U) ((IComponentFactory<T>) event.getFactory()).getComponent();
			return true;
		}
		return false;
	}
}