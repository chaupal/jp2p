/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.filter;

public abstract class AbstractFilter<T extends Object>
		implements IFilter<T> {

	private T reference;
	private boolean accept;
	
	protected AbstractFilter( T reference ) {
		this.reference = reference;
	}

	protected T getReference() {
		return reference;
	}

	/**
	 * Provide the conditions on which the filter will accept the event
	 * @param event
	 * @return
	 */
	protected abstract boolean onAccept( T object );
	
	@Override
	public boolean accept( T object ) {
		this.accept = this.onAccept( object );
		return accept;
	}

	@Override
	public boolean hasAccepted() {
		return accept;
	}
}
