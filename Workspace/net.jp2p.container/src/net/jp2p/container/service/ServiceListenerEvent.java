/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.service;

import java.util.EventObject;

import net.jp2p.container.service.IServiceListener.ServiceRegistrationEvents;

public class ServiceListenerEvent<T extends Object> extends EventObject {
	private static final long serialVersionUID = 5928582157413151939L;
	
	private ServiceRegistrationEvents event;
	private T data;
	
	public ServiceListenerEvent( Object arg0, ServiceRegistrationEvents event, T data ) {
		super(arg0);
		this.event = event;
		this.data = data;
	}

	public ServiceRegistrationEvents getServiceListenerEvent() {
		return event;
	}

	public T getData() {
		return data;
	}	
}
