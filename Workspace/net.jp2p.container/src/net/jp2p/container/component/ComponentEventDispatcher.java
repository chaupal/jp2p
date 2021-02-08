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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ComponentEventDispatcher {

	private Collection<IComponentChangedListener<?>> listeners;
	
	private static ComponentEventDispatcher dispatcher = new ComponentEventDispatcher();
	
	private Lock lock;
	
	private ComponentEventDispatcher() {
		this.listeners = new ArrayList<IComponentChangedListener<?>>();
		lock = new ReentrantLock();
	}
	
	public static ComponentEventDispatcher getInstance(){
		return dispatcher;
	}

	public void addServiceChangeListener( IComponentChangedListener<?> listener ){
		lock.lock();
		try{
			this.listeners.add( listener );
		}
		finally{
			lock.unlock();
		}
	}

	public void removeServiceChangeListener( IComponentChangedListener<?> listener ){
		lock.lock();
		try{
			this.listeners.remove( listener );
		}
		finally{
			lock.unlock();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public synchronized void serviceChanged( ComponentChangedEvent<?> event ){
		lock.lock();
		try{
			for( IComponentChangedListener listener: this.listeners )
				listener.notifyServiceChanged(event);
		}
		finally{
			lock.unlock();
		}
	}

}
