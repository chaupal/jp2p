/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.context;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.factory.IComponentFactory;

public interface IContextObserver<T extends Object> {

	public void buildStarted( IJp2pContainer<T> context );
	
	/**
	 * Observes the creation of a factory in a context 
	 * @param factory
	 */
	public void factoryCreated( IComponentFactory<?> factory );

	/**
	 * Observes the creation of a component in a context 
	 * @param factory
	 */
	public void componentCreated( Object component );

	public void buildCompleted( IJp2pContainer<T> context );

}
