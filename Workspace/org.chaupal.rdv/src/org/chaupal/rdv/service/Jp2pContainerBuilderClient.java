package org.chaupal.rdv.service;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import net.jp2p.container.builder.ContainerBuilderClient;
import net.jp2p.container.builder.IJp2pContainerBuilder;

@Component
public class Jp2pContainerBuilderClient {

	//private Dispatcher dispatcher = Dispatcher.getInstance();
	private ContainerBuilderClient<INetworkManager<?>> client;
	
	
	public Jp2pContainerBuilderClient(){
		client = new ContainerBuilderClient<INetworkManager<?>>( this.getClass()); 
	}

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public synchronized void setBuilder(IJp2pContainerBuilder<INetworkManager<?>> builder ){
		client.setBuilder( builder );
		client.execute();
	}

	public synchronized void unsetBuilder(IJp2pContainerBuilder<INetworkManager<?>> builder) {
		client.removeBuilder( builder );
	}
	
}
