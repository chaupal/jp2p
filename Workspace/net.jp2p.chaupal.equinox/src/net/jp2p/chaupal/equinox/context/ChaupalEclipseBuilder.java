package net.jp2p.chaupal.equinox.context;

import net.jp2p.chaupal.equinox.persistence.OsgiPersistenceFactory;
import net.jp2p.container.context.AbstractJp2pServiceBuilder;
import net.jp2p.container.properties.IJp2pProperties;

public class ChaupalEclipseBuilder extends AbstractJp2pServiceBuilder {

	private static final String S_CHAUPAL_ECLIPSE = "ChaupalEclipse";
	
	public ChaupalEclipseBuilder() {
		super( S_CHAUPAL_ECLIPSE);
	}

	
	@Override
	protected void prepare() {
		super.addFactory( new OsgiPersistenceFactory());
	}

	@Override
	public Object createValue( String componentName, IJp2pProperties id ){
		return null;
	}
}
