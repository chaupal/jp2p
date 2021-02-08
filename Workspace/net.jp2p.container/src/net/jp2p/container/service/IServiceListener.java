/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.service;

public interface IServiceListener<T extends Object> {

	public enum ServiceRegistrationEvents{
		REGISTERED,
		UNREGISTERED;
	}
	
	/**
	 * respond to a module service changed event
	 * @param event
	 */
	public void notifyModuleServiceChanged( ServiceListenerEvent<T> event );
}
