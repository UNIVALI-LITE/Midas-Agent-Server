/*
 * Created on 11/04/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.midas.as.agent.templates;


import java.util.List;
import java.util.Map;

/**
 * The Component template is one of the main classes of the MIDAS structure. It is
 * an abstract class that must be extended in order to create concrete components 
 * that provides support and data funcionality to the agents. 
 */
public abstract class Component implements Provider 
{	
	/**
	 * Abstract method that acts as the providing interface for the components services, 
	 * must be implemented by the concrete component class.
	 * 
	 * @param service  Service name
	 * @param in  Map with the parameters passe along the service requisition
	 * @param out  List used to return information back to the requirer
	 * 
	 * @throws ServiceException  in case any processing error occurs
	 */	
	public abstract void provide(String service, Map in, List out) throws ServiceException;		
}
