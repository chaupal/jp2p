/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import net.jp2p.container.utils.StringStyler;

public interface IJp2pProperties{

	/**
	 * Directives give additional clues on how to create the component
	 * @author Kees
	 *
	 */
	public enum Jp2pProperties implements IJp2pProperties{
		
		STATUS,
		BUNDLE_ID,
		CONTEXT,
		CREATE_DATE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		/**
		 * Returns true if the given property belonf]gs to this enum
		 * @param str
		 * @return
		 */
		public static boolean isProperty( String str ){
			str = StringStyler.styleToEnum(str);
			if(( str == null ) || ( str.length() == 0 ))
				return false;
			for( Jp2pProperties comp: values()){
				if( comp.name().equals( str.toUpperCase() ))
					return true;
			}
			return false;
		}
		
		
	}

	public String name();	
}
