/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import java.util.EventObject;

import net.jp2p.container.Jp2pContainer.ServiceChange;
import net.jp2p.container.utils.Utils;

public class ComponentChangedEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private String identifier;
	private ServiceChange change;
	private T target;

	public ComponentChangedEvent(Object source, ServiceChange change ) {
		this( source, null, null, change );
	}

	public ComponentChangedEvent(T arg0, String identifier, ServiceChange change ) {
		this( arg0, arg0, identifier, change );
	}

	public ComponentChangedEvent(Object arg0, T target, String identifier, ServiceChange change ) {
		super(arg0);
		this.change = change;
		this.target = target;
		this.identifier = identifier;
	}

	public boolean isMatched( String id ){
		if( Utils.isNull( this.identifier))
			return false;
		return this.identifier.equals( id );
	}
	
	
	public final String getIdentifier() {
		return identifier;
	}

	public T getTarget() {
		return target;
	}

	public ServiceChange getChange() {
		return change;
	}

	@Override
	public String toString() {
		return this.identifier + ": " + change.toString() + "=>" + getSource().toString();
	}	
}
