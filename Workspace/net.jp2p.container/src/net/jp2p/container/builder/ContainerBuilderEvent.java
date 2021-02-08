/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

import java.util.EventObject;

import net.jp2p.container.IJp2pContainer;

public class ContainerBuilderEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = -1266257260044093122L;

	public enum Types{
		START,
		SUCCESSFUL,
		ERROR,
	}
	
	private Types type;
	
	private IJp2pContainer<T> container;

	public ContainerBuilderEvent(Object source, IJp2pContainer<T> container) {
		this( source, Types.START, container );
	}
	
	public ContainerBuilderEvent(Object source, Types type, IJp2pContainer<T> container) {
		super(source);
		this.type = type;
		this.container = container;
	}

	public Types getType() {
		return type;
	}

	public IJp2pContainer<T> getContainer() {
		return container;
	}
}
