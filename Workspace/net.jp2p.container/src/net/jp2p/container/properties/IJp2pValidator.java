/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public interface IJp2pValidator<T,U> {

	static final String S_MSG_VALIDATION_OK = "No problems were found while validating";
	
	/**
	 * Validate the given value
	 * @return
	 */
	public boolean validate( U value);
	
	/**
	 * Returns a message concerning the validation result
	 * @return
	 */
	public String getMessage();
}
