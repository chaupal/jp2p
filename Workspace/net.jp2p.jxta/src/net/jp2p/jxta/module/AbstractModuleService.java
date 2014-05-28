package net.jp2p.jxta.module;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jxse.module.IModuleService;

abstract class AbstractModuleService<T extends Object> implements IModuleService<T> {

	private static final String VERSION_REG_EX = "(\\d\\.){3}($|\\.v\\d{8}";
	private static final String RANGE_REG_EX = "(\\[|\\<)" + VERSION_REG_EX + "(\\]|\\>)";
	private static final String FULL_RANGE_REG_EX = RANGE_REG_EX + "$|(-" + RANGE_REG_EX + ")";
	
	private String identifier;
	private String description;
	private String version;
	
	protected AbstractModuleService( String identifier ) {
		this.setIdentifier(identifier);
	}

	public String getIdentifier() {
		return identifier;
	}

	protected void setIdentifier(String identifier) {
		if( identifier == null )
			throw new NullPointerException();
		this.identifier = identifier;
	}

	public String getDescription() {
		return description;
	}

	protected void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	protected void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		String str = this.identifier;
		if( this.version != null )
			str += this.version;
		return str.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(( obj == null ) || !(obj instanceof IModuleService ))
			return false;
		IModuleService<?> ms = (IModuleService<?>) obj;
		if(( ms.getIdentifier() == null ) || !identifier.equals( ms.getIdentifier()))
			return false;
		//if( this.version == null )
		//	return ( ms.getVersion() == null);
		//else
		//	return this.version.equals( ms.getVersion() );
		return true;
	}

	/**
	 * Check if the given version conforms to the correct syntax. This is either:
	 * - X.Y.Z
	 * - X.Y.Z.vYYYYMMDD
	 * @param version
	 * @return
	 */
	public static boolean isCorrectVersionSyntax( String version ){
		if( version == null )
			return true;
		Pattern pattern = Pattern.compile( VERSION_REG_EX );
		Matcher matcher = pattern.matcher(version);
		return matcher.matches();
	}
	
	/**
	 * Check if the given version range conforms to the correct syntax. This is either:
	 * - [X.Y.Z], <X,Y,Z], [X,Y,Z> or <X,Y,Z> 
	 * - X.Y.Z.vYYYYMMDD is allowed
	 * - [X,Y,Z]-{U,V,W>
	 * @param version
	 * @return
	 */
	public static boolean isCorrectRangeSyntax( String range ){
		if( range == null )
			return true;
		Pattern pattern = Pattern.compile( FULL_RANGE_REG_EX );
		Matcher matcher = pattern.matcher( range);
		return matcher.matches();
		
	}
}
