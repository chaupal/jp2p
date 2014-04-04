/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

public class EnumUtils {

	/**
	 * Return a String representation of the given objects. Is used to
	 * quickly modify enums
	 * @param objects
	 * @return
	 */
	public static String[] toString( Object[] objects ){
		String[] results = new String[objects.length ];
		for( int i=0; i<objects.length; i++ )
			results[i] = objects[i].toString();
		return results;
	}
	
	//public String[] getValuesAsString( Enum enm ){
	//	for( )
	//}

}
