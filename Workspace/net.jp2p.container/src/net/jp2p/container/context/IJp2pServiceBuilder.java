/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import net.jp2p.container.factory.IJp2pComponents;
import net.jp2p.container.factory.IPropertySourceFactory;
import net.jp2p.container.properties.IJp2pDirectives;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IPropertyConvertor;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.xml.IJp2pHandler;

public interface IJp2pServiceBuilder extends Comparable<IJp2pServiceBuilder>{

	/**
	 * Directives give additional clues on how to create the component
	 * @author Kees
	 *
	 */
	public enum ContextDirectives implements IJp2pDirectives{
		NAME,
		CLASS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isDirective( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( ContextDirectives comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}

	public static enum Components implements IJp2pComponents{
		JP2P_CONTAINER,
		CONTEXT,
		STARTUP_SERVICE,
		PERSISTENCE_SERVICE,
		LOGGER_SERVICE;
	
		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
		
		public static boolean isComponent( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( Components comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}

	//The name of the context
	public String getName();
	
	/**
	 * Get the names of the services that are available
	 * @return
	 */
	public String[] getSupportedServices();
	
	/**
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	public boolean hasFactory( String componentName );

	//Get the factory that is created
	public IPropertySourceFactory getFactory( String componentName);
	
	/**
	 * Get the handler for this context
	 * @return
	 */
	public IJp2pHandler getHandler();
	
	/**
	 * Get the default factory for this container
	 * @param parent
	 * @param componentName
	 * @return
	 */
	public IPropertyConvertor<String, Object> getConvertor( IJp2pPropertySource<IJp2pProperties> source );

	/**
	 * Create a value for the given component name and id
	 * @param parent
	 * @param componentName
	 * @return
	 */
	public Object createValue( String componentName, IJp2pProperties id );
	
	/**
	 * Print the supported factories, and whether they are available
	 * @return
	 */
	public String printFactories();
}
