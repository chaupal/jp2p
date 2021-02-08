package net.jp2p.container.builder;

import java.io.InputStream;

import net.jp2p.container.IJp2pContainer;

public interface IJp2pContainerBuilder<T extends Object>{

	public static final String S_DEFAULT_PATH = "/JP2P-INF/jp2p-1.0.0.xml";

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
	 * Build the container from the given input stream
	 * @return
	 */
	public IJp2pContainer<T> build( InputStream in) throws Jp2pBuildException;

	/**
	 * Build the container from the given resource path. 
	 * @return
	 */
	public IJp2pContainer<T> build(Class<?> clss, String path) throws Jp2pBuildException;


	/**
	 * Build the container from the given default location
	 * @return
	 */
	public IJp2pContainer<T> build( Class<?> clss ) throws Jp2pBuildException;
}
