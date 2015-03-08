/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.utils.StringStyler;

public interface IJp2pDirectives{

	/**
	 * Directives give additional clues on how to create the component
	 * @author Kees
	 *
	 */
	public enum Directives implements IJp2pDirectives{
		XMLNS_1XS,
		SCHEMA_LOCATION,
		ID,
		NAME,
		ENABLED,
		CLASS,
		TYPE,
		FILE,
		VISIBLE,
		CONTEXT,
		AUTO_START,
		CLEAR,
		VERSION,
		CREATE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isDirective( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( Directives comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
	}

	/**
	 * The types of services that are available
	 * @author Kees
	 *
	 */
	public enum Contexts{
		JXTA,
		PLATFORM,
		JP2P,
		CHAUPAL;

		public static boolean isValidContext( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( Contexts context: values()){
				if( context.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public String name();	
}
