/**
 * Created on 28/12/2004
 * @author Aluizio Haendchen Filho
 */

package org.midas.as.catalog;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.midas.as.proxy.Factory;
import org.midas.as.proxy.Proxy;
import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.DataSourceInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.MetaInfoException;
import org.midas.metainfo.NativeAgentInfo;
import org.midas.metainfo.NativeComponentInfo;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ParameterInfo;
import org.midas.metainfo.ServiceInfo;
import org.midas.metainfo.WebServiceComponentInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Executa os servi�os de acessar e extrair informa��es do arquivo XML. Ela se
 * utiliza da biblioteca JDOM, que mapeia arquivos XML para um objeto, investi-
 * gando ent�o os registros de servi�os e estrutura. Estes registros s�o encapsu-
 * lados respectivamente em objetos Service (que cont�m o nome e o caminho do servi�o)
 * e em um HashMap. Para tornar a recupera��o mais r�pida, os servi�os s�o inseridos 
 * em um objeto HashMap, utilizando os pr�prios nomes como �ndice.
 */
public class Parser
{
	/**
	 * o m�todo loadStructure percorre o documento structure.XML usando a 
	 * biblioteca javax.xml.parsers, e o utilit�rio DOM. Opera mapeando em 
	 * objetos as informacoes contidas nos n�s do documento. Os objetos 
	 * recuperados do documento XML s�o armazenados em um HashMap. 
	 */
	public static synchronized void parse(String structureXML, String servicesXML) throws ParserException
	{
		// Inicializando Vari�veis do Catalog
		Document 	  doc;
		ContainerInfo containerInfo;
		
		Map<String,ServiceInfo> services    = new HashMap<String,ServiceInfo>();
		Map<String,String[]>    dataSources = new HashMap<String,String[]>();
								
		// Carregando documento XML
		//doc = loadDocument("structure.xml");
		doc = convertStringToDocument(structureXML);
		// Varrendo o Documento e Montando a Estrutura
		try
		{
			/*********************************
			 * 1. VARRENDO TAG "agentserver" *
			 *********************************/
			
			// Tag agentserver
			Element tagContainer = doc.getDocumentElement();
			
			// Vari�veis do Agent Server
			String containerName 	  = getChildTagValue( tagContainer, "name" );
			String containerPath      = new File("").getAbsolutePath();						
			
			String containerAddress	  = InetAddress.getLocalHost().getHostAddress();
			String containerPort	  = getChildTagValue( tagContainer, "localPort");
			
			String serverAddress 	  = getChildTagValue( tagContainer, "serverAddress" );
			String serverPort 		  = getChildTagValue( tagContainer, "serverPort" );																		
				
			// Construindo Carregador de Classes
			String containerClassPath = ".";
			
			try	{
				containerClassPath = getChildTagValue( tagContainer, "classPath");				
			}catch(Exception e){}
			
			Proxy.getInstance().loadClasses(containerClassPath);
			
			// Adicionando ao Cat�logo
			containerInfo = new ContainerInfo(containerName,"as",containerPath,containerAddress,containerPort,serverAddress,serverPort);			
			
			/*********************************
			 * 2. VARRENDO TAG "datasources" *
			 *********************************/
						
			// Tag datasources 
			NodeList nlData  = tagContainer.getElementsByTagName("data");
			Element  tagData = (Element) nlData.item(0);
			
			if (tagData != null)
			{
				NodeList nlDataSource   = tagData.getElementsByTagName("dataSource");
				
				for( int i=0 ; i<nlDataSource.getLength() ; i++ ) 
				{
					// Tag datasource
					Element  tagDataSource = (Element) nlDataSource.item(i);
					
					// Vari�veis de um DataSource
					String sourceName       = getChildTagValue(tagDataSource,"name");
					String sourceDriver     = getChildTagValue(tagDataSource,"driverString");
					String sourceConnection = getChildTagValue(tagDataSource,"connectionString");
					
					// Adicionando ao ASInfo
					containerInfo.addDataSource(new DataSourceInfo(sourceName,sourceDriver,sourceConnection));
					
					// Adicionando ao Cat�logo
					String[] sourceInfo = {sourceDriver,sourceConnection};
					dataSources.put(sourceName,sourceInfo);
				}
			}
			
			/***********************************
			 * 3. VARRENDO TAG "organizations" *
			 ***********************************/
			
			// Tag organizations
			NodeList nlOrganizations  = tagContainer.getElementsByTagName("organizations");
			Element  tagOrganizations = (Element) nlOrganizations.item(0);
			
			NodeList nlOrganization   = tagOrganizations.getElementsByTagName("organization");
			
			for( int i=0 ; i<nlOrganization.getLength() ; i++ ) 
			{
				// Tag organization
				Element tagOrganization = (Element) nlOrganization.item(i);
				
				// Vari�veis de um Organization
				String orgName	  = getChildTagValue(tagOrganization,"name");
				String orgPackage = getChildTagValue(tagOrganization, "package");
				
				// Adicionando ao ASInfo
				OrganizationInfo org = new OrganizationInfo(orgName,orgPackage,containerInfo);
				containerInfo.addOrganization(org);
				
				/*****************************
				 * 3.1 VARRENDO TAG "agents" *
				 *****************************/

				// Tag agents
				NodeList nlAgents	= tagOrganization.getElementsByTagName("agents");
				Element  tagAgents  = (Element)nlAgents.item(0);
				
				if (tagAgents != null)
				{				
					NodeList nlAgent	= tagAgents.getElementsByTagName("agent");
										
					for ( int j=0 ; j<nlAgent.getLength() ; j++ )
					{
						// Tag agent
						Element tagAgent = (Element)nlAgent.item(j);
						
						// Vari�vel da Entidade 
						EntityInfo entity;
						
						// Vari�veis de um Agent
						String agentName     = getChildTagValue(tagAgent,"name");						
						String agentProtocol = getChildTagValue(tagAgent,"protocol");
						
						// Testando Protocolo do Agente					
						if (agentProtocol.equals("native"))
						{
							// Recuperando Dados do Agente Nativo
							String agentClass   = getChildTagValue(tagAgent,"className");
							String agentPackage = getChildTagValue(tagAgent,"package");
							
							// Testando a exist�ncia do agente
							try
							{
								Factory.getInstance().loadClass((orgPackage+"."+agentPackage+"."+agentClass));
							}
							catch(ClassNotFoundException e)
							{
								throw new ParserException("Could not find the agent - "+agentName+" - check the given classpath for "+orgPackage+"."+agentPackage+"."+agentClass);
							}
							
							// Criando Entidade
							entity = new NativeAgentInfo (agentName,"agent",agentProtocol,agentClass,agentPackage,org);
						}
						else
						{
							throw new ParserException("Invalid protocol '"+agentProtocol+"' for agent '"+agentName+"'");
						}										
						
						// Adicionando Entidade a OrganizationInfo
						org.addEntity(entity); 	
					}
				}
				
				/*********************************
				 * 3.2 VARRENDO TAG "components" *
				 *********************************/
				
				// Tag components
				NodeList nlComponents  = tagOrganization.getElementsByTagName("components");
				Element  tagComponents = (Element)nlComponents.item(0);
				
				if (tagComponents != null)
				{				
					NodeList nlComponent	= tagComponents.getElementsByTagName("component");
					
					for ( int j=0 ; j<nlComponent.getLength() ; j++ )
					{
						// Tag component
						Element tagComponent = (Element)nlComponent.item(j);
						
						// Vari�vel da Entidade
						EntityInfo entity;
						
						// Vari�veis de um Component
						String componentName     = getChildTagValue(tagComponent,"name");
						String componentProtocol = getChildTagValue(tagComponent,"protocol");
						
						// Testando Protocolo do Componente					
						if (componentProtocol.equals("native"))
						{
							// Recuperando Dados do Agente Nativo
							String componentClass   = getChildTagValue(tagComponent,"className");
							String componentPackage = getChildTagValue(tagComponent,"package");
							
							// Testando a exist�ncia do agente
							try
							{
								Factory.getInstance().loadClass(orgPackage+"."+componentPackage+"."+componentClass);
							}
							catch(Exception e)
							{
								throw new ParserException("Cannot find the agent - "+componentName+" - check the classpath for "+orgPackage+"."+componentPackage+"."+componentClass);
							}
							
							// Criando Entidade
							entity = new NativeComponentInfo (componentName,"component",componentProtocol,componentClass,componentPackage,org);
						}
						else if (componentProtocol.equals("web_service"))
						{
							// Recuperando Dados do Agente Nativo
							String componentUrl   = getChildTagValue(tagComponent,"url");
							
							// Criando Entidade
							entity = new WebServiceComponentInfo (componentName,"component",componentProtocol,componentUrl,org);

						}
						else
						{
							throw new ParserException("Invalid protocol '"+componentProtocol+"' for agent '"+componentName+"'");
						}										
						
						// Adicionando Entidade a OrganizationInfo					
						org.addEntity(entity);										
					}
				}
			}
		}
		catch(ParserException e)
		{
			throw e;
		}
		catch(Exception e)
		{
			throw new ParserException("Unknown problem in parsing structure.xml, check it�s format.",e);
		} 
						
		// Carregando documento XML
		//doc = loadDocument("services.xml");
		doc = convertStringToDocument(servicesXML);
			
		try
		{
			/******************************
			 * 1. VARRENDO TAG "services" *
			 ******************************/
		
			// Tag services
			Element tagServices = doc.getDocumentElement();
			NodeList nlService  = tagServices.getElementsByTagName("service");
						
			if (tagServices != null)
			{
				for( int i=0 ; i<nlService.getLength() ; i++ )
				{
					// Tag service
					Element tagService = (Element)nlService.item(i);
					
					// Vari�veis de um Service
					String serviceName   	= getChildTagValue(tagService,"name");
					String serviceScope     = getChildTagValue(tagService,"scope");
					String organizationName = getChildTagValue(tagService,"organization");
					String entityName 		= getChildTagValue(tagService,"entity");
					String description	    = getChildTagValue(tagService,"description");
					
					// Inicializando Vari�veis �teis
					OrganizationInfo org	= containerInfo.getOrganizationByName(organizationName);
					EntityInfo 		 entity = org.getEntityByName(entityName);
					
					// Inicializando Vari�veis do Servi�o
					String path; 					
					String orgPackage	= org.getPackageName();
					
					if (entity.getType().equals("agent"))
					{
						if (entity.getProtocol().equals("native"))
						{
							String entPackage	= ((NativeAgentInfo)entity).getPackageName();
							String entClassName = ((NativeAgentInfo)entity).getClassName();
									
							path = (orgPackage+"."+entPackage+"."+entClassName);
						}
						else
						{
							throw new ParserException("Error while constructing service '"+serviceName+"' from organization '"+org.getName()+"' - The provider agent '"+entityName+"' has an unconsisting protocol '"+entity.getProtocol()+"'");
						}
					}
					else
					{
						if (entity.getProtocol().equals("native"))
						{
							String entPackage	= ((NativeComponentInfo)entity).getPackageName();
							String entClassName = ((NativeComponentInfo)entity).getClassName();
									
							path = (orgPackage+"."+entPackage+"."+entClassName);
						}
						else if (entity.getProtocol().equals("web_service")) 
						{
							String entUrl = ((WebServiceComponentInfo)entity).getUrl();
									
							path = (entUrl);
						}
						else
						{
							throw new ParserException("Error while constructing service '"+serviceName+"' from organization '"+org.getName()+"' - The provider agent '"+entityName+"' has an unconsisting protocol '"+entity.getProtocol()+"'");
						}
					}
					
					// Adicionado a Entity
					ServiceInfo service = new ServiceInfo(serviceName,path,serviceScope,description,entity);
					entity.addService(service);
					
					// Adicionando ao Cat�logo
					services.put(organizationName+"."+serviceName,service);
					
					/*********************************
					 * 1.1 VARRENDO TAG "parameters" *
					 *********************************/
					
					// Tag parameters
					NodeList nlParameters  = tagService.getElementsByTagName("parameters");
					Element  tagParameters = (Element)nlParameters.item(0);
					
					if (tagParameters != null)
					{
						NodeList nlParameter   = tagParameters.getElementsByTagName("parameter");
						
						for (int j=0; j<nlParameter.getLength();j++)
						{
							// Tag parameter
							Element tagParameter = (Element)nlParameter.item(j);
						
							// Vari�veis de um Parameter
							String parameterName  = getChildTagValue(tagParameter,"name");
							String paramClassName = getChildTagValue(tagParameter,"class");
							
							// TODO: Considerar quest�o do Array...
							// String array    	  = getChildTagValue(tagParameter,"array");
							
							// Vari�veis Auxiliares
							Class    paramClass = Class.forName(paramClassName);
							boolean  isArray    = false; // STUB... deveria ser gerado pela String array...
							
							// Adicionando a Service
							ParameterInfo parameter = new ParameterInfo(parameterName,paramClass,isArray);
							service.addParameter(parameter);
						}
					}
				}
			}
		}	
		catch (ParserException e)
		{	
			throw new ParserException("unknown problem in parsing structure.xml, check it�s format.",e);
		} 
		catch (MetaInfoException e) 
		{
			// TODO: Melhorar a exce��o avisando qual o servi�o mal-feito e qual o agente referenciado que n�o existe.
			throw new ParserException("services.xml references an entity that does not exist on structure.xml",e);
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO: Melhorar a exce��o avisando qual o servi�o mal-feito e qual a classe que n�o existe.
			throw new ParserException("invalid class declared at parameter tag in services.xml",e);
		}
								
		// Setando Catalog
		Catalog.setServices(services);
		Catalog.setDataSources(dataSources);
		Catalog.setContainerInfo(containerInfo);
    }	
	
	/**
	 * este metodo recupera o conteudo das tags dos n�s requeridos usando 
	 * o metodo getElementByTagName do DOM, que capura o conteudo da tag
	 * akjsallakjs
	 */
	private static Document loadDocument(String file) throws ParserException 
	{
		DocumentBuilderFactory dbf;
		DocumentBuilder db;
		Document doc;
		
		//Abrindo o Documento XML		 
		try
		{
			dbf = DocumentBuilderFactory.newInstance();
			db = dbf.newDocumentBuilder();
			doc = db.parse( file );
		}
		catch (ParserConfigurationException e)
		{
			throw new ParserException("Unexpected behavior from parsing driver",e);			
		} 
		catch (SAXException e) 
		{
			throw new ParserException("Malformed XML file - "+file,e);			
		} 
		catch (IOException e) 
		{
			throw new ParserException("Unable to find XML file - "+file,e);			
		}
		
		return doc;
	}

	/**
	 * este metodo recupera o conteudo das tags dos n�s requeridos usando 
	 * o metodo getElementByTagName do DOM, que capura o conteudo da tag
	 * akjsallakjs
	 */
	private static Document convertStringToDocument(String xmlStr) throws ParserException  {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;  
        try  
        {  
            builder = factory.newDocumentBuilder();  
            Document doc = builder.parse( new InputSource( new StringReader( xmlStr ) ) ); 
            return doc;
		}
		catch (ParserConfigurationException e)
		{
			throw new ParserException("Unexpected behavior from parsing driver",e);			
		} 
		catch (SAXException e) 
		{
			throw new ParserException("Malformed XML file - "+xmlStr,e);			
		} 
		catch (IOException e) 
		{
			throw new ParserException("Unable to find XML file - "+xmlStr,e);			
		}
    }
	
	/**
	 * este metodo recupera o conteudo das tags dos n�s requeridos usando 
	 * o metodo getElementByTagName do DOM, que capura o conteudo da tag
	 * @param elem objeto do tipo Element contendo os itens da tag
	 * @param tagName string nome da tag
	 * @return string contendo o conteudo do item da tag
	 * @throws Exception
	 */
	private static String getChildTagValue( Element elem, String tagName ) 
		throws ParserException
	{
    	NodeList children = elem.getElementsByTagName( tagName );
    	
    	if( children == null )
    	{
    		throw new ParserException("Invalid TAG - "+tagName);
    	}
    	
    	Element child = (Element) children.item(0);
    	
    	if( child == null )
    	{
    		throw new ParserException("Invalid TAG - "+tagName);
    	}
    	
    	return child.getFirstChild().getNodeValue();
  	}
}
