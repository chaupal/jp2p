package net.jp2p.chaupal.activator;

import net.jp2p.container.IJp2pContainer;

import org.osgi.framework.BundleActivator;

public interface IJp2pBundleActivator extends BundleActivator {

	/**
	 * get the container
	 * @return
	 */
	public IJp2pContainer getContainer();
}
