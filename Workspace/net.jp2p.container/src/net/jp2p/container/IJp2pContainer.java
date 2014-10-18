/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container;

import net.jp2p.container.component.ComponentEventDispatcher;
import net.jp2p.container.component.IJp2pComponentNode;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.utils.StringStyler;

public interface IJp2pContainer<T extends Object> extends IJp2pComponentNode<T> {

	/**
	 * The properties supported by the container
	 * @author Kees
	 *
	 */
	public enum ContainerProperties implements IJp2pProperties{
		HOME_FOLDER,
		PERSIST,
		PASS_1,
		PASS_2;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
		
		/**
		 * Returns true if the given key is valid for this enum
		 * @param key
		 * @return
		 */
		public static boolean isValidKey( IJp2pProperties key ){
			if( key instanceof ContainerProperties )
				return true;
			for( ContainerProperties sp: ContainerProperties.values()){
				if( sp.name().equals(key.name()))
					return true;
			}
			return false;
		}
	}

	/**
	 * 
	 * The options for persistence
	 * @author Kees
	 *
	 */
	public enum PersistenceOptions{
		JAVA,
		OSGI;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	/**
	 * The identifier of the container
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Get the dispatcher for this container
	 * @return
	 */
	public ComponentEventDispatcher getDispatcher();
	
	/**
	 * Fotrece deactivation of the child nodes
	 */
	public void deactivate();
}