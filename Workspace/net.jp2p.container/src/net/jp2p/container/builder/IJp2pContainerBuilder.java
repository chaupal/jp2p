package net.jp2p.container.builder;

import net.jp2p.container.IJp2pContainer;

public interface IJp2pContainerBuilder<T extends Object>{

	/**
	 * Add a container builder listener
	 * @param listener
	 */
	public void addContainerBuilderListener( IContainerBuilderListener<T> listener );

	/**
	 * Remove a container builder listener
	 * @param listener
	 */
	public void removeContainerBuilderListener( IContainerBuilderListener<T> listener );

	/**
	 * get the container
	 * @return
	 */
	public IJp2pContainer<T> getContainer();
}
