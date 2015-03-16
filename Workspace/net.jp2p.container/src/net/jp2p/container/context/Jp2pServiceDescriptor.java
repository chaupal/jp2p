/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

import net.jp2p.container.utils.Utils;

public class Jp2pServiceDescriptor implements Comparable<Jp2pServiceDescriptor>{

	String name;
	String context;
	boolean optional = false;
	boolean found;

	public Jp2pServiceDescriptor( String name, String context ) {
		this( name, context, false );
	}

	public Jp2pServiceDescriptor( String name, String context, boolean optional) {
		super();
		this.name = name;
		this.context = context;
		this.optional = optional;
		this.found = false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public boolean isOptional() {
		return optional;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	@Override
	public String toString() {
		return this.context + ":" + this.name + "=" + found;
	}

	@Override
	public int hashCode() {
		String str = this.context + ":" + this.name; 
		return str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if( super.equals(obj))
			return true;
		if( !( obj instanceof Jp2pServiceDescriptor))
			return false;
		
		if( Utils.isNull( this.context ))
			return false;
		Jp2pServiceDescriptor descriptor = (Jp2pServiceDescriptor) obj;		
		return this.name.equals( descriptor.getName() );
	}

	@Override
	public int compareTo(Jp2pServiceDescriptor o) {
		if(( context == null ) && ( o.getContext() != null ))
			return -1; 
		if(( context != null ) && ( o.getContext() == null ))
			return 1; 
		if(( context != null ) && ( o.getContext() != null ))
			return this.context.compareTo( o.getContext()); 
		return this.name.compareTo( o.getName() );
	}
	
	
}
