package net.jp2p.jxta.module;

import net.jxse.module.IJxtaModuleService;
import net.jxta.platform.Module;
import net.jxta.platform.ModuleClassID;
import net.jxta.platform.ModuleSpecID;
import net.jxta.protocol.JxtaSocket;

public abstract class AbstractJxtaService<T extends Module> extends
		AbstractModuleService<T> implements IJxtaModuleService<T> {

	private JxtaSocket implAdv;
	private T module;
	
	protected AbstractJxtaService(JxtaSocket implAdv, T module) {
		super(implAdv.getCode());
		this.implAdv = implAdv;
		this.module = module;
	}
	
	
	@Override
	public String getDescription() {
		return this.implAdv.getDescription();
	}

	@Override
	public T getModule() {
		return module;
	}

	@Override
	public ModuleClassID getModuleClassID() {
		return implAdv.getModuleSpecID().getBaseClass();
	}

	@Override
	public ModuleSpecID getModuleSpecID() {
		return implAdv.getModuleSpecID();
	}

	@Override
	public JxtaSocket getModuleImplAdvertisement() {
		return this.implAdv;
	}

	@Override
	public int hashCode() {
		String str = this.implAdv.getID().toString();
		if( super.getVersion() != null )
			str += super.getVersion();
		return str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(( obj == null ) || !(obj instanceof IJxtaModuleService ))
			return false;
		IJxtaModuleService<?> ms = (IJxtaModuleService<?>) obj;
		if(( ms.getModuleImplAdvertisement() == null ) || !this.getModuleImplAdvertisement().equals( ms.getModuleImplAdvertisement()))
			return false;
		//if( super.getVersion() == null )
		//	return ( ms.getVersion() == null);
		//else
		//	return super.getVersion().equals( ms.getVersion() );
		return true;
	}
}
