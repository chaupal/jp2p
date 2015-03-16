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

import net.jp2p.container.context.Jp2pLoaderEvent.LoaderEvent;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.Utils;

public class Jp2pServiceLoader{

	private static final String S_CONTEXTS_AVAILABLE = "The following JP2P builders are available";
	
	private Collection<IJp2pServiceBuilder> builders;
	
	private static Jp2pServiceLoader loader = new Jp2pServiceLoader();

	private Collection<IContextLoaderListener> listeners;
	
	private Lock lock = new ReentrantLock();
	
	private Jp2pServiceLoader() {
		builders = new TreeSet<IJp2pServiceBuilder>();
		this.listeners = new ArrayList<IContextLoaderListener>();
	}

	public static Jp2pServiceLoader getInstance(){
		return loader;
	}

	public void clear(){
		builders.clear();
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

	private synchronized final void notifyContextChanged( Jp2pLoaderEvent event ){
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

	public void addBuilder( IJp2pServiceBuilder builder ){
		this.builders.add( builder );
		notifyContextChanged( new Jp2pLoaderEvent( this, LoaderEvent.REGISTERED, builder ));		
	}

	public void removeBuilder( IJp2pServiceBuilder builder ){
		this.builders.remove( builder );
		notifyContextChanged( new Jp2pLoaderEvent( this, LoaderEvent.UNREGISTERED, builder ));		
	}
	
	/**
	 * Get the context for the given name
	 * @param name
	 * @return
	 */
	protected IJp2pServiceBuilder getBuilder( String name ){
		for( IJp2pServiceBuilder context: this.builders ){
			if( Utils.isNull( name ))
				continue;
			if( context.getName().toLowerCase().equals( name.toLowerCase() ))
				return context;
		}
		return null;
	}

	/**
	 * Returns true if the loader supports a factory with the given name
	 * @param componentName
	 * @return
	 */
	public boolean hasFactory( Jp2pServiceDescriptor descriptor ){
		for( IJp2pServiceBuilder builder: this.builders ){
			if( builder.hasFactory( descriptor ))
				return true;
		}
		return false;
	}
	
	/**
	 * Get the context for the given componentname
	 * @param contextName
	 * @return
	 */
	/*
	public synchronized IPropertyConvertor<String, Object> getConvertor( String contextName, IJp2pPropertySource<IJp2pProperties> source ){
		String componentName = source.getComponentName();
		if( Utils.isNull( componentName ))
			return null;
		
		//for( IJp2pServiceBuilder builder: this.builders ){
		//	if( builder.getName().equals( contextName ) && builder.hasFactory( componentName))
		//		return builder.getConvertor(source);
		//}
		return null;
	}
	*/

	/**
	 * Get the convertor for the given property source
	 * @param contextName
	 * @return
	 */
	public IPropertyConvertor<String,Object> getConvertor( IJp2pPropertySource<IJp2pProperties> source ){
		String contextName = source.getDirective( Directives.CONTEXT );
		for( IJp2pServiceBuilder builder: this.builders ){
			if( !builder.getName().equals( contextName ))
				continue;
			IPropertyConvertor<String,Object> convertor = builder.getConvertor( source);
			if(convertor != null )
				return convertor;
		}
		return null;
	}

	/**
	 * Print the available contexts
	 * @return
	 */
	public String printContexts(){
		StringBuffer buffer = new StringBuffer();
		buffer.append( S_CONTEXTS_AVAILABLE + "\n");
		for( IJp2pServiceBuilder builder : builders ){
			buffer.append( builder.printFactories() );
		}
		return buffer.toString();
	}

	/**
	 * Get the context, or try to load it if none was found
	 * @param source
	 * @return
	 */
	public IPropertySourceFactory getFactory( String contextName, String componentName ){
		if( Utils.isNull( contextName ))
		return null;
		
		IJp2pServiceBuilder builder = getBuilder(contextName);
		return builder.getFactory(componentName);
	}

	/**
	 * Load a context from the given directives
	 * @param name
	 * @param className
	 * @return
	*/
	public static IJp2pServiceBuilder loadContext( Object source, String className ){
		if( Utils.isNull( className ))
			return null;
		Class<?> clss;
		IJp2pServiceBuilder context = null;
		try {
			clss = source.getClass().getClassLoader().loadClass( className );
			context = (IJp2pServiceBuilder) clss.newInstance();
			System.out.println("URL found: " + ( clss != null ));
		}
		catch ( Exception e1) {
			e1.printStackTrace();
		}
		return context;
	}
}