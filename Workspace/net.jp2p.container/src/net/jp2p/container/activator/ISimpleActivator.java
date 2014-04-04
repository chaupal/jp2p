/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

public interface ISimpleActivator
{
	public static final String S_ACTIVATOR = "Activator";
	
	
	/**
	 * Start activating. Returns true if the activation is possible, false
	 * if not, for instance when the activator is already started
	 */
	public boolean start();
	
	/**
	 * Stop activating
	 */
	public void stop();	

	/**
	 * Supportive method gives true if the status is set to active
	 * @return
	 */
	public boolean isActive();

}