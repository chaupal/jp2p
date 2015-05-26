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

import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.utils.Utils;

public abstract class AbstractJp2pServiceBuilder implements IJp2pServiceBuilder {

	private Collection<IPropertySourceFactory> factories;
	private String name;
	
	protected AbstractJp2pServiceBuilder( String name ) {
		super();
		this.name = name;
		factories = new ArrayList<IPropertySourceFactory>();
		this.prepare();
	}

	/**
	 * Prepare the factory
	 */
	protected abstract void prepare();
	
	@Override
	public String getName() {
		return name;
	}

	protected void addFactory( IPropertySourceFactory factory ){
		factories.add( factory );
	}

	protected void removeFactory( IPropertySourceFactory factory ){
		factories.remove( factory );
	}

	/**
	 * Get the supported services
	 */
	@Override
	public Jp2pServiceDescriptor[] getSupportedServices() {
		Collection<Jp2pServiceDescriptor> descriptors = new ArrayList<Jp2pServiceDescriptor>();
		for( IPropertySourceFactory factory: this.factories ){
			Jp2pServiceDescriptor descriptor = new Jp2pServiceDescriptor( factory.getComponentName()); 
			descriptor.setContext( this.getName() );
			descriptors.add( descriptor);
		}
		return descriptors.toArray( new Jp2pServiceDescriptor[ descriptors.size()]);
	}
	
	/**
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	@Override
	public boolean hasFactory( Jp2pServiceDescriptor descriptor ){
		for( Jp2pServiceDescriptor desc: getSupportedServices() ){
			if( desc.equals( descriptor ))
				return true;
		}
		return false;
	}

	
	@Override
	public int compareTo(IJp2pServiceBuilder o) {
		return name.compareTo( o.getName());
	}

	@Override
	public IPropertySourceFactory getFactory( Jp2pServiceDescriptor descriptor ) {
		for( IPropertySourceFactory factory: this.factories ){
			if( factory.getComponentName().equals( descriptor.getName() )){
				ClassLoader loader = factory.getClass().getClassLoader();
				String name = factory.getClass().getName();
				try{
					return (IPropertySourceFactory) loader.loadClass(name).newInstance();
				} catch ( Exception e) {
				    e.printStackTrace();
				}
				return factory;
			}
		}
		return null;
	}

	@Override
	public String printFactories() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("JP2P Service Builder: " + this.name + "\n" );
		for( IPropertySourceFactory factory: this.factories ){
			buffer.append( "\t" + factory.getComponentName() + ": " + factory.canCreate() + "\n");
		}
		return buffer.toString();
	}
	
	@Override
	public String toString() {
		return this.name + ":" + super.toString();
	}

	/**
	 * Returns true if the lowercases of the Strings are equAL
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean isContextNameEqual( String str1, String str2 ){
		if( Utils.isNull(str1 ) && !Utils.isNull(str2))
			return false;
		if( Utils.isNull(str2 ) && !Utils.isNull(str1))
			return false;
		if( Utils.isNull(str1 ) && Utils.isNull(str2))
			return false;
		return str1.toLowerCase().equals( str2.toLowerCase());				
	}
}
