/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import java.util.EventObject;

public class ContextLoaderEvent extends EventObject {
	private static final long serialVersionUID = -7007824760022520347L;

	public enum LoaderEvent{
		REGISTERED,
		UNREGISTERED;
	}
	
	private LoaderEvent type;
	private IJp2pContext context;
	
	public ContextLoaderEvent( Object source, LoaderEvent type, IJp2pContext context) {
		super(source);
		this.type = type;
		this.context = context;
	}

	public LoaderEvent getType() {
		return type;
	}

	public IJp2pContext getContext() {
		return context;
	}
}
