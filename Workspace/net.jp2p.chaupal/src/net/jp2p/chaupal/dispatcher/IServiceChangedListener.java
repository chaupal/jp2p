/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.dispatcher;

import net.jp2p.container.utils.StringStyler;

public interface IServiceChangedListener {

	public enum ServiceChange{
		CHILD_ADDED,
		CHILD_REMOVED,
		STATUS_CHANGE,
		COMPONENT_EVENT,
		REFRESH;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	public void notifyServiceChanged( ServiceChangedEvent event );
}
