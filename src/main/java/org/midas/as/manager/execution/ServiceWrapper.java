package org.midas.as.manager.execution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.midas.as.agent.templates.Agent;
import org.midas.as.agent.templates.ServiceException;
import org.midas.as.broker.Receiver;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.proxy.Proxy;
import org.midas.as.proxy.ProxyException;
import org.midas.metainfo.EntityInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a service requisition. It holds several information
 * regardind the service to be processed, including the requester and provider
 * entities, the QoS time statistics and the parameters to be passed along the
 * requisition. It supports two execution methods, a synchronouns and an 
 * assynchronous one.  
 */
public class ServiceWrapper implements Callable<List>
{
	private static Logger LOG = LoggerFactory.getLogger(Receiver.class);
	
	// Variáveis de Informação
	private  String  provider;
	private  String  requester;
	
	// Variáveis de Parâmetros
	private  Map<String,Object> in;
	private  List<Object>       out;
	
	// Variáveis de Meta-Informação
	private  String  service;
	private  String  organization;
	
	// Variáveis de QoS
	private  long        startTime;
	private  long        endTime;
	
	/**
	 * Standard constructor method.
	 * 
	 * @param organization  The organization that offers the service
	 * @param service  The service name
	 * @param requester  The requesting Agent reference
	 */
	public ServiceWrapper(String organization,String service,Agent requester)
	{
		in = new HashMap<String,Object>();				
						
		this.service=service;
		
		if (requester == null)
		{
			this.requester = "remote";
		}
		else
		{
			this.requester = requester.getClass().toString().substring(6);
		}
		
		try
		{
			EntityInfo entity = Catalog.getEntityByService(organization,service);
			provider = entity.getName();
		}
		catch (CatalogException e)
		{
			provider = "remote";
		}

		
		this.organization=organization;		
	}
	
	/**
	 *  Implement from callable, its invoked by the {@link Execution Pool},
	 *  when the service is fired from its regular methods. This method must
	 *  not be invoked directly. It proccess the service, and states the QoS
	 *  metrics.
	 */	
	public List call() throws ProxyException,ServiceException 
	{
		// Inicializando vetor de saída
		out = new ArrayList<Object>(10);
		
		try
		{
			// Incrementando métrica de threads ativas
			ExecutionPool.increaseThreadCount();
			
			// Marcando tempo de início
			startTime = System.currentTimeMillis();
						
			// Processando Serviço
			Proxy.getInstance().require(organization,service,in,out, requester);
			
			// Marcando tempo de término
			endTime = System.currentTimeMillis();
			
			// Decrementando métrica de threads ativas
			ExecutionPool.decreaseThreadCount();			
			
			// TODO: Verificar se este é o local ideal para 
			// invocar o logging...
			LOG.debug("Executed "+organization+"."+service+" - Timing: "+(endTime-startTime)+"ms - Requirer: "+requester+" - Provider: "+provider,true);			
		}
		catch (ServiceException e)
		{		
			// Marcando tempo de término
			endTime = System.currentTimeMillis();
			
			// Decrementando métrica de threads ativas
			ExecutionPool.decreaseThreadCount();
			
			// TODO: Verificar se este é o local ideal para 
			// invocar o logging...
			LOG.error("ERROR Invoking "+organization+"."+service+" - Timing: "+(endTime-startTime)+"ms - Requirer: "+requester+"  -  Provider: "+provider+	" - Exception: ServiceException",false);
			
			throw e;
		}
		catch (ProxyException e)
		{			
			// Marcando tempo de término
			endTime = System.currentTimeMillis();
			
			// Decrementando métrica de threads ativas
			ExecutionPool.decreaseThreadCount();
			
			// TODO: Verificar se este é o local ideal para 
			// invocar o logging...
			LOG.error("ERROR Invoking "+organization+"."+service+" - Timing: "+(endTime-startTime)+"ms - Requirer: "+requester+"  -  Provider: not found - Exception: ProxyException",false);
			
			throw e;
		}		
		catch (RuntimeException e)
		{
			// Marcando tempo de término
			endTime = System.currentTimeMillis();
			
			// Decrementando métrica de threads ativas
			ExecutionPool.decreaseThreadCount();
			
			// TODO: Verificar se este é o local ideal para 
			// invocar o logging...
			LOG.error("ERROR Invoking "+organization+"."+service+" - Timing: "+(endTime-startTime)+"ms - Requirer: "+requester+"  -  Provider: "+provider+" - Exception: RuntimeException",false);
												
			throw new ServiceException("Service threw a Runtime Exception",e);
		}
		
		return out;
	}
	
	/**
	 * Set a group of name/value pairs as parameters of the requisition.
	 * 
	 * @param parameters  HashMap with the name/value pairs.
	 */
	public void setParameters(Map<String,Object> parameters)
	{
		in = parameters;
	}
	
	/**
	 * Add a single parameter to the requisition.
	 * 
	 * @param name  Parameter name
	 * @param value  Parameter value
	 */	
	public void addParameter(String name,Object value)
	{
		in.put(name,value);
	}

	/**
	 * Remove a parameter from the requisition.
	 * 
	 * @param name  Parameter name
	 */
	public void removeParameter(String name)
	{
		in.remove(name);
	}
	
	/**
	 * Tells if the requisiton already holds a parameter.
	 * 
	 * @param name  Parameter name
	 * 
	 * @return boolean  Telling if the parameter exists or not
	 */
	public void hasParameter(String name)
	{
		in.containsKey(name);
	}
	
	/**
	 * Method that runs the requisiton synchronously.
	 * 
	 * @return  A List with the response objects
	 * 
	 * @throws InterruptedException  In case the pool interrupts the execution 
	 * @throws ExecutionException  In case any exception is thrown from the service
	 */
	public List run() throws InterruptedException, ExecutionException
	{
		return ExecutionPool.runService(this);
	}
	

	/**
	 * Method that runs the requisiton assynchronously.
	 * 
	 * @return  A Future object that allows further recover of the List response
	 */
	public Future<List> submit()
	{
		return ExecutionPool.submitService(this);
	}
	
}
