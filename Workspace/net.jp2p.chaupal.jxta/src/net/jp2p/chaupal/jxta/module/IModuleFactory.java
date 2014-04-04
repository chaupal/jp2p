package net.jp2p.chaupal.jxta.module;

import net.jxse.module.IJxtaModuleService;
import net.jxta.platform.Module;

public interface IModuleFactory<U extends Module>{

	/**
	 * Get the modules that are needed to start this service
	 * @return
	 */
	public IJxtaModuleService<U>[] createModules();
}
