/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import net.jp2p.container.Jp2pContainerPropertySource;
import net.jp2p.container.activator.IActivator;
import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.component.Jp2pComponentNode;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public class Jp2pContainer<T extends Object> extends Jp2pComponentNode<T> 
implements	IJp2pContainer<T>{

	private static final String S_CONTAINER = "Container Structure for: ";
	
	public static enum ServiceChange{
		CHILD_ADDED,
		CHILD_REMOVED,
		PEERGROUP_ADDED,
		PEERGROUP_REMOVED,
		STATUS_CHANGE,
		COMPONENT_EVENT;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	protected Jp2pContainer( String bundleId, String identifier) {
		this( new Jp2pContainerPropertySource( bundleId, identifier ));
	}

	protected Jp2pContainer( IJp2pWritePropertySource<IJp2pProperties> source ) {
		super( source, null );
		super.setRoot(true);
	}

	/**
	 * Get the dispatcher for this container
	 * @return
	 */
	@Override
	public ComponentEventDispatcher getDispatcher(){
		return super.getDispatcher();
	}
	
	public void clear(){
		super.clear();
	}
	
	@Override
	public String getIdentifier() {
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		return source.getDirective( Directives.NAME );
	}
	
	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	public String getCategory( Object key ){
		IJp2pPropertySource<IJp2pProperties> source = super.getPropertySource();
		return source.getCategory( (IJp2pProperties) key );
	}

	public void deactivate() {
		for( IJp2pComponent<?> component: super.getChildren() ){
			if( component instanceof IActivator ){
				IActivator service = (IActivator )component;
				service.stop();
			}
		}
	}	

	/**
	 * Remove the given module from the container
	 * @param container
	 * @param module
	 */
	protected static void removeModule( Jp2pContainer<?> container, Object module ){
		for( IJp2pComponent<?> component: container.getChildren() ){
			if( component.getModule().equals( module ))
				container.removeChild(component);
		}
	}
	
	/**
	 * Find the component with the given name 
	 * @param componentName
	 * @param root
	 * @return
	 */
	public static IJp2pComponent<?> findComponent( String componentName, IJp2pComponent<?> root ){
		if( Utils.isNull( componentName ))
			return null;
		if( componentName.equals( root.getPropertySource().getComponentName()))
			return root;
		if(!( root instanceof IJp2pComponentNode ))
			return null;
		IJp2pComponentNode<?> node = ( IJp2pComponentNode<?>) root;
		IJp2pComponentNode<?> result;
		for( IJp2pComponent<?> child: node.getChildren() ){
			result = (IJp2pComponentNode<?>) findComponent(componentName, child);
			if( result != null )
				return result;
		}
		return null;
	}
	
	public static final String printContainerStructure( IJp2pContainer<?> container ){
		StringBuffer buffer = new StringBuffer();
		buffer.append( S_CONTAINER + container.getId() );
		for( IJp2pComponent<?> child: container.getChildren() )
			buffer.append(printComponentStructure( 1, child ));
		return buffer.toString();
	}

	/**
	 * Print the component structure
	 * @param index
	 * @param component
	 * @return
	 */
	protected static final String printComponentStructure( int index, IJp2pComponent<?> component ){
		StringBuffer buffer = new StringBuffer();
		for( int i=0; i<index; i++ )
			buffer.append("\t");
		buffer.append( component.getComponentLabel() + "\n" );
		if(!( component instanceof IJp2pComponentNode ))
			return buffer.toString();
		IJp2pComponentNode<?> node = (IJp2pComponentNode<?>) component;
		for( IJp2pComponent<?> child: node.getChildren() )
			buffer.append(printComponentStructure( ++index, child ));
		return buffer.toString();
	}

}