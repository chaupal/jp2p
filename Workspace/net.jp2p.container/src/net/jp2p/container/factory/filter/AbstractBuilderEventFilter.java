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

public abstract class AbstractBuilderEventFilter<T extends Object> extends
		AbstractComponentFactoryFilter<T> {

	private BuilderEvents event;
	
	public AbstractBuilderEventFilter( BuilderEvents event, IComponentFactory<T> factory) {
		super(factory);
		this.event = event;
	}

	protected BuilderEvents getEvent() {
		return event;
	}

	/**
	 * Is called when the correct builder event is given. Should return true if the filter conditions are met
	 * @param event
	 * @return
	 */
	protected abstract boolean onCorrectBuilderEvent(ComponentBuilderEvent event);
	
	@Override
	public boolean onAccept(ComponentBuilderEvent event) {
		if( !this.event.equals( event.getBuilderEvent() ))
			return false;
		return this.onCorrectBuilderEvent(event);
	}

}
