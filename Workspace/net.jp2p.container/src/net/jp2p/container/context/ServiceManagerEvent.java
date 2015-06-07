/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import java.util.EventObject;

public class ServiceManagerEvent extends EventObject {
	private static final long serialVersionUID = -1266257260044093122L;

	public ServiceManagerEvent(Object source) {
		super(source);
	}
}
