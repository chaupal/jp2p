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
import org.osgi.service.log.LogService;

import net.jp2p.chaupal.activator.Jp2pBundleActivator;
import net.jp2p.chaupal.jxta.Activator;
import net.jp2p.chaupal.module.AbstractService;
import net.jxse.module.IJxtaModuleService;
import net.jxta.impl.loader.DynamicJxtaLoader;
import net.jxta.platform.Module;

public class Jp2pJxseBundleActivator extends Jp2pBundleActivator {

	private static ModuleLoaderService moduleService; 
	
	
	public Jp2pJxseBundleActivator(String bundle_id) {
		super(  bundle_id );
	}


	@Override
	public void start(BundleContext bundleContext) throws Exception {
		moduleService = new ModuleLoaderService( bundleContext );
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

	/**
	 * <p>
	 * Wrapper class which listens for all framework services of
	 * class ImoduleFactory. Each such service is picked
	 * up and installed into the module container
	 * </p>
	 * <p>
	 * <p>
	 * The alias used for the servlet/resource is taken from the 
	 * PROP_ALIAS service property.
	 * </p>
	 * <p>
	 * The resource dir used for contexts is taken from the 
	 * PROP_DIR service property.
	 * </p>
	 */
	private static class ModuleLoaderService extends AbstractService<IJxtaModuleService<Module>> {

	    private static final String filter = "(objectclass=" + IJxtaModuleService.class.getName() + ")";

		private DynamicJxtaLoader loader = DynamicJxtaLoader.getInstance();
		
		public ModuleLoaderService(BundleContext bc) {
			super( bc, IJxtaModuleService.class, filter );
		}

		@Override
		protected void onDataRegistered(IJxtaModuleService<Module> module) {
			loader.addModuleService( module );
			Activator.getLog().log( LogService.LOG_INFO,"Module Factory " + module.getIdentifier() + " registered." );
		}

		@Override
		protected void onDataUnRegistered(IJxtaModuleService<Module> module) {
			loader.removeModuleService(module);
			Activator.getLog().log( LogService.LOG_INFO,"Module Factory " + module.getIdentifier() + " unregistered." );
		}
	}
}