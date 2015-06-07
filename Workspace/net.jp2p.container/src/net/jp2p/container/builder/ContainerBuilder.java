/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import net.jp2p.container.IContainerFactory;
import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.factory.ContainerFactory;
import net.jp2p.container.factory.IComponentFactory;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pDirectives.DeveloperModes;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class ContainerBuilder implements IContainerBuilder<Object>{

	public static final String S_WRN_NOT_COMPLETE = "\n\t!!! The Service Container did not complete: ";

	private List<ICompositeBuilderListener<?>> factories;
	private IContainerFactory<Object> factory;
	private DeveloperModes mode;
	
	private Lock lock;
	
	public ContainerBuilder( DeveloperModes mode) {
		factories = new ArrayList<ICompositeBuilderListener<?>>();
		this.mode = mode;
		lock = new ReentrantLock();
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#addFactory(net.osgi.jp2p.factory.IComponentFactory)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean addFactory( IPropertySourceFactory factory ){
		lock.lock();
		try{
		  boolean retval = this.factories.add( factory );
		  if( factory instanceof ContainerFactory )
			  this.factory = (IContainerFactory<Object>) factory;
		  Collections.sort(this.factories, new FactoryComparator());
		  return retval;
		}
		finally{
			lock.unlock();
		}
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#removeFactory(net.osgi.jp2p.factory.IComponentFactory)
	 */
	@Override
	public boolean removeFactory( IPropertySourceFactory factory ){
		lock.lock();
		try{
			return this.factories.remove( factory );
		}
		finally{
			lock.unlock();
		}
	}
	
	@Override
	public IPropertySourceFactory[] getFactories(){
		return this.factories.toArray( new IPropertySourceFactory[0] );
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#getFactory(java.lang.String)
	 */
	@Override
	public IPropertySourceFactory getFactory( String name ){
		for( ICompositeBuilderListener<?> listener: factories ){
			IPropertySourceFactory factory = (IPropertySourceFactory) listener;
			if( factory.getComponentName().equals(name ))
					return factory;
		}
		return null;	
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#getFactory(net.osgi.jp2p.properties.IJp2pPropertySource)
	 */
	@Override
	public IPropertySourceFactory getFactory( IJp2pPropertySource<?> source ){
		for( ICompositeBuilderListener<?> listener: factories ){
			IPropertySourceFactory factory = (IPropertySourceFactory) listener;
			if( factory.getPropertySource().equals( source ))
					return factory;
		}
		return null;	
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#isCompleted()
	 */
	@Override
	public boolean isCompleted(){
		for( ICompositeBuilderListener<?> listener: factories ){
			IPropertySourceFactory factory = (IPropertySourceFactory) listener;
			if(( factory instanceof IComponentFactory<?> ) && !((ContainerBuilder) factory).isCompleted())
				return false;
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#listModulesNotCompleted()
	 */
	@Override
	public String listModulesNotCompleted(){
		StringBuffer buffer = new StringBuffer();
		for( ICompositeBuilderListener<?> listener: factories ){
			IComponentFactory<?> factory = (IComponentFactory<?>) listener;
			if( !factory.isCompleted())
				buffer.append( "\t\t\t"+ factory.getComponentName() + "\n" );
		}
		if( Utils.isNull( buffer.toString() ))
			return null;
		return S_WRN_NOT_COMPLETE + buffer.toString();
	}
	
	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#updateRequest(net.osgi.jp2p.factory.ComponentBuilderEvent)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public synchronized void updateRequest(ComponentBuilderEvent<?> event) {
		lock.lock();
		try{
			for( ICompositeBuilderListener<?> listener: this.factories ){
				if( !listener.equals( event.getFactory())){
					IPropertySourceFactory factory = (IPropertySourceFactory) listener;
					factory.notifyChange( (ComponentBuilderEvent<Object>) event);
				}
			}
		}
		finally{
			lock.unlock();
		}
	}	
	

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#addFactoryToContainer(java.lang.String, net.osgi.jp2p.properties.IJp2pPropertySource, boolean, boolean)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public IPropertySourceFactory addFactoryToContainer( String componentName, IJp2pPropertySource<IJp2pProperties> parentSource, boolean createSource, boolean create) {
		String str = StringStyler.styleToEnum(componentName);
		IJp2pWritePropertySource<IJp2pProperties> ncp = (IJp2pWritePropertySource<IJp2pProperties>) parentSource.getChild( str );
		if( ncp != null )
			return null;
		IPropertySourceFactory factory = ContainerFactory.getDefaultFactory( componentName );
		factory.prepare( parentSource, this, null);
		addFactory(factory);
		if(!createSource )
			return factory;
		IJp2pWritePropertySource<IJp2pProperties> props = (IJp2pWritePropertySource<IJp2pProperties>) factory.createPropertySource();
		props.setDirective( Directives.CREATE, Boolean.valueOf( create ).toString());
		parentSource.addChild(props);
		return factory; 
	}

	/* (non-Javadoc)
	 * @see net.osgi.jp2p.builder.IContainerBuilder#getOrCreateChildFactory(net.osgi.jp2p.properties.IJp2pPropertySource, java.lang.String, boolean, boolean)
	 */
	@Override
	public IPropertySourceFactory getOrCreateChildFactory( IJp2pPropertySource<IJp2pProperties> source, String componentName, boolean createSource, boolean create ){
		IJp2pPropertySource<?> child = source.getChild( componentName ); 
		if( child != null )
			return this.getFactory(child );
		return addFactoryToContainer(componentName, source, createSource, create);
	}

	@Override
	public IJp2pContainer<Object> createContainer() {
		return (IJp2pContainer<Object>) factory.createComponent();
	}
}

/**
 * This comparator compares the weights of the factories
 * @author Kees
 *
 */
class FactoryComparator<T extends Object> implements Comparator<ICompositeBuilderListener<T>>{

	@Override
	public int compare(ICompositeBuilderListener<T> arg0, ICompositeBuilderListener<T> arg1) {
		if(!(arg0 instanceof IComponentFactory<?>))
			return -1;
		if(!(arg1 instanceof IComponentFactory<?>))
			return 1;
		IComponentFactory<?> f0 = (IComponentFactory<?>) arg0;
		IComponentFactory<?> f1 = (IComponentFactory<?>) arg1;
		return f0.getWeight() - f1.getWeight();
	}
}