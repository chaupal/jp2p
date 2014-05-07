package net.jp2p.chaupal.jxta.module;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.jxta.module.IModuleLoader;
import net.jxse.module.IJxtaModuleService;
import net.jxse.module.IModuleService;
import net.jxta.platform.Module;

public abstract class AbstractModuleLoader<T extends Module> implements IModuleLoader<T> {

	private Collection<IModuleService<?>> requiredIds;

	private static Collection<IJxtaModuleService<? extends Module>> modules = new ArrayList<IJxtaModuleService<? extends Module>>();

	public AbstractModuleLoader() {
		requiredIds = new ArrayList<IModuleService<?>>();
		addRequiredModuleServices(requiredIds);
	}

	@SuppressWarnings("unchecked")
	public static IJxtaModuleService<Module>[] getModules(){
		return modules.toArray( new IJxtaModuleService[ modules.size() ] );
	}
	
	/**
	 * Add the module class IDS required to run the service
	 * @param requiredIds
	 */
	protected abstract void addRequiredModuleServices( Collection<IModuleService<?>> requiredIds );
		
	protected abstract boolean onModuleServiceAdded( IJxtaModuleService<T> module );
	
	@Override
	public boolean addModule(IJxtaModuleService<T> module) {
		for( IModuleService<?> service: requiredIds ){
			if( service.equals( module )){
				if( onModuleServiceAdded(module)){
					return modules.add( module );
				}
			}
		}
		return false;
	}

	public void activate(){}
	
	public void deactivate(){}
}
