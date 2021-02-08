/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.validator;

public class StringValidator<T> extends ClassValidator<T,String> {

	public static final String S_NAME_REGEX = "[A_Z][\\w]*";
	public static final String S_BUNDLE_ID_REGEX = "[a-zA_Z_][\\.\\w]*";
	
	private static final String S_ERR_INVALID_REGEX = "The value does not comply with regular expression: ";

	private String regex;
	
	public StringValidator( T id, String regex ) {
		this(id, regex, false);
	}

	public StringValidator( T id, String regex, boolean nullable ) {
		super(id, String.class, nullable );
		this.regex = regex;
	}

	@Override
	public boolean validate( String value) {
		if( !super.validate(value) )
			return false;
		String msg = S_ERR_INVALID_REGEX + super.getId() + ": {" + value + "x> [" + this.regex + "]}";
		return super.validate(value, value.matches( regex ), msg );
	}

	@Override
	public String getMessage() {
		return null;
	}
}
