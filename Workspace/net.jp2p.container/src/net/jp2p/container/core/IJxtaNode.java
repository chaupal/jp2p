package net.jp2p.container.core;

public interface IJxtaNode<T extends Object> {

	/**
	 * The module that is to be shared
	 * @return
	 */
	public T getModule();

	/**
	 * Add a child
	 * @param child
	 */
	public void addChild( IJxtaNode<T> child );

	/**
	 * Add a module and return the corresponding node that is created
	 * @param child
	 */
	public IJxtaNode<T> addChild( T module );

	/**
	 * Remove a child
	 * @param child
	 */
	public void removeChild( IJxtaNode<T> child );

	/**
	 * The children of the module
	 * @return
	 */
	public T[] getChildren();
}
