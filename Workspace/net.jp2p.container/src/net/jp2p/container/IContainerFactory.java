/*******************************************************************************
 * Copyright 2014 Chaupal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Kees Pieters - initial API and implementation
 *******************************************************************************/
package net.jp2p.container;

import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.factory.IComponentFactory;

public interface IContainerFactory<M extends Object> extends IComponentFactory<M>{

	/**
	 * Returns true if the context can be auto started 
	 * @return
	 */
	public abstract boolean isAutoStart();

	/**
	 * Create the component
	 * @return
	 */
	public IJp2pComponent<M> createComponent();
}