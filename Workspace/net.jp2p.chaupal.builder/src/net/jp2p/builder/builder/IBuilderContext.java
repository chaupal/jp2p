/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.builder.builder;

import net.jp2p.builder.context.IContextObserver;
import net.jp2p.container.properties.IJp2pProperties;

public interface IBuilderContext<T extends Object,U extends IJp2pProperties> {

	public abstract IContextObserver<T> getObserver();

	public abstract void setObserver(IContextObserver<T> observer);

}