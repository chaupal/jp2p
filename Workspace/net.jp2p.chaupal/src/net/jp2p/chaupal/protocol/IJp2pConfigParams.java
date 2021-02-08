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
 * @See: JXTA v2.0 Protocols Specification, Chapter 1
 * @author keesp
 * @Organisation: chaupal.org 
 * 
 * Copyright 2020: Apache 2.0 License
 *
*/
package net.jp2p.chaupal.protocol;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.jp2p.chaupal.document.IJp2pAdvertisement;
import net.jp2p.chaupal.id.IJp2pID;

public interface IJp2pConfigParams {

	/**
	 * {@inheritDoc}
	 */
	IJp2pConfigParams clone();

	/**
	 * Returns the private key
	 *
	 * @return the private key
	 */
	String getPrivateKey();

	/**
	 * Sets the private key
	 *
	 * @param privateKey the private key (i.e. password)
	 */
	void setPrivateKey(String privateKey);

	/**
	 * Returns the authentication type
	 *
	 * @return the authentication type
	 */
	String getAuthenticationType();

	/**
	 * Sets the authentication type
	 *
	 * @param authenticationType the authentication type
	 */
	void setAuthenticationType(String authenticationType);

	/**
	 * {@inheritDoc}
	 */
	String getBaseAdvType();

	/**
	 * Return the advertisement as a document.
	 *
	 *  @param adv the document to add elements to.
	 *  @return true if elements were added otherwise false.
	 */
	boolean addDocumentElements(Document adv);

	/**
	 * Returns the number of times this object has been modified since it was
	 * created. This permits the detection of local changes that require
	 * refreshing some other data.
	 *
	 * @return int the current modification count.
	 */
	int getModCount();

	/**
	 * Puts a service parameter in the service parameters table
	 * under the given key. The key is usually a ModuleClassID. This method
	 * makes a clone of the  given element into an independent document.
	 *
	 * @param key   The key.
	 * @param param The parameter document.
	 */
	void putServiceParam(IJp2pID key, Element param);

	/**
	 * Puts an advertisement into the service parameters table under the given
	 * key. The key is usually a {@code ModuleClassID}. This method makes a
	 * clone of the advertisement.
	 *
	 * @param key The key.
	 * @param adv The advertisement, a clone of which is stored or {@code null}
	 * to forget this key.
	 */
	void setSvcConfigAdvertisement(IJp2pID key, IJp2pAdvertisement adv);

	/**
	 * Puts an advertisement into the service parameters table under the given
	 * key. The key is usually a {@code ModuleClassID}. This method makes a
	 * clone of the advertisement.
	 *
	 * @param key The key.
	 * @param adv The advertisement, a clone of which is stored or {@code null}
	 * to forget this key.
	 * @param enabled If true then the service is enabled or disabled if false.
	 */
	void setSvcConfigAdvertisement(IJp2pID key, IJp2pAdvertisement adv, boolean enabled);

	/**
	 * Gets an advertisement from the service parameters table under the given
	 * key. The key is usually a {@code ModuleClassID}. This method makes a
	 * clone of the advertisement.
	 *
	 * @param key The key.
	 * @return If {@code true} then the service is enabled otherwise
	 * {@code false} if the service is disabled.
	 */
	boolean isSvcEnabled(IJp2pID key);

	/**
	 * Gets an advertisement from the service parameters table under the given
	 * key. The key is usually a {@code ModuleClassID}. This method makes a
	 * clone of the advertisement.
	 *
	 * @param key The key.
	 * @return The advertisement for the specified key otherwise {@code null}.
	 */
	IJp2pAdvertisement getSvcConfigAdvertisement(IJp2pID key);

	/**
	 * Returns the parameter element that matches the given key from the
	 * service parameters table. The key is of a subclass of ID; usually a
	 * ModuleClassID.
	 *
	 * @param key The key.
	 * @return Document The matching parameter document or null if
	 *         none matched.
	 */
	Document getServiceParam(IJp2pID key);

	/**
	 * Removes and returns the parameter element that matches the given key
	 * from the service parameters table. The key is of a subclass of ID;
	 * usually a ModuleClassID.
	 *
	 * @param key The key.
	 *
	 * @return The removed parameter element or {@code null} if not found.
	 */
	Document removeServiceParam(IJp2pID key);

	/**
	 * Removes any parameters for the given key from the service parameters
	 * table.
	 *
	 * @param key The key.
	 */
	void removeSvcConfigAdvertisement(IJp2pID key);

}