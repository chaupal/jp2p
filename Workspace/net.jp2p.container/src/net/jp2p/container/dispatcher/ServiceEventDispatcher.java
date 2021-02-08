/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.dispatcher;

import java.util.ArrayList;
import java.util.Collection;

public class ServiceEventDispatcher {

	private Collection<IServiceChangedListener> listeners;

	private static ServiceEventDispatcher dispatcher = new ServiceEventDispatcher();
	
	private ServiceEventDispatcher() {
		this.listeners = new ArrayList<IServiceChangedListener>();
	}
	
	public static ServiceEventDispatcher getInstance(){
		return dispatcher;
	}

	public void addServiceChangeListener( IServiceChangedListener listener ){
		this.listeners.add( listener );
	}

	public void removeServiceChangeListener( IServiceChangedListener listener ){
		this.listeners.remove( listener );
	}
	
	public void serviceChanged( ServiceChangedEvent event ){
		for( IServiceChangedListener listener: this.listeners )
			listener.notifyServiceChanged(event);
	}

}
