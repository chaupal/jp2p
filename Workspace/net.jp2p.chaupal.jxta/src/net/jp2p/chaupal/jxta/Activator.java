package net.jp2p.chaupal.jxta;

import net.jp2p.chaupal.jxta.module.ModuleFactoryRegistrator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

public class Activator implements BundleActivator {

	public static final String BUNDLE_ID = "net.jp2p.chaupal";
	
	private static Activator plugin;
	
	private static Jp2pLogService logService;
	private static ModuleFactoryRegistrator mfr;
	
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;

		mfr = new ModuleFactoryRegistrator(context);

		logService = new Jp2pLogService( context );
		logService.open();
	}

	@Override
	public void stop(BundleContext context) throws Exception {		

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
}
