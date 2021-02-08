/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

import net.jp2p.container.factory.ComponentBuilderEvent;
import net.jp2p.container.utils.StringStyler;

public interface ICompositeBuilderListener<T extends Object> {

	public enum BuilderEvents{
		PROPERTY_SOURCE_PREPARED,
		PROPERTY_SOURCE_CREATED,
		COMPONENT_PREPARED,
		COMPONENT_CREATED,
		COMPONENT_STARTED,
		FACTORY_COMPLETED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	public void notifyChange( ComponentBuilderEvent event );
}
