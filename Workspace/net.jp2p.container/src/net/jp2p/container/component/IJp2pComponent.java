/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.component;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;

public interface IJp2pComponent<T extends Object>{
	
	public enum ModuleProperties implements IJp2pProperties{
		STATUS,
		CREATE_DATE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		/**
		 * Returns true if the given property is valid for this enum
		 * @param prop
		 * @return
		 */
		public static boolean isValid( IJp2pProperties prop ){
			for( ModuleProperties mp: values() ){
				if( mp.name().equals( prop.name() ))
					return true;
			}
			return false;
		}
	}

	/**
	 * Get the id of the component
	 * @return
	 */
	public String getId();
	
	/**
	 * Get a String label for this component. This can be used for display options and 
	 * is not meant to identify the component;
	 * @return
	 */
	public String getComponentLabel();
	
	/**
	 * Get the property source of this component
	 * @return
	 */
	public IJp2pPropertySource<IJp2pProperties> getPropertySource();
	
	/**
	 * Get the module that is contained in the component
	 * @return
	 */
	public T getModule();
}