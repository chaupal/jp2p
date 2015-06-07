package net.jp2p.container.context;

import net.jp2p.container.factory.IPropertySourceFactory;

public interface IJp2pServiceManager extends IJp2pFactoryCollection{

	public static final String S_CONTEXT_FOUND = "The following context was found and registered: ";
	public static final String S_INFO_BUILDING = "\n\nAll the required services have been found. Start to build the container: ";
	
	/**
	 * Get the name of the manager
	 */
	public String getName();

	/**
	 * Returns true if the manager contains a factory for the given descriptor
	 */
	public boolean hasFactory(Jp2pServiceDescriptor descriptor);

	/**
	 * Get the factory for the given service descriptor
	 */
	public IPropertySourceFactory getFactory( Jp2pServiceDescriptor descriptor  );

	/**
	 * Add a listener
	 * @param listener
	 */
	public void addListener( IServiceManagerListener listener );

	/**
	 * Remove a listener
	 * @param listener
	 */
	public void removeListener( IServiceManagerListener listener );

	/**
	 * Open the manager
	 * @return 
	 */
	public boolean open();

	/**
	 * Close the manager
	 */
	public void close();
}