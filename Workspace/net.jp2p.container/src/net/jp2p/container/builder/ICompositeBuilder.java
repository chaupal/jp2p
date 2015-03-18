/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package net.jp2p.container.builder;

public interface ICompositeBuilder<T extends Object> {

	public abstract void addListener(ICompositeBuilderListener<?> listener);

	public abstract void removeListener(ICompositeBuilderListener<?> listener);

	public void build();

	public abstract boolean isCompleted();
}