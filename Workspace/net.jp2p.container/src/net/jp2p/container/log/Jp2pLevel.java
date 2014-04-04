/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.log;

import java.util.logging.Level;

public class Jp2pLevel extends Level {
	private static final long serialVersionUID = -4215501769316462679L;

	public static final Level JP2PLEVEL = new Jp2pLevel("JP2PLEVEL", Level.WARNING.intValue()+10);  
	
	public Jp2pLevel(String arg0, int arg1) {
		super(arg0, arg1);
	}

	public Jp2pLevel(String arg0, int arg1, String arg2) {
		super(arg0, arg1, arg2);
	}

	public static Level getJxtaLevel() {    
	    return JP2PLEVEL;    
	  } 
}
