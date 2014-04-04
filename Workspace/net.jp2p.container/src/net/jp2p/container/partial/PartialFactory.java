/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.partial;

import net.jp2p.container.factory.AbstractPropertySourceFactory;

public class PartialFactory<T extends Object> extends AbstractPropertySourceFactory {

	private String componentName;
	
	public PartialFactory( String componentName ){
		this.componentName = componentName;
	}

	@Override
	public String getComponentName() {
		return componentName;
	}

	@Override
	protected PartialPropertySource onCreatePropertySource() {
		return new PartialPropertySource( this.componentName, super.getParentSource() );
	}
}
