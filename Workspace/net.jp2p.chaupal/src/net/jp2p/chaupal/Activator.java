/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	public static final String BUNDLE_ID = "net.jp2p.chaupal";
	
	private static Activator plugin;
	private static Jp2pLogService logService;
	
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;		

		logService = new Jp2pLogService( context );
		logService.open();		
	}

	@Override
	public void stop(BundleContext context) throws Exception {		
		plugin = null;

		if( logService != null ){
			logService.close();
			logService = null;
		}
	}	
	
	public static Activator getDefault(){
		return plugin;
	}

	public static LogService getLog(){
		return logService.getLogService();
	}
}
