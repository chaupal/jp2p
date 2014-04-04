/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

public interface IJp2pWritePropertySource< T extends Object> extends IJp2pPropertySource<T> {
	
	public ManagedProperty<T,Object> getOrCreateManagedProperty(T id, Object value, boolean derived );

	/**
	 * Set the value for the given property
	 * @param id
	 * @param value
	 * @return 
	 */
	public boolean setProperty( T id, Object value );
	
	/**
	 * add a child source
	 * @param child
	 * @return
	 */
	@Override
	public boolean addChild( IJp2pPropertySource<?> child );
	
	boolean setDirective(IJp2pDirectives id, String value); 
}
