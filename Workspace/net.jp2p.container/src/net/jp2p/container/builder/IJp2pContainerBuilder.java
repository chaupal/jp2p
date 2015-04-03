package net.jp2p.container.builder;

import net.jp2p.container.IJp2pContainer;
import net.jp2p.container.utils.StringStyler;

public interface IJp2pContainerBuilder<T extends Object>{

	/**
	 * In production mode, the container is not added as a declarative service.
	 * This prevents other bundles from getting access to it.
	 * @author Kees
	 *
	 */
	public enum DeveloperModes{
		DEBUG,
		PRODUCTION;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}
	}
	
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
