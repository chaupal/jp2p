package net.jp2p.chaupal.jxta.service;

import java.net.URL;
import java.util.Collection;

import net.jp2p.jxta.factory.IJxtaComponents.JxtaNetworkComponents;
import net.jxta.impl.loader.JxtaLoaderModuleManager;
import net.jxta.impl.loader.RefJxtaLoader;
import net.jxta.impl.modulemanager.AbstractJxtaModuleDescriptor;
import net.jxta.impl.modulemanager.ImplAdvModuleDescriptor;
import net.jxta.module.IModuleBuilder;
import net.jxta.peergroup.core.Module;
import net.jxta.protocol.ModuleImplAdvertisement;
import net.jxta.util.cardinality.Cardinality.Denominator;

public class Component{

	private static JxtaLoaderModuleManager<Module> manager;
	
	public Component() {
		manager = JxtaLoaderModuleManager.getRoot( Component.class, true );	
	}

	public void registerBuilder(IModuleBuilder<Module> builder) {
		manager.registerBuilder( builder);
    }

    public void unregisterBuilder( IModuleBuilder<Module> builder ) {
	   manager.unregisterBuilder( builder );
    }

    
    public static final boolean canBuild( JxtaNetworkComponents jxtaComponent ) {
    	PlatformDescriptor descriptor = new PlatformDescriptor();
    	return true;//manager.canBuild(descriptor);
	}

	private static class PlatformDescriptor extends AbstractJxtaModuleDescriptor {

		public static final String S_DESCRIPTION = "Standard World PeerGroup Reference Implementation";
		public static final String S_IDENTIFIER = "net.jxta.impl.platform.Platform";
		public static final String S_MODULE_SPEC_ID = "urn:jxta:uuid-deadbeefdeafbabafeedbabe000000010106";
		private static final String S_VERSION ="2.8.0"; 

		public static final String S_SHADOW_IDENTIFIER = "net.jxta.impl.platform.ShadowPeerGroup";
		public static final String S_ST_PEERGROUP_IDENTIFIER  = "net.jxta.impl.platform.StdPeerGroup";

		//These are skipped
		private enum PlatformModules{
			STD_PEERGROUP,
			SHADOW_PEERGROUP,
			PLATFORM,
			PROXY_SERVICE;
			
			@Override
			public String toString() {
				String str = super.toString();
				switch( this ){
				case STD_PEERGROUP:
					str = "net.jxta.impl.platform.StdPeerGroup";
					break;
				case SHADOW_PEERGROUP:
					str = "net.jxta.impl.platform.ShadowPeerGroup";
					break;
				case PLATFORM:
					str = "net.jxta.impl.platform.Platform";
					break;
				case PROXY_SERVICE:
					str = "net.jxta.impl.proxy.ProxyService";
					break;
				default:
					break;
				}
				return str;
			}
		
			/**
			 * Returns true if the code should be included in the given module
			 * @param code
			 * @return
			 */
			public static boolean isPlatform( String code ){
				for( PlatformModules sm: values() ){
					if( !sm.toString().equals( code ))
						return true;
				}
				return false;
			}
		}

		PlatformDescriptor() {
			super( Denominator.ONE );
		}

		protected void prepare(){
			super.setIdentifier(S_IDENTIFIER);
			super.setRefClass( S_IDENTIFIER );
			super.setDescription( S_DESCRIPTION );
			super.setVersion( S_VERSION );
			super.setSpecID( S_MODULE_SPEC_ID );

			//Load the dependencies
			URL url = PlatformDescriptor.class.getResource( "/" + RefJxtaLoader.S_RESOURCE_LOCATION );
			String hashHex = Integer.toString( this.hashCode(), 16);
			Collection<ModuleImplAdvertisement> implAdvs = RefJxtaLoader.locateModuleImplementations( hashHex, url );
			for( ModuleImplAdvertisement implAdv: implAdvs ){
				if( PlatformModules.isPlatform( implAdv.getCode() )){					
					if( !S_ST_PEERGROUP_IDENTIFIER.equals(implAdv.getCode()))
						continue;
					if( !S_SHADOW_IDENTIFIER.equals(implAdv.getCode()))
						continue;
				}
				super.addDependency( new ImplAdvDescriptor( implAdv ));
			}

		}

		@Override
		public boolean onInitialised() {
			return true;
		}		

		/**
		 * Create a descriptor from the given impl advertisement
		 * @author Kees
		 *
		 */
		private static class ImplAdvDescriptor extends ImplAdvModuleDescriptor{
			
			protected ImplAdvDescriptor(ModuleImplAdvertisement implAdv ) {
				super(implAdv);
			}
		}

	}	
}