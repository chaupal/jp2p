/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.chaupal.jxse.platform.seeds;

import java.net.URI;

import net.jp2p.container.utils.StringStyler;

public interface ISeedInfo {

	public enum SeedTypes{
		RDV,
		RELAY;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString());
		}	
	}	

	/**
	 * Get the seed type
	 * @return
	 */
	public SeedTypes getSeedType();

	/**
	 * Get the uri corresponding with the given string
	 * @return
	 */
	public URI getUri();
}
