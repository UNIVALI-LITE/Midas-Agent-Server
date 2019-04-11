/**
 * Created on 12/05/2003
 * @author Aluizio Haendchen Filho
 */

package org.midas.as.proxy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.midas.as.agent.templates.ServiceException;
import org.midas.as.broker.Broker;
import org.midas.as.broker.BrokerException;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.ParameterInfo;
import org.midas.metainfo.ServiceInfo;



/**
 * 
**/
public class Proxy
{	
	private static Proxy singleton;
	
	private Proxy()
	{
		
	}
	
	public static Proxy getInstance()
	{
		if (singleton == null)
			singleton = new Proxy();
		
		return singleton;
	}
	
	@SuppressWarnings({"unchecked"})
	public void provide(String organization,String service, Map in, List out) throws ProxyException,ServiceException
	{
		// 1. Valida o Serviço
		if (Catalog.hasService(organization,service))
		{
			// Valida os Parâmetros de Entrada
			ServiceInfo servInfo = null;

			try 
			{
				servInfo = Catalog.getServiceByName(organization,service);
			} 
			catch(Exception e){} // A existência do serviço já foi validada
			
			Set parameters = servInfo.getParameters();				
			
			for (Iterator i=parameters.iterator();i.hasNext();)
			{
				// Para cada entrada de parâmetro do XML
				ParameterInfo paramInfo = (ParameterInfo)i.next();				
				Class paramClass = paramInfo.getParamClass();
				
				// Testa o nome do parâmetro
				if (! (in.containsKey(paramInfo.getName())))
				{
					throw new ProxyException("Unable to invoke service, lack of parameter - "+paramInfo.getName()+" -");
				}
				
				Class incomingClass = (in.get(paramInfo.getName())).getClass();
								
				// Testa o tipo do parâmetro
				if (!paramClass.isAssignableFrom(incomingClass))
				{				
					throw new ProxyException("Unable to invoke service, wrong type in parameter - "+paramInfo.getName()+" - expecting "+paramInfo.getParamClass());
				}
			}
		}
		else
		{
			throw new ProxyException("Invalid service -"+service+"- for organization -"+organization+"-");
		}		
		
		//chama o método fábrica que devolve a instância da entidade
    	Factory.getInstance().invokeEntity(organization,service,in,out);
  	}
	
	public void require(String organization,String service, Map in, List out) throws ProxyException,ServiceException
	{		
		if (Catalog.hasService(organization,service))
		{
			EntityInfo entity;
			
			try
			{
				entity = Catalog.getEntityByService(organization,service);
			}
			catch (CatalogException e)
			{
				throw new ProxyException("Could not redirect service due to an unexpected unconsistency error in the system structure memory model, recomendded re-intialize the container.");
			}
			
			String entityProtocol = entity.getProtocol();
			
			if (entityProtocol.equals("native"))
			{
				provide(organization,service,in,out);
			}
			else if (entityProtocol.equals("web_service"))
			{
				// TODO: Habilitar tradução de requisições nativas para webServices
				System.out.println("requisitando web_service");
			}
			else
			{
				throw new ProxyException("Could not redirect service due to an unexpected unconsistency error in the system structure memory model, recomendded re-intialize the container.");
			}
		}
		else
		{
			try
			{
				Broker.getInstance().require(organization,service,in,out);
			}
			catch (BrokerException e)
			{
				throw new ProxyException("Error while processing remote service",e);				
			}	
		}
	}

	public void loadClasses(String containerClassPath) throws ProxyException
	{
		Factory.getInstance().reloadClasses(containerClassPath.split(";"));		
	}	
}