/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jp2p.container.context.ContextLoaderEvent.LoaderEvent;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.Utils;

public class ContextLoader {

	private static final String S_CONTEXTS_AVAILABLE = "The following JP2P contexts are available";
	
	private Collection<IJp2pContext> contexts;
	
	private static ContextLoader contextLoader = new ContextLoader();

	private Collection<IContextLoaderListener> listeners;
	
	private Lock lock = new ReentrantLock();
	
	private ContextLoader() {
		contexts = new TreeSet<IJp2pContext>();
		this.listeners = new ArrayList<IContextLoaderListener>();
	}

	public static ContextLoader getInstance(){
		return contextLoader;
	}

	public void clear(){
		contexts.clear();
	}

	public synchronized void addContextLoaderListener( IContextLoaderListener listener ){
		lock.lock();
		try{
			this.listeners.add( listener );
		}
		finally{
			lock.unlock();
		}
	}

	public synchronized void removeContextLoaderListener( IContextLoaderListener listener ){
		lock.lock();
		try{
			this.listeners.remove( listener );
		}
		finally{
			lock.unlock();
		}
	}

	private synchronized final void notifyContextChanged( ContextLoaderEvent event ){
		lock.lock();
		try{
			for( IContextLoaderListener listener: listeners){
				switch( event.getType() ){
				case REGISTERED:
					listener.notifyRegisterContext(event);
					break;
				default:
					listener.notifyUnregisterContext(event);
					break;
				}
			}
		}
		finally{
			lock.unlock();
		}
	}

	public void addContext( IJp2pContext context ){
		this.contexts.add( context );
		notifyContextChanged( new ContextLoaderEvent( this, LoaderEvent.REGISTERED, context ));		
	}

	public void removeContext( IJp2pContext context ){
		this.contexts.remove( context );
		notifyContextChanged( new ContextLoaderEvent( this, LoaderEvent.UNREGISTERED, context ));		
	}
	
	/**
	 * get the contexts
	 * @return
	 */
	public IJp2pContext[] getContexts(){
		return this.contexts.toArray( new IJp2pContext[ this.contexts.size() ]);
	}
	
	/**
	 * Get the context for the given name
	 * @param contextName
	 * @return
	 */
	public IJp2pContext getContext( String contextName ){
		for( IJp2pContext context: this.contexts ){
			if( Utils.isNull( contextName ))
				continue;
			if( context.getName().toLowerCase().equals( contextName.toLowerCase() ))
				return context;
		}
		return null;
	}

	/**
	 * Get the context for the given componentname
	 * @param contextName
	 * @return
	 */
	public synchronized IJp2pContext getContextForComponent( String contextName, String componentName ){
		if( Utils.isNull( componentName ))
			return new Jp2pContext();
		
		for( IJp2pContext context: this.contexts ){
			if(context.isValidComponentName(contextName, componentName))
				return context;
		}
		return new Jp2pContext();
	}

	/**
	 * Get the convertor for the given property source
	 * @param contextName
	 * @return
	 */
	public IPropertyConvertor<String,Object> getConvertor( IJp2pPropertySource<IJp2pProperties> source ){
		for( IJp2pContext context: this.contexts ){
			IPropertyConvertor<String,Object> convertor = context.getConvertor( source);
			if(convertor != null )
				return convertor;
		}
		return null;
	}

	/**
	 * Get the context for the given componentname
	 * @param contextName
	 * @return
	 */
	public boolean isLoadedComponent( String contextName, String componentName ){
		if( Utils.isNull( componentName ))
			return false;
		
		for( IJp2pContext context: this.contexts ){
			if(context.isValidComponentName(contextName, componentName))
				return true;
		}
		return false;
	}

	/**
	 * Print the available contexts
	 * @return
	 */
	public String printContexts(){
		StringBuffer buffer = new StringBuffer();
		buffer.append( S_CONTEXTS_AVAILABLE + "\n");
		for( IJp2pContext context : contexts ){
			buffer.append( "\t" + context.getName() + "\n" );
		}
		return buffer.toString();
	}

	/**
	 * Load a context from the given directives
	 * @param name
	 * @param className
	 * @return
	*/
	public static IJp2pContext loadContext( Object source, String className ){
		if( Utils.isNull( className ))
			return null;
		Class<?> clss;
		IJp2pContext context = null;
		try {
			clss = source.getClass().getClassLoader().loadClass( className );
			context = (IJp2pContext) clss.newInstance();
			System.out.println("URL found: " + ( clss != null ));
		}
		catch ( Exception e1) {
			e1.printStackTrace();
		}
		return context;
	}
}