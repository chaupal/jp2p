/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.jxta.socket;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.utils.StringStyler;

public interface ISocketFactory {


	public enum Properties implements IJp2pProperties{
		TIME_OUT,
		SO_TIME_OUT,
		WAIT_FOR_RENDEZ_VOUS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
}