package net.jp2p.container.core;


public interface IJP2PCompatibility<T extends Object> {

	/**
	 * Get an identifier for this compatibility object
	 * @return
	 */
	public String getIdentifier();
	
	/**
	 * Get the node which contains the relevant JXTA modules
	 * @return
	 */
	public IJxtaNode<T> getRoot();
	
	/**
	 * add a compatibility listener
	 * @param listener
	 */
	public void addListener( ICompatibilityListener listener );


	/**
	 * remove a compatibility listener
	 * @param listener
	 */
	public void removeListener( ICompatibilityListener listener );

	/**
	 * A standard JXTa application is usually run from main, so we allow this
	 * @param args
	 */
	public void main( String[] args);
	
	/**
	 * Deactivate the modules
	 */
	public void deactivate();
}
