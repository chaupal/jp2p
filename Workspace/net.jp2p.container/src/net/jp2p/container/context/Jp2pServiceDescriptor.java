/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.context;

public class Jp2pServiceDescriptor {

	String name;
	String context;
	boolean found;
	
	public Jp2pServiceDescriptor( String name, String context) {
		super();
		this.name = name;
		this.context = context;
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
		Jp2pServiceDescriptor descriptor = (Jp2pServiceDescriptor) obj;
		if( !this.context.equals( descriptor.getContext() ))
			return false;
		return this.name.equals( descriptor.getName() );
	}
	
	
}
