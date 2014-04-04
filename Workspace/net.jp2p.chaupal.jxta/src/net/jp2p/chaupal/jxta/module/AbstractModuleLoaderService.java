package net.jp2p.chaupal.jxta.module;

import net.jp2p.jxta.module.IModuleLoader;
import net.jxta.platform.Module;

public abstract class AbstractModuleLoaderService<T extends Module> {

	public AbstractModuleLoaderService() {
		// TODO Auto-generated constructor stub
	}

	public abstract void setModuleLoaderService( IModuleLoader<T> service );

	public void unsetModuleLoaderService( IModuleLoader<T> service ){
		
	}
	
	public void activate(){}
	
	public void deactivate(){}
}
