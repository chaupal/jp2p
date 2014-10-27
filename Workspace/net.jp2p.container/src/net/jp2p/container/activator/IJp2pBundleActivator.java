package net.jp2p.container.activator;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.IContainerBuilderListener;

public interface IJp2pBundleActivator<T extends Object>{

	/**
	 * get the bundle ID
	 * @return
	 */
	public String getBundleId();
	
	public void addContainerBuilderListener( IContainerBuilderListener<T> listener );

	public void removeContainerBuilderListener( IContainerBuilderListener<T> listener );

	/**
	 * get the container
	 * @return
	 */
	public IJp2pContainer<?> getContainer();
}
