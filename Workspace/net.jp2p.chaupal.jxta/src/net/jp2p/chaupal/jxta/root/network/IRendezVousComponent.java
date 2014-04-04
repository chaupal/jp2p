package net.jp2p.chaupal.jxta.root.network;

import net.jp2p.container.properties.IJp2pProperties;
import net.jp2p.container.utils.StringStyler;

public interface IRendezVousComponent {

	public enum RendezVousServiceProperties  implements IJp2pProperties{
		STATUS,
		AUTO_START,
		IS_RENDEZVOUS,
		IS_CONNECTED_TO_RENDEZVOUS,
		RENDEZVOUS_STATUS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}	
	}


	public abstract Object getProperty(RendezVousServiceProperties key);

	public abstract void putProperty(RendezVousServiceProperties key,
			Object value);

}