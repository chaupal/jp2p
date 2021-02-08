/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.properties;

import java.util.Iterator;


public interface IJp2pPropertySource< T extends Object> extends IDescendant<T, IJp2pPropertySource<?>>{

	public static final String JP2P_SETTINGS = "jp2p.settings";
	public static final String S_USER_HOME = "user.home";
	public static final String S_JP2P = "JP2P";

	/**
	 * Get the component name
	 * @return
	 */
	public String getComponentName();

	/**
	 * Get the id of the component that this property source is bound to
	 * @return
	 */
	public String getId();
	
	/**
	 * If the enabled boolean is true, the factroy will create the component
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Get the depth of the component (root = 0)
	 * @return
	 */
	public int getDepth();
	
	/**
	 * Get the default value for the property
	 * @param id
	 * @return
	 */
	public Object getDefault( T id );
	
	/**
	 * Get the current value for the given property
	 */
	public Object getProperty( T id );

	/**
	 * Get the category for the given key
	 * @param key
	 * @return
	 */
	public String getCategory( T id );

	/**
	 * Get the managed property
	 * @param id
	 * @return
	 */
	public ManagedProperty<T,Object> getManagedProperty( T id );

	/**
	 * Set the managed property
	 * @param property
	 * @return
	 */
	public boolean setManagedProperty( ManagedProperty<IJp2pProperties,Object> property );

	/**
	 * Get an iterator over the properties
	 * @return
	 */
	public Iterator<T> propertyIterator();
	
	/**
	 * Validate the given property
	 * @param id
	 * @return
	 */
	public boolean validate( T id, Object value );

	/**
	 * Get the given directives
	 * @param id
	 * @return
	 */
	public String getDirective( IJp2pDirectives id );

	/**
	 * Set the directive with the given value
	 * @param id
	 * @return
	 */
	public boolean setDirective( IJp2pDirectives id, String value );

	/**
	 * Get an iterator over the directives
	 * @return
	 */
	public Iterator<IJp2pDirectives> directiveIterator();

	/**
	 * A property convertor is able to convert a property to a string and vice versa
	 * @param source
	 * @return
	 */
	public IPropertyConvertor<T, String, Object> getConvertor();

	/**
	 * add a child to the property source
	 * @param child
	 * @return
	 */
	@Override
	public boolean addChild( IJp2pPropertySource<?> child );

	/**
	 * Remove a child from the property source
	 * @param child
	 */
	@Override
	public void removeChild( IJp2pPropertySource<?> child );

	/**
	 * Get the children of the property source
	 * @return
	 */
	@Override
	public IJp2pPropertySource<?>[] getChildren();
	
	/**
	 * Get the child with the given component name
	 * @param componentName
	 * @return
	 */
	public IJp2pPropertySource<?> getChild( String componentName );

	/**
	 * Returns true if the source does not contain any properties
	 * @return
	 */
	public boolean isEmpty();
	
	/**
	 * Copy the directives and properties intop the given source. 
	 * NOTE: the method does not clear the given source, so this acts as a merge operation
	 * rather than a clone.
	 * @param source
	 * @throws CloneNotSupportedException
	 */
	public void copy( IJp2pPropertySource<IJp2pProperties> source ) throws CloneNotSupportedException;

}
