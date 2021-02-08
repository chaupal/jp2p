/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.search;

import java.util.ArrayList;
import java.util.Collection;

import net.jp2p.container.component.ILeaf;
import net.jp2p.container.component.INode;
import net.jp2p.container.filter.IFilter;

public class ComponentSearch<T,U extends Object> implements ISearchable<T,U> {

	private SearchScope scope;
	
	public ComponentSearch( SearchScope scope ) {
		this.scope = scope;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T[] find(IFilter<T> filter, U reference, boolean include) {
		Collection<Object> results = null; 
		switch( scope ){
		case CONTAINER:
			INode<?,?> root = findRoot( reference );
			results = findNodes((IFilter<Object>) filter, root); 
			break;
		case ANCESTORS:
			break;
		case SIBLINGS:
			Object parent = getParent(reference);
			if( parent == null )
				return null;
			results = findChildNodes((IFilter<Object>) filter, parent);
			break;
		case CHILDREN:
			results = findChildNodes((IFilter<Object>) filter, reference);
			break;
		case DESCENDANTS:
			results = findNodes((IFilter<Object>) filter, reference ); 
			break;
		}
		if( !include )
			results.remove( reference );
		return (T[]) results.toArray( new Object[ results.size()]);
	}

	/**
	 * Find the root node
	 * @param current
	 * @return
	 */
	public static INode<?,?> findRoot( Object reference ){
		if(!( reference instanceof INode<?,?>))
			return null;
		INode<?,?> node = (INode<?, ?>) reference;
		if(( node.isRoot()) || ( !( reference instanceof ILeaf)))
			return node;
		return findRoot( ((ILeaf<?>) reference).getParent() );
	}

	/**
	 * Get the parent of the given reference, or null if it doesn't exist
	 * @param reference
	 * @return
	 */
	public static Object getParent( Object reference ){
		if(!( reference instanceof ILeaf))
			return null;
		ILeaf<?> descendant = (ILeaf<?>) reference;
		return descendant.getParent();
	}
	/**
	 * Find the root node
	 * @param current
	 * @return
	 */
	public static Collection<Object> findNodes( IFilter<Object> filter, Object reference ){
		Collection<Object> results = new ArrayList<Object>(); 
		findNodes( filter, reference, results );
		return results;
	}

	/**
	 * Find all the nodes accepting the given filter, starting from the reference
	 * @param current
	 * @return
	 */
	private static void findNodes( IFilter<Object> filter, Object reference, Collection<Object> results ){
		if( filter.accept( reference ))
			results.add( reference );
		if(!( reference instanceof INode<?,?>))
			return;
		INode<?,?> node = (INode<?, ?>) reference;
		for( Object child: node.getChildren() )
			findNodes( filter, child, results );
	}

	/**
	 * Find all the child nodes accepting the given filter, starting from the reference
	 * @param current
	 * @return
	 */
	private static Collection<Object> findChildNodes( IFilter<Object> filter, Object reference ){
		Collection<Object> results = new ArrayList<Object>(); 
		if(!( reference instanceof INode<?,?>))
			return null;
		INode<?,?> node = (INode<?, ?>) reference;
		for( Object child: node.getChildren() ){
			if( filter.accept( child ))
				results.add( child );
		}
		return results;
	}

}
