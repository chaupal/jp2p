package org.chaupal.rdv.service;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.jp2p.builder.container.ContainerBuilderClient;
import net.jp2p.chaupal.platform.INetworkManager;
import net.jp2p.container.builder.IJp2pContainerBuilder;

@Component
public class Jp2pContainerBuilderClient {

	//private Dispatcher dispatcher = Dispatcher.getInstance();
	private ContainerBuilderClient<INetworkManager<?>> client;
	
	
	public Jp2pContainerBuilderClient(){
		client = new ContainerBuilderClient<INetworkManager<?>>(); 
	}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public synchronized void setBuilder(IJp2pContainerBuilder<INetworkManager<?>> builder ){
		//builder
		client.setBuilder( builder );
	}

	public synchronized void unsetBuilder(IJp2pContainerBuilder<INetworkManager<?>> builder) {
		client.unsetBuilder( builder );
	}
	
}
