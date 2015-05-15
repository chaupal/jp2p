/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.activator;

import net.jp2p.container.utils.StringStyler;

public interface IActivator extends ISimpleActivator
{
	public static final String S_ACTIVATOR = "Activator";
	public static final String S_STATUS = "Status";
	
	
	public enum Status{
		UNKNOWN,
		DISABLED,
		IDLE,
		INITIALISING,
		INITIALISED,
		ACTIVATING,
		ACTIVE,
		PAUSED,
		AVAILABLE,
		SHUTTING_DOWN,
		COMPLETED,
		FAILED,
		FINALISING,
		FINALISED;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	/**
	 * Start activating. Returns true if the activator is paused, false
	 * if not, for instance when the activator is not started, or paused already
	 */
	public boolean pause();

	/**
	 * Get the status of the activator
	 * @return
	*/
	public Status getStatus();
	
	/**
	 * Supportive method gives true if the status is set to idle
	 * @return
	 */
	public boolean isIdle();

	/**
	 * Supportive method gives true if the status is set to available
	 * @return
	 */
	public boolean isAvailable();
}