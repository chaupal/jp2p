/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import net.jp2p.container.context.IJp2pContext;
import net.jp2p.container.context.Jp2pContext;
import net.jp2p.container.utils.StringStyler;
import net.jp2p.container.utils.Utils;

public abstract class AbstractJp2pContext implements IJp2pContext {

	/**
	 * Returns true if the given component name is valid for this context
	 * @param componentName
	 * @return
	 */
	@Override
	public boolean isValidComponentName( String contextName, String componentName ){
		if( !Utils.isNull( contextName ) && !Jp2pContext.isContextNameEqual( this.getName(), contextName ))
			return false;
		String compName = StringStyler.styleToEnum( componentName );
		compName = StringStyler.prettyString( compName );
		for( String name: getSupportedServices() ){
			if( name.equals( compName ))
				return true;
		}
		return false;
	}
}
