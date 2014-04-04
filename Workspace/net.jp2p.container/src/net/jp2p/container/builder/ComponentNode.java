/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

import java.util.Collection;
import java.util.TreeSet;

public class ComponentNode<T extends Object> implements Comparable<ComponentNode<T>> {

	public static final String S_NODE = "Node:";
	
	private T data;
	
	private ComponentNode<?> parent;
	
	private Collection<ComponentNode<?>> children;

	public ComponentNode() {
		this( null );
	}

	public ComponentNode( T data ) {
		this.data = data;
		children = new TreeSet<ComponentNode<?>>();
	}

	protected ComponentNode( T data, ComponentNode<?> parent ) {
		this( data );
		this.parent = parent;
	}

	public ComponentNode<?> getParent() {
		return parent;
	}

	
	public T getData() {
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ComponentNode<?> addChild( Object data ){
		ComponentNode<?> node = new ComponentNode( data, this );
		children.add( node );
		return node;
	}

	public boolean removeChild( Object data ){
		for( ComponentNode<?> nd: children ){
			if( nd.getData().equals( data )){
				return children.remove(nd);
			}
		}
		return false;
	}

	protected Collection<ComponentNode<?>> getChildrenAsCollection(){
		return children;
	}
	
	public int nrOfChildren(){
		return this.children.size();
	}
	
	public ComponentNode<?>[] getChildren(){
		return this.children.toArray( new ComponentNode<?>[ this.children.size() ] );
	}

	@Override
	public int compareTo(ComponentNode<T> arg0) {
		if( arg0 == null )
			return 1;
		if(( this.data == null ) && ( arg0.getData() == null ))
				return 0;
		if(( this.data != null ) && ( arg0.getData() == null ))
				return 1;
		if(( this.data == null ) && ( arg0.getData() != null ))
				return -1;

		return this.data.toString().compareTo( arg0.getData().toString());
	}
	
	@Override
	public String toString() {
		return S_NODE + this.getData().toString();
	}
}
