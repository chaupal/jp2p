package net.jp2p.container.activator;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.builder.IContainerBuilderListener;

public interface IJp2pBundleActivator{

	public void addContainerBuilderListener( IContainerBuilderListener listener );

	public void removeContainerBuilderListener( IContainerBuilderListener listener );

	/**
	 * get the container
	 * @return
	 */
	public IJp2pContainer getContainer();
}
