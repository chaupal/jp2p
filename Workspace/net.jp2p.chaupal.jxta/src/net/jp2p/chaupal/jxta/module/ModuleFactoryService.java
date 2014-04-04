package net.jp2p.chaupal.jxta.module;

import net.jp2p.chaupal.jxta.Activator;
import net.jp2p.chaupal.module.AbstractService;
import net.jxse.module.IJxtaModuleService;
import net.jxta.platform.Module;
import net.jxta.refplatform.impl.loader.DynamicJxtaLoader;

import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

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
public class ModuleFactoryService extends AbstractService<IJxtaModuleService<Module>> {

    private static String filter = "(objectclass=" + IJxtaModuleService.class.getName() + ")";

	private DynamicJxtaLoader loader = DynamicJxtaLoader.getInstance();
	
	public ModuleFactoryService(BundleContext bc) {
		super( bc, IJxtaModuleService.class, filter );
	}

	@Override
	public void open() {
		super.open();
	}

	@Override
	public void close() {
		super.close();
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