/**
 * The JXTA protocols often need to refer to peers, peer groups, pipes and other JXTA resources. These references are presented in
 * the protocols as JXTA IDs. JXTA IDs are a means for uniquely identifying specific peer groups, peers, pipes, codat and service
 * instances. JXTA IDs provide unambiguous references to the various JXTA entities. There are six types of JXTA entities which
 * have JXTA ID types defined: peergroups, peers, pipes, codats, module classes and module specifications. Additional JXTA ID
 * types may be defined in the future.
 * JXTA IDs are normally presented as URNs. URNs are a form of URI that ‘... are intended to serve as persistent, locationindependent,
 * resource identifiers’. Like other forms of URI, JXTA IDs are presented as text. See IETF RFC 2141 RFC2141 for
 * more information on URNs.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 *  INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL CHAUPAL 
 *  MICROSYSTEMS OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, 
 *  INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, 
 *  OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF 
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 *******************************************************************************
 * Copyright (c) 2014-2021 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************
*/
package net.jp2p.container.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ContainerBuilderClient<C extends Object>{

	private String path;
	private Class<?> clss;

	private Executor executor;

	private IJp2pContainerBuilder<C> builder;

	private Collection<IContainerBuilderListener<C>> listeners;

	/**
	 * The default build path
	 * @param clss
	 */
	public ContainerBuilderClient( Class<?> clss ) {
		this( clss, null );
	}
	
	public ContainerBuilderClient( Class<?> clss, String path ) {
		this.clss = clss;
		this.path = path;
		this.listeners = new ArrayList<>();
		executor = Executors.newCachedThreadPool();
	}

	protected void execute( ) {
		executor.execute(()->buildContainer( builder ));		
	}
	
	public synchronized void setBuilder(IJp2pContainerBuilder<C> builder ){
		this.builder = builder;
		for( IContainerBuilderListener<C> listener: listeners )
			builder.addContainerBuilderListener(listener);
	}

	public synchronized void unsetBuilder(IJp2pContainerBuilder<C> builder) {
		for( IContainerBuilderListener<C> listener: listeners )
			builder.removeContainerBuilderListener(listener);
		this.builder = null;
	}
	
	public void addContainerBuilderListener(IContainerBuilderListener<C> listener) {
		this.listeners.add(listener);
	}

	public void removeContainerBuilderListener(IContainerBuilderListener<C> listener) {
		this.listeners.remove(listener);	
	}

	/**
	 * This method can be overridden to allow a custom build. If a true is returned, then
	 * the default builder options will be ignored
	 * @param builder
	 * @return
	 */
	protected boolean onBuildContainer( IJp2pContainerBuilder<C> builder ) {
		return false;
	}
	
	protected final void buildContainer( IJp2pContainerBuilder<C> builder ) {
		if( onBuildContainer(builder))
			return;
		try {
			if(( this.path == null ) || this.path.equals(""))
				this.builder.build( this.clss );
			else
				this.builder.build(clss, path );
		} catch (Jp2pBuildException e) {
			e.printStackTrace();
			for( IContainerBuilderListener<C> listener: listeners )
				listener.notifyContainerBuilt( (ContainerBuilderEvent<C>) new ContainerBuilderEvent<>(this, ContainerBuilderEvent.Types.ERROR, null ));
		}
	}
}
