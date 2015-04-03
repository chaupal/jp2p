/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

public interface IJp2pFactoryCollection{
	
	/**
	 * Returns true if the given component name is valid for this context
	 * @param descriptor
	 * @return
	 */
	public boolean hasFactory( Jp2pServiceDescriptor descriptor );
}