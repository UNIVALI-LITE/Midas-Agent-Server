/**
 * Created on 28/12/2004
 * @author Aluizio Haendchen Filho
 */

package org.midas.as.catalog;

import java.util.HashMap;
import java.util.Map;

import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.MetaInfoException;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ServiceInfo;



public class Catalog 
{
	// TODO: Verificar Possível uso de Exceções Personalizadas nos Gets

	private static String serverAddress;
	private static String serverPort;
		
	// Variável que guarda os Meta Dados de um Agent Server
	// private static ContainerInfo containerInfo;
	private static Map<String, ContainerInfo> containerInfos = new HashMap<>();
		
	// HashMap que serve de indíce para os data sources
	// Key   = Nome do data source
	// Value = Array de strings com o driver (1) e a conexão (2) 
	private static Map<String,String[]> dataSources;
		
	// HashMap que serve de índice para as organizações que o AS hospeda
	// Key   = Nome do serviço
	// Value = Objeto do serviço
	private static Map<String,ServiceInfo> services;
	
	/**
	 * Obtém a string do driver pelo nome do Data Source
	 * @param dataSourceName - nome do Data Source
	 * @return string do driver
	 */
	public static String getDatabaseDriver(String source) throws CatalogException
	{
		return ( (Catalog.getDataSourceInfo(source))[0] );	
	}
	
	
	/**
	 * Obtém a string de conexão pelo nome do Data Source
	 * @param dataSourceName - nome do Data Source
	 * @return string de conexão
	 */	
	public static String getDatabaseConnectionString(String source) throws CatalogException
	{
		return ( (Catalog.getDataSourceInfo(source))[1] );
	}
	
	/**
	 * Obtém as strings de driver e conexão pelo nome do Data Source
	 * @param dataSourceName - nome do Data Source
	 * @return strings de driver e conexão
	 */
	private static String[] getDataSourceInfo(String dataSourceName) throws CatalogException
	{	
		String[] dataSourceInfo = dataSources.get(dataSourceName);
		
		if (dataSourceInfo == null)
		{
			throw new CatalogException ("Invalid datasource name");
		}
		else
		{
			return (dataSourceInfo);
		}
	}	
		
	public static String getServicePath(String organizationName,String serviceName) 
		throws CatalogException
	{
		return (getServiceByName(organizationName,serviceName)).getPath();
	}		
	
	// public static OrganizationInfo getOrganizationByName(String organizationName) 
	// 	throws MetaInfoException
	// {
	// 	return (containerInfo.getOrganizationByName(organizationName));
	// }
	
	// public static EntityInfo getEntityByName(String organizationName,String entityName) 
	// 	throws MetaInfoException 
	// {
	// 	return (containerInfo.getOrganizationByName(organizationName).getEntityByName(entityName));
	// }

	public static OrganizationInfo getOrganizationByName(String port, String organizationName) 
		throws MetaInfoException
	{
		return (containerInfos.get(port).getOrganizationByName(organizationName));
	}
	
	public static EntityInfo getEntityByName(String port, String organizationName,String entityName) 
		throws MetaInfoException 
	{
		return (containerInfos.get(port).getOrganizationByName(organizationName).getEntityByName(entityName));
	}
	
	public static EntityInfo getEntityByService(String organizationName,String serviceName) 
		throws CatalogException
	{
		return (getServiceByName(organizationName,serviceName)).getEntity();								
	}	
	
	public static boolean hasService(String organizationName,String serviceName) 
	{
		if (services.containsKey(organizationName+"."+serviceName))
		{
			return true;
		}
		
		return false;	
	}
	
	public static ServiceInfo getServiceByName (String organizationName,String serviceName)	throws CatalogException
	{
		if (hasService(organizationName,serviceName))
		{
			return services.get(organizationName+"."+serviceName);
		}
		else
		{
			throw new CatalogException("Invalid service - " + serviceName + " - for organization - " + organizationName + " -");
		}		
	}

	public static String loadCatalog(String structureXML, String servicesXML) throws CatalogException
	{
		try
		{
			return Parser.parse(structureXML, servicesXML);				
		}
		catch(ParserException e)
		{
			throw new CatalogException("Unable to parse xml files",e);
		}		
	}

	public static String getServerAddress() {
		return Catalog.serverAddress;
	}

	public static void setServerAddress(String serverAddress) {
		Catalog.serverAddress = serverAddress;
	}

	public static String getServerPort() {
		return Catalog.serverPort;
	}

	public static void setServerPort(String serverPort) {
		Catalog.serverPort = serverPort;
	}

	// Getters & Setters...		
	// public static ContainerInfo getContainerInfo()
	// {
	// 	return containerInfo;
	// }
	
	// public static void setContainerInfo(ContainerInfo asInfo)
	// {
	// 	Catalog.containerInfo = asInfo;
	// }

	public static void removeContainer(String port)
	{
		containerInfos.remove(port);
	}

	public static ContainerInfo getContainerInfos(String port)
	{
		return containerInfos.get(port);
	}
	
	public static void setContainerInfos(String port, ContainerInfo asInfo)
	{
		Catalog.containerInfos.put(port, asInfo);
	}
	
	public static Map<String,ServiceInfo> getServices() 
	{
		return services;
	}
	
	public static void setServices(Map<String,ServiceInfo> services) 
	{
		if(Catalog.services == null)
		{
			Catalog.services = new HashMap<String,ServiceInfo>();
		}
		Catalog.services.putAll(services);
	}
	
	public static Map<String,String[]> getDataSources() 
	{
		return dataSources;
	}
	
	public static void setDataSources(Map<String,String[]> dataSources) 
	{
		Catalog.dataSources = dataSources;
	}
}
