/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

import java.util.EventObject;

import net.jp2p.container.activator.IActivator.Status;

public class ActivatorEvent extends EventObject {

	private static final long serialVersionUID = 302931451825865288L;

	private Status previous;
	
	public ActivatorEvent( IActivator activator, Status previous ) {
		super(activator);
		this.previous = previous;
	}

	public Status getStatus(){
		IActivator activator = ( IActivator )super.getSource(); 
		return activator.getStatus();
	}
	
	public Status getPreviousStaTus(){
		return previous;
	}
}
