/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.context;

import java.util.Collection;
import java.util.HashSet;

import net.jp2p.container.context.ContextLoader;
import net.jp2p.container.context.IJp2pContext;
import net.jxta.peergroup.core.ModuleClassID;
import net.jxta.peergroup.core.ModuleSpecID;

public class JxtaContextUtils {

	public static ModuleClassID[] getLoadedModuleClassIDs( IJp2pContext context ) {
		if( context instanceof IJxtaContext){
			IJxtaContext jc = (IJxtaContext) context;
			return jc.getSupportedModuleClassIDs();
		}
		return null;
	}

	public static ModuleSpecID[] getLoadedModuleClassIDs( ContextLoader loader ) {
		Collection<ModuleClassID> ids = new HashSet<ModuleClassID>();
		for( IJp2pContext context: loader.getContexts() ){
			if( context instanceof IJxtaContext){
				IJxtaContext jc = (IJxtaContext) context;
				for( ModuleClassID id: jc.getSupportedModuleClassIDs()){
					ids.add( id );
				}
			}
		}
		return ids.toArray( new ModuleSpecID[ ids.size()]);
	}

}
