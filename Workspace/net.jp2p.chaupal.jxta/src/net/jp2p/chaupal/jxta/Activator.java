package net.jp2p.chaupal.jxta;

import net.jp2p.chaupal.jxta.module.ModuleFactoryService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	public static final String BUNDLE_ID = "net.jp2p.chaupal";
	
	private static Activator plugin;
	
	private static Jp2pLogService logService;
	private static ModuleFactoryService moduleService; 
	
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;

		moduleService = new ModuleFactoryService( context );
		moduleService.open();			

		logService = new Jp2pLogService( context );
		logService.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {		

		if( moduleService != null ){
		    moduleService.close();
		    moduleService = null;
		}

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

	/**
	 * Get the module service
	 * @return
	 */
	public static final ModuleFactoryService getModuleService() {
		return moduleService;
	}
}
