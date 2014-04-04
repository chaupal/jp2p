/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

import net.jp2p.container.properties.IJp2pDirectives;

public class StringDirective implements IJp2pDirectives{

	private String key;
	
	public StringDirective( String key ) {
		this.key = key;
	}

	@Override
	public String name() {
		return key;
	}

	@Override
	public String toString() {
		return StringStyler.prettyString(key);
	}
}
