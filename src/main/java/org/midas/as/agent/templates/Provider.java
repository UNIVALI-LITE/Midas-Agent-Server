/**
 * Created on 06/04/2004
 * @author Aluizio Haendchen Filho
 */
package org.midas.as.agent.templates;

import java.util.List;
import java.util.Map;

import org.midas.as.proxy.ProxyException;

/**
 * Interface that provides a standard providing signature to any {@link Agent} or
 * {@link Component} hosted within the container.
 */
public interface Provider
{
	/**
	 * Method signature that acts as the providing interface for an entity´s service. 
	 * 
	 * @param service  Service name
	 * @param in  Map with the parameters passe along the service requisition
	 * @param out  List used to return information back to the requirer
	 * 
	 * @throws ServiceException  in case any processing error occurs
	 */
	public void provide(String service, Map in, List out) throws ServiceException;
}
