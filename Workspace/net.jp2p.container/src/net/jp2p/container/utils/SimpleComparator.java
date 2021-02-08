/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.utils;

import java.util.Comparator;

public class SimpleComparator<T extends Object> implements Comparator<T> {

	@Override
	public int compare(Object arg0, Object arg1) {
		if(( arg0 == null ) && ( arg1 == null ))
			return 0;
		if(( arg0 != null ) && ( arg1 == null ))
			return 1;
		if(( arg0 == null ) && ( arg1 != null ))
			return 1;
		return arg0.toString().compareTo(arg1.toString());
	}

}
