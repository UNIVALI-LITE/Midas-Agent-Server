/**
 * @author Aluizio Haendchen Filho
 * Created on 21/10/2004
 */

package org.midas.as.agent.templates;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.midas.as.catalog.Catalog;
import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.execution.ServiceWrapperException;
import org.midas.as.manager.manager.Manager;
import org.midas.metainfo.ContainerInfo;

/**
 * The Agent template is one of the main classes of the MIDAS structure. It´s
 * an abstract class, that must be extend in order to create concrete agents that
 * implement the business running over the MIDAS platform.
 * 
 * It implements methods that allow the agent to require services and to access 
 * meta-data regarding its own container, and requires the concrete agent to implement
 * the methods that are invoked by the architecture to require the agent´s services,
 * and the lifeCycle of the agent. 
 */
public abstract class Agent implements Provider,Callable<Object>
{
	public volatile boolean alive = true;
	
	/**
	 * Abstract method that acts as the providing interface for the agents services, 
	 * must be implemented by the concrete agent class.
	 * 
	 * @param service  Service name
	 * @param in  Map with the parameters passe along the service requisition
	 * @param out  List used to return information back to the requirer
	 * 
	 * @throws ServiceException  in case any processing error occurs
	 */	
	public abstract void provide(String service,Map in, List out) throws ServiceException;
	
	/**
	 * This is the method used by the concrete agent whenever it wants to obtain a 
	 * requisition wrapper to a determined service. 
	 * 
	 * @param organization  Organization that provides the desired service
	 * @param service  Desired service name
	 * 
	 * @return ServiceWrapper object that represents a formal service requisition
	 * 
	 * @throws ServiceWrapperException  in case the desired service does not exists or cannot be reached 
	 * 
	 * @see ServiceWrapper
	 */
	public ServiceWrapper require(String organization,String service) throws ServiceWrapperException
	{
		return(Manager.getInstance().obtainService(organization,service,this));		
	}
	
	/**
	 * Implemented method from the Callable interface, its invoked in a separate thread
	 * from an execution pool, to fire the agent life cycle. Must not be invoked directly. 
	 * 
	 * @throws Exception  In case any exception occurs within the lifeCycle processing, the different possible exceptions are further filtered by the MIDAS.
	 */
	public Object call() throws Exception
	{
		lifeCycle();		
		return null;
	}
	
	/**
	 * Method that defines the agent LifeCycle, its invoked when the container its initialized
	 * for all the hosted agents, runs on a separate thread. Must be implemented by the concrete agent.
	 */
	protected abstract void lifeCycle() throws LifeCycleException,InterruptedException;
	
	/**
	 * Abstracts the process of communication with the internal MIDAS architecture, to recover 
	 * the stored meta-data of the application structure. Allows the agent to investigate all 
	 * the details regardind the organizations and data sources.
	 * 
	 * @return ContainerInfo object with the container meta-data
	 * 
	 * @see ContainerInfo
	 */
	protected ContainerInfo recoverMetaInformation()
	{
		return (Catalog.getContainerInfo());
	}
}