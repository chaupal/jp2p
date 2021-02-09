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
package net.jp2p.builder.service;

import net.jp2p.container.context.Jp2pServiceLoader;
import net.jp2p.container.context.IJp2pServiceBuilder;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

public class Jp2pBuilderComponent implements CommandProvider{

	private Jp2pServiceLoader loader = Jp2pServiceLoader.getInstance();
	
	public void addBuilder( IJp2pServiceBuilder context ){
		loader.addBuilder(context);
	}

	public void removeBuilder( IJp2pServiceBuilder builder ){
		loader.removeBuilder( builder );
	}

	public Object _jp2p( CommandInterpreter ci ){
		return loader.printContexts();		
	}
	
	@Override
	public String getHelp() {
		return "\tjp2p - Provides information about the registered JP2P Service Builders.";
	}
}
