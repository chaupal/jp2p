/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public class ValidationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ValidationException( String msg ) {
		super( msg);
	}

	public ValidationException( IJp2pValidator<?,?> arg0 ) {
		super(arg0.getMessage());
	}
}
