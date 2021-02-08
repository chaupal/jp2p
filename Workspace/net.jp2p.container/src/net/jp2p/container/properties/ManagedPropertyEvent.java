/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import java.util.EventObject;

import net.jp2p.container.properties.IManagedPropertyListener.PropertyEvents;


public class ManagedPropertyEvent<T,U extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private PropertyEvents event;
	
	public ManagedPropertyEvent( ManagedProperty<T,U> property, PropertyEvents event) {
		super(property);
		this.event = event;
	}
	
	@SuppressWarnings("unchecked")
	public T getId(){
		ManagedProperty<T,U> property = (ManagedProperty<T, U>) super.getSource();
		return property.getKey();
	}

	@SuppressWarnings("unchecked")
	public U getValue(){
		ManagedProperty<T,U> property = (ManagedProperty<T, U>) super.getSource();
		return property.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public ManagedProperty<T,U> getProperty(){
		return (ManagedProperty<T, U>) this.getSource();
	}

	public PropertyEvents getEvent() {
		return event;
	}

}
