/*******************************************************************************
 * Copyright (c) 2013 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.chaupal.jxta.activator;

import org.osgi.framework.BundleContext;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.chaupal.jxta.module.ModuleFactoryService;

public class Jp2pJxseBundleActivator extends Jp2pBundleActivator {

	private static ModuleFactoryService moduleService; 
	
	
	public Jp2pJxseBundleActivator(String bundle_id) {
		super(  bundle_id );
	}


	@Override
	public void start(BundleContext bundleContext) throws Exception {
		moduleService = new ModuleFactoryService( bundleContext );
		moduleService.open();			
		super.start(bundleContext);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		if( moduleService != null ){
		    moduleService.close();
		    moduleService = null;
		}
		super.stop(bundleContext);
	}
}