package net.jp2p.chaupal.jxse.service;

import net.jp2p.jxta.factory.IJxtaComponents.JxtaComponents;
import net.jxta.impl.loader.JxtaLoaderModuleManager;
import net.jxta.impl.modulemanager.JxtaModuleBuilder;
import net.jxta.module.IModuleBuilder;
import net.jxta.peergroup.core.Module;

public class Component{

	private static JxtaLoaderModuleManager<Module> manager;
	
	private boolean canBuild;
	
	public Component() {
		manager = JxtaLoaderModuleManager.getRoot( Component.class, true );	
		this.canBuild = false;
	}

	public void activate(){ /* DO NOTHING */ }
	
	public void deactivate(){ /* DO NOTHING */ }
	
	protected final boolean canBuild() {
		return canBuild;
	}

	public void registerBuilder(IModuleBuilder<Module> builder) {
		manager.registerBuilder( builder);
		if( builder instanceof JxtaModuleBuilder )
			this.canBuild = true;
    }

    public void unregisterBuilder( IModuleBuilder<Module> builder ) {
	   manager.unregisterBuilder( builder );
    }

    
    public static final boolean canBuild( JxtaComponents jxtaComponent ) {
    	//PlatformDescriptor descriptor = new PlatformDescriptor();
    	return true;//manager.canBuild(descriptor);
	}
}