/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxta.platform;

import net.jp2p.chaupal.context.Jp2pContextService;
import net.jp2p.chaupal.jxta.module.ModuleFactoryRegistrator;
import net.jp2p.container.context.ContextLoader;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	public static final String BUNDLE_ID = "net.jp2p.chaupal.jxta.platform";
	
	private static Activator plugin;
	
	private static Jp2pLogService logService;
	private static ModuleFactoryRegistrator mfr;

	private static Jp2pContextService contextService;
	private static ContextLoader loader;
		
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		
		mfr = new ModuleFactoryRegistrator(context);

		loader = ContextLoader.getInstance();
		contextService = new Jp2pContextService( loader, context );
		contextService.open();
		
		logService = new Jp2pLogService( context );
		logService.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {		

		contextService.close();
		contextService = null;
		loader = null;
		
		mfr.unregister();
		mfr = null;

		logService.close();
		logService = null;
		plugin = null;
	}	
	
	public static LogService getLog(){
		return logService.getLogService();
	}
	
	public static Activator getDefault(){
		return plugin;
	}

	public static ContextLoader getLoader() {
		return loader;
	}

	public static ModuleFactoryRegistrator getModuleFactoryRegistrator(){
		return mfr;
	}
}
