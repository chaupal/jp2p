/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.utils;

import java.util.Comparator;

import net.jxta.platform.Module;
import net.jxta.refplatform.platform.NetworkConfigurator;
import net.jxta.refplatform.platform.NetworkManager;

public class JxseModuleComparator<T extends Object> implements
		Comparator<T> {

	@Override
	public int compare(T arg0, T arg1) {
		return getIndex( arg0 ) - getIndex( arg1 );
	}

	/**
	 * Create an index for the various modules
	 * @param obj
	 * @return
	 */
	public static int getIndex( Object obj  ){
		int index = 0;
		if( obj instanceof NetworkManager )
			return index;
		index++;
		if( obj instanceof NetworkConfigurator )
			return index;
		index++;
		if( obj instanceof Module )
			return index;
		return ++index;
	}
}
