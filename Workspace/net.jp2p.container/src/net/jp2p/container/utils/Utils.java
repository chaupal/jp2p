/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

import java.util.Arrays;
import java.util.Collection;

public class Utils
{

	/**
	 * returns true if the string is null or empty
	 * @param str
	 * @return
	 */
	public static final boolean isNull( String str ){
		return (( str == null) || ( str.trim().length() == 0 ));
	}
	
	/**
	 * add the given array to the collection
	 * @param col
	 * @param add
	 */
	public static void addArray( Collection<Object> col, Object[] add ){
		if(( add == null ) || ( add.length == 0 ))
			return;
		col.addAll( Arrays.asList( add ));
	}
	
}
