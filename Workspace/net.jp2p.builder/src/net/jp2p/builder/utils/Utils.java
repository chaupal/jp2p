/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.utils;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.properties.IJp2pDirectives.Directives;
import net.jp2p.container.properties.IJp2pPropertySource;

public class Utils
{

	public static boolean isEmpty( String str ) {
		return ( str == null ) || ( str.length() == 0);
	}
	
	/**
	 * Get the label of a JP2P service component
	 * @param component
	 * @return
	 */
	public static String getLabel( IJp2pComponent<?> component) {
		if( component instanceof IJp2pContainer ){
			IJp2pContainer<?> container = (IJp2pContainer<?> )component;
			return container.getIdentifier();			
		}
		if(( component == null ) || ( component.getPropertySource() == null ))
			return "NULL";
		if( !Utils.isEmpty( component.getComponentLabel() ))
			return component.getComponentLabel();
		if( component.getModule() == null )
			return component.getClass().getSimpleName();
		return component.getModule().getClass().getSimpleName();
	}

	/**
	 * Get the label of a JP2P service component
	 * @param component
	 * @return
	 */
	public static String getLabel( IJp2pPropertySource<?> source ) {
		String label = source.getDirective( Directives.NAME );
		if(( label == null ) || ( label.length() == 0 )){
			return source.getComponentName();			
		}
		return label;
	}
}
