/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.validator;

import net.jp2p.container.properties.AbstractJp2pValidator;

public class ClassValidator<T,U> extends AbstractJp2pValidator<T,U> {

	private static final String S_ERR_INVALID_CLASS = "The class is invalid of ";

	private Class<U> clss;
	
	public ClassValidator( T id, Class<U> clss ) {
		this(id, clss, false);
	}

	public ClassValidator( T id, Class<U> clss, boolean nullable ) {
		super(id, nullable );
		this.clss = clss;
	}

	@Override
	public boolean validate(Object value) {
		if( !super.validate(value) )
			return false;
		String msg = S_ERR_INVALID_CLASS + super.getId() + ": {" + value.getClass().getName() + "->" +  clss.getName() + "}";
		return super.validate(value, value.getClass().equals( clss ), msg );
	}

	@Override
	public String getMessage() {
		return null;
	}

}
