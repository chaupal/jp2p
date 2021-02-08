/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.factory;

public class FactoryException extends RuntimeException {
	private static final long serialVersionUID = -8597836283568092235L;

	public FactoryException() {
	}

	public FactoryException(String arg0) {
		super(arg0);
	}

	public FactoryException(Throwable arg0) {
		super(arg0);
	}

	public FactoryException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public FactoryException(String arg0, Throwable arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
