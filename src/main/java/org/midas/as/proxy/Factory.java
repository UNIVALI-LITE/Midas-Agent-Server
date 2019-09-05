/**
 * Created on 12/02/2003
 * @author Aluizio Haendchen Filho
 */
package org.midas.as.proxy;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

import org.midas.as.agent.templates.Provider;
import org.midas.as.agent.templates.ServiceException;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;

 
/**
 * Esta classe atua como um manipulador de eventos e um Factory Method ataves
 * do qual e possivel abstrair o processo de criacao de instancias de organiza-
 * coes. Ela solicita colaboracao da classe CatalogParser para localizar servi-
 * cos no arquivo XML e devolver a instância para a classe MASThread.
 */
public class Factory 
{
	private static URLClassLoader MidasClassLoader;
	private static Factory singleton;
	
	private Factory()
	{
		
	}
	
	public static Factory getInstance()
	{
		if (singleton == null)
			singleton = new Factory();
		
		return singleton;
	}
		
	public void reloadClasses(String... classPath) throws FactoryException
	{
		URL[] urls = new URL[classPath.length];
		String url = null;
		
		try
		{
			for (int i=0 ; i<classPath.length ; i++)
			{
				url = classPath[i];
				urls[i] = new File(classPath[i]).toURL();
			}
		}
		catch(MalformedURLException e)
		{
			if (url==null)
			{
				throw new FactoryException("Error while reloading classes, bad URL definition - '"+url+"'");
			}
			else
			{
				throw new FactoryException("Error while reloading classes, bad URL definition");
			}
		}
		
		MidasClassLoader = new URLClassLoader(urls);
	}
		
	public void invokeEntity(String organizationName,String serviceName,Map in,List out) throws FactoryException,ServiceException 
	{   
		try
		{
			String path 	  = Catalog.getServicePath(organizationName,serviceName);
			Provider provider = instantiateEntity(path);
			provider.provide(serviceName,in,out);			
		}
		catch(CatalogException e)
		{
			throw new FactoryException("No entity for such service",e);
		} 
		catch (InstantiationException e) 
		{
			throw new FactoryException("Error instantiating the entity",e);
		} 
		catch (IllegalAccessException e) 
		{
			throw new FactoryException("Error instantiating the entity - probably lack of entity class visibility",e);
		} 		
		catch (ClassNotFoundException e) 
		{
			try 
			{
				String entityName = ((Catalog.getEntityByService(organizationName,serviceName)).getName());				
				throw new FactoryException("Unable to find "+entityName+" entity class, verify if declaration on structure.xml points to a valid class",e);
			} 
			catch (CatalogException e1) 
			{
				throw new FactoryException("Severe error on the consistence of information about the structure, recommended to check structure.xml and re-start the server",e1);
			}								
		}	
	}
	
	public Provider instantiateEntity(String entityClassName) 
		throws InstantiationException, IllegalAccessException, ClassNotFoundException
	{
		// Carrega a classe através do ClassLoader do MIDAS
		Class entityClass = loadClass(entityClassName);
		
		// Instancia um objeto da classe
		Object entityObject = entityClass.newInstance();
		
		// Realiza um casting, guardando a referência a 
		// entidade numa interface provedora
		Provider entityInterface = (Provider)entityObject; 
		
		// Retorna a interface provedora
		return (entityInterface);
	}
	
	public Class loadClass(String entityClassName) throws ClassNotFoundException
	{
		// Recupera a classe da entidade
		return(MidasClassLoader.loadClass(entityClassName));
	}
}
