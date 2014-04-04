/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.validator;

public class RangeValidator<T> extends ClassValidator<T,Integer> {

	private static final String S_ERR_INVALID_RANGE = "The value is not within the range: ";

	private int min, max;
	
	public RangeValidator( T id, int max ) {
		this(id, 0, max, false);
	}

	public RangeValidator( T id, int min, int max, boolean nullable ) {
		super(id, Integer.class, nullable );
		this.min = min;
		this.max = max;
	}

	@Override
	public boolean validate( Integer value) {
		if( !super.validate(value) )
			return false;
		String msg = S_ERR_INVALID_RANGE + super.getId() + ": {" + value + "x> [" + this.min + ", " + this.max + "]}";
		return super.validate(value, ( value >= this.min ) && ( value <= this.max ), msg );
	}

	@Override
	public String getMessage() {
		return null;
	}
}
