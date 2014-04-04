/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import java.util.ArrayList;
import java.util.Collection;

public class ComponentEventDispatcher {

	private Collection<IComponentChangedListener> listeners;

	private static ComponentEventDispatcher dispatcher = new ComponentEventDispatcher();
	
	private ComponentEventDispatcher() {
		this.listeners = new ArrayList<IComponentChangedListener>();
	}
	
	public static ComponentEventDispatcher getInstance(){
		return dispatcher;
	}

	public void addServiceChangeListener( IComponentChangedListener listener ){
		this.listeners.add( listener );
	}

	public void removeServiceChangeListener( IComponentChangedListener listener ){
		this.listeners.remove( listener );
	}
	
	public synchronized void serviceChanged( ComponentChangedEvent event ){
		for( IComponentChangedListener listener: this.listeners )
			listener.notifyServiceChanged(event);
	}

}
