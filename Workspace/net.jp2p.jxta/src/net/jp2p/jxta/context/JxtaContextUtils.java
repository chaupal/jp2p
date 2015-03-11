/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.context;

import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jxta.peergroup.core.ModuleClassID;

public class JxtaContextUtils {

	public static ModuleClassID[] getLoadedModuleClassIDs( IJp2pServiceBuilder context ) {
		if( context instanceof IJxtaBuilder){
			IJxtaBuilder jc = (IJxtaBuilder) context;
			return jc.getSupportedModuleClassIDs();
		}
		return null;
	}
}
