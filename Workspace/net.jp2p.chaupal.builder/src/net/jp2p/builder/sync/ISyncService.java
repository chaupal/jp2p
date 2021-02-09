package net.jp2p.builder.sync;

import org.osgi.framework.BundleContext;

public interface ISyncService<T extends Object> {

	/**
	 * Prepare the service
	 */
	public abstract void prepare(BundleContext bc, Class<ISyncServer> clss);
	
	public void open();
	
	public void close();

}