/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.log;

import net.jp2p.container.component.Jp2pComponent;

public class LoggerComponent extends Jp2pComponent<LoggerPropertySource>{

	public LoggerComponent( LoggerFactory factory ) {
		super( (LoggerPropertySource) factory.getPropertySource() );
	}
}