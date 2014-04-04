/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.filter;

public interface IFilter<T extends Object> {

	/**
	 * Returns true if the conditions of the filter are met. Note that this can be 
	 * the result of multiple calls
	 * @param event
	 * @return
	 */
	public boolean accept( T object );
	
	/**
	 * returns the result of the accept method, and is changed at the start of a new trial
	 * @return
	 */
	public boolean hasAccepted();
}
