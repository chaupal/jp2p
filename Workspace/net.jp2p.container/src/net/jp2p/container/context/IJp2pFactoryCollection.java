/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import net.jp2p.container.factory.IPropertySourceFactory;

public interface IJp2pFactoryCollection{

	/**
	 * The name of the context
	 * @return
	 */
	public String getName();
	
	/**
	 * Returns true if the given component name is valid for this context
	 * @param descriptor
	 * @return
	 */
	public boolean hasFactory( Jp2pServiceDescriptor descriptor );

	/**
	 * Get the factory
	 * @param descriptor
	 * @return
	 */
	public IPropertySourceFactory getFactory( Jp2pServiceDescriptor descriptor );
}
