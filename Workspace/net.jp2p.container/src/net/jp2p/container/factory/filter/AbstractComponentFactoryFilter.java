/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory.filter;

import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.IComponentFactory;

public abstract class AbstractComponentFactoryFilter<T extends Object>
		implements IComponentFactoryFilter {

	private IComponentFactory<T> factory;
	private boolean accept;
	
	protected AbstractComponentFactoryFilter( IComponentFactory<T> factory ) {
		this.factory = factory;
	}

	protected IComponentFactory<T> getFactory() {
		return factory;
	}

	/**
	 * Provide the conditions on which the filter will accept the event
	 * @param event
	 * @return
	 */
	protected abstract boolean onAccept( ComponentBuilderEvent event );
	
	@Override
	public boolean accept(ComponentBuilderEvent event) {
		this.accept = this.onAccept(event);
		return accept;
	}

	@Override
	public boolean hasAccepted() {
		return accept;
	}
}
