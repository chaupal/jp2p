/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.utils.StringProperty;

public class SimplePropertyConvertor extends AbstractPropertyConvertor<IJp2pProperties, String, Object> {

	public SimplePropertyConvertor( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source );
	}

	@Override
	public String convertFrom(IJp2pProperties id) {
		Object value = super.getSource().getProperty(id);
		if( value == null )
			return null;
		return value.toString();
	}

	@Override
	public Object convertTo(IJp2pProperties id,String value) {
		return null;
	}

	@Override
	public IJp2pProperties getIdFromString(String key) {
		return new StringProperty( key );
	}
}
