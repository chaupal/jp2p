/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform.module;

import java.util.Collection;
import java.util.HashSet;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.context.ContextRegistrator;
import net.jp2p.chaupal.jxta.platform.context.JxtaNetworkContext;
import net.jp2p.chaupal.module.AbstractService;
import net.jp2p.container.context.IJp2pContext;
import net.jp2p.jxta.context.IJxtaContext;
import net.jp2p.jxta.context.JxtaContextUtils;
import net.jxta.peergroup.IModuleDefinitions.DefaultModules;
import net.jxta.platform.ModuleClassID;

public class JxtaContextService extends AbstractService<IJp2pContext> {
	
	//in component.xml file you will use target="(jp2p.context=contextName). The wildcard will collect all contexts"
	private static String filter = "(jp2p.context=*)"; 

	private Collection<ModuleClassID> loadedModules;
	private ContextRegistrator cr;

	public JxtaContextService(BundleContext bc) {
		super(bc, IJp2pContext.class, filter);
		this.cr = new ContextRegistrator( bc );
		loadedModules = new HashSet<ModuleClassID>();
	}

	@Override
	protected void onDataRegistered(IJp2pContext service) {
		ModuleClassID[] classIds = JxtaContextUtils.getLoadedModuleClassIDs( service );	
		if(( classIds == null ) || (!( service instanceof IJxtaContext ))) 
			return;
		IJxtaContext context = (IJxtaContext) service;
		for( ModuleClassID module: context.getSupportedModuleClassIDs()){
			loadedModules.add(module);
		}
		int amount = DefaultModules.values().length;
		if( loadedModules.size() < amount )
			return;
		cr.register( new JxtaNetworkContext() );	
	}

	@Override
	protected void onDataUnRegistered(IJp2pContext service) {
		ModuleClassID[] classIds = JxtaContextUtils.getLoadedModuleClassIDs( service );	
		if(( classIds == null ) || (!( service instanceof IJxtaContext ))) 
			return;
		IJxtaContext context = (IJxtaContext) service;
		for( ModuleClassID module: context.getSupportedModuleClassIDs()){
			loadedModules.remove(module);
		}
		int amount = DefaultModules.values().length;
		if( loadedModules.size() >= amount )
			return;
		cr.unregister();	
	}
}
