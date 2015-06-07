package net.jp2p.chaupal.equinox.persistence;

import org.eclipse.core.runtime.preferences.ConfigurationScope;

import net.jp2p.chaupal.persistence.SimplePersistenceFactory;
import net.jp2p.container.component.IJp2pComponent;
import net.jp2p.container.persistence.IPersistedProperties;
import net.jp2p.container.persistence.PersistenceService;
import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.properties.IJp2pPropertySource;
import net.jp2p.container.properties.IJp2pWritePropertySource;
import net.jp2p.container.properties.IManagedPropertyListener;

public class OsgiPersistenceFactory extends SimplePersistenceFactory{
	
	@Override
	protected IJp2pComponent<IManagedPropertyListener<IJp2pProperties, Object>> onCreateComponent(
			IJp2pPropertySource<IJp2pProperties> source) {
		IPersistedProperties<IJp2pProperties, String,Object> properties = new PersistedProperties( (IJp2pWritePropertySource<IJp2pProperties>) super.getPropertySource(), ConfigurationScope.INSTANCE );
		PersistenceService<String,Object> service = new PersistenceService<String,Object>((IJp2pWritePropertySource<IJp2pProperties>) source, properties );
		service.start();
		return service;
	}

}
