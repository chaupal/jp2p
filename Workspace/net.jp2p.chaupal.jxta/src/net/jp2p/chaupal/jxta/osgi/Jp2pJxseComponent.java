package net.jp2p.chaupal.jxta.osgi;

import net.jp2p.chaupal.service.Jp2pDSComponent;
import net.jp2p.container.activator.IJp2pBundleActivator;
import net.jxta.impl.loader.JxtaLoaderModuleManager;
import net.jxta.module.IModuleBuilder;
import net.jxta.peergroup.core.Module;

public class Jp2pJxseComponent extends Jp2pDSComponent{

	private static JxtaLoaderModuleManager<Module> manager;
	
	protected Jp2pJxseComponent( IJp2pBundleActivator<Object> activator ) {
		super( activator );
		manager = JxtaLoaderModuleManager.getRoot( this.getClass(), true );	
	}

	public void registerBuilder(IModuleBuilder<Module> builder) {
		manager.registerBuilder( builder);
    }

    public void unregisterBuilder( IModuleBuilder<Module> builder ) {
	   manager.unregisterBuilder( builder );
    }
}