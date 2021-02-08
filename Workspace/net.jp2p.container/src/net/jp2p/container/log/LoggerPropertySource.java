/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.log;

import net.jp2p.container.context.IJp2pServiceBuilder;
import net.jp2p.container.properties.AbstractJp2pWritePropertySource;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.utils.StringStyler;

public class LoggerPropertySource extends AbstractJp2pWritePropertySource{

	public enum LoggerDirectives implements IJp2pProperties{
		LOG_LEVEL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}
	
	public LoggerPropertySource( IJp2pPropertySource<IJp2pProperties> parent) {
		super( IJp2pServiceBuilder.Components.LOGGER_SERVICE.toString(), parent);
	}
}