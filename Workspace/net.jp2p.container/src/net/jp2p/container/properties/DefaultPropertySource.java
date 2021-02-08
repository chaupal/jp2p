/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public class DefaultPropertySource extends AbstractJp2pWritePropertySource {

	public DefaultPropertySource(String bundleId, String componentName) {
		super(bundleId, componentName);
	}

	public DefaultPropertySource(String componentName,
			IJp2pPropertySource<IJp2pProperties> parent) {
		super(componentName, parent);
	}
}
