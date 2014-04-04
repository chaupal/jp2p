package net.jp2p.chaupal.jxta.module;


import java.util.Dictionary;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;

import net.jp2p.chaupal.activator.AbstractRegistrator;
import net.jxse.module.IJxtaModuleService;

public class ModuleFactoryRegistrator extends AbstractRegistrator<IJxtaModuleService<?>> {
	
	private static final String S_MODULE_SERVICE = "ModuleService";


	public ModuleFactoryRegistrator( BundleContext context ) {
		super( IJxtaModuleService.class.getName(), context );
	}

	@Override
	protected void fillDictionary(Dictionary<String, Object> dictionary) {
		dictionary.put( S_MODULE_SERVICE, super.getRegistered() );				
	}

	@Override
	public void unregister() {
		try {
			super.unregister();
		} catch (InvalidSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
