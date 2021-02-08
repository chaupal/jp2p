/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

import java.util.EventObject;

public class Jp2pComponentEvent<T extends Object> extends EventObject {

	private static final long serialVersionUID = 302931451825865288L;

	private T module;
	
	public Jp2pComponentEvent( IComponentFactory<T> source, T module ) {
		super(source);
		this.module = module;
	}

	public T getModule(){
		return module;
	}
}