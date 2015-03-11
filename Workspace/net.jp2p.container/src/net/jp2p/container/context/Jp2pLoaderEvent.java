/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import java.util.EventObject;

public class Jp2pLoaderEvent extends EventObject {
	private static final long serialVersionUID = -7007824760022520347L;

	public enum LoaderEvent{
		REGISTERED,
		UNREGISTERED;
	}
	
	private LoaderEvent type;
	private IJp2pServiceBuilder builder;
	
	public Jp2pLoaderEvent( Object source, LoaderEvent type, IJp2pServiceBuilder builder) {
		super(source);
		this.type = type;
		this.builder = builder;
	}

	public LoaderEvent getType() {
		return type;
	}

	public IJp2pServiceBuilder getBuilder() {
		return builder;
	}
}
