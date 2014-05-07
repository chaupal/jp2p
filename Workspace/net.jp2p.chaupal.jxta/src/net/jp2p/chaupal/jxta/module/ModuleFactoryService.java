package net.jp2p.chaupal.jxta.module;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.chaupal.jxta.Activator;
import net.jp2p.chaupal.module.AbstractService;
import net.jxse.module.IJxtaModuleService;
import net.jxse.module.IModuleService;
import net.jxta.platform.Module;

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

	private Collection<IJxtaModuleService<Module>> services;
	
	public ModuleFactoryService(BundleContext bc) {
		super( bc, IJxtaModuleService.class, filter );
		services = new ArrayList<IJxtaModuleService<Module>>();
	}

	/**
	 * returns true if the service with the given identifier is avaialable
	 * @param identifier
	 * @return
	 */
	public boolean hasService( String identifier ){
		if( identifier == null )
			return false;
		for( IModuleService<?> service: this.services ){
			if( identifier.equals(service.getIdentifier() ))
				return true;
		}
		return false;
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
		services.add( module );
		Activator.getLog().log( LogService.LOG_INFO,"Module Factory " + module.getIdentifier() + " registered." );
	}

	@Override
	protected void onDataUnRegistered(IJxtaModuleService<Module> module) {
		services.remove(module);
		Activator.getLog().log( LogService.LOG_INFO,"Module Factory " + module.getIdentifier() + " unregistered." );
	}
}