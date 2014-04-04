package net.jp2p.chaupal.jxta.module;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jxse.module.IJxtaModuleService;
import net.jxta.platform.Module;

public abstract class AbstractModuleFactory<T extends Module> implements IModuleFactory<T> {

    private Collection<IJxtaModuleService<T>> modules;
    private IJp2pPropertySource<IJp2pProperties> source;
    private ModuleFactoryRegistrator registrator;
    
    protected AbstractModuleFactory( IJp2pPropertySource<IJp2pProperties> source, ModuleFactoryRegistrator registrator ) {
		modules = new ArrayList<IJxtaModuleService<T>>();
		this.source = source;
		this.registrator = registrator;
	}

    
    protected IJp2pPropertySource<IJp2pProperties> getSource() {
		return source;
	}

	/**
     * add the necessary modules
     * @param modules
     */
	protected abstract void addModules( Collection<IJxtaModuleService<T>> modules );

	@SuppressWarnings("unchecked")
	@Override
	public IJxtaModuleService<T>[] createModules() {
		if( !this.source.isEnabled() )
			return null;
		
		this.addModules(modules);
		for( IJxtaModuleService<T> module: modules ){
			this.registrator.register(module);
		}
		return modules.toArray( new IJxtaModuleService[ modules.size()]);
	}
}
