package org.midas.as.manager.manager;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.ExecutionPool;
import org.midas.as.manager.manager.listeners.BuildListener;
import org.midas.as.manager.manager.listeners.MainListener;
import org.midas.as.manager.manager.listeners.ViewListener;
import org.midas.metainfo.ContainerInfo;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.MetaInfoException;
import org.midas.metainfo.NativeAgentInfo;
import org.midas.metainfo.NativeComponentInfo;
import org.midas.metainfo.OrganizationInfo;
import org.midas.metainfo.ServiceInfo;

import thinlet.FrameLauncher;
import thinlet.Thinlet;

public class ManagerScreen
{
	// Manager
	private static Manager manager = Manager.getInstance();
	
	// Vari�veis de Estado
	private static boolean hidden = true;
	
	// Vari�veis do Thinlet
	private static Object  mainScreen;
	private static Object  viewScreen;
	private static Object  buildScreen;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static FrameLauncher frame;
	private static Thinlet 		 thinlet;
				
	// �cones
	private static Image agentIcon;
	private static Image folderIcon;
	private static Image serviceIcon;
	private static Image componentIcon;
	private static Image organizationIcon;
	
	
	public static void show() throws ManagerException
	{
		// 1. Montando Tela de Visualiza��o
		thinlet = new Thinlet();
				
		try 
		{			
			// 2. Renderizando Arquivos XUL
			mainScreen  = thinlet.parse("/org/midas/as/manager/manager/screens/main.xul",new MainListener());
			viewScreen  = thinlet.parse("/org/midas/as/manager/manager/screens/view.xul",new ViewListener());
			buildScreen = thinlet.parse("/org/midas/as/manager/manager/screens/build.xul",new BuildListener());
						
			// 3. Relacionando as Telas
			thinlet.add(mainScreen);
			thinlet.add(thinlet.find(mainScreen,"viewTab"),viewScreen);
			thinlet.add(thinlet.find(mainScreen,"buildTab"),buildScreen);
						
			// 4. Disparando Frame AWT			
			frame = new FrameLauncher("Agent Server Manager",thinlet,500,500);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

			// 5. Acertando Estado da Tela
			hidden = false;
			
			// 5. Sincronizando com estado do AS
			if (manager.isInitialized())
			{
				// Verifica se os �cones j� foram carregados
				if (organizationIcon == null)
				{
					URL organizationIconFile = ManagerScreen.class.getResource("/images/organizationIcon.gif");
					URL componentIconFile = ManagerScreen.class.getResource("/images/componentIcon.gif");
					URL agentIconFile = ManagerScreen.class.getResource("/images/agentIcon.gif");
					URL serviceIconFile = ManagerScreen.class.getResource("/images/serviceIcon.gif");
					URL folderIconFile = ManagerScreen.class.getResource("/images/folderIcon.gif");

					organizationIcon = tk.getImage(organizationIconFile);
					componentIcon = tk.getImage(componentIconFile);
					agentIcon = tk.getImage(agentIconFile);
					serviceIcon = tk.getImage(serviceIconFile);
					folderIcon = tk.getImage(folderIconFile);					
				}
				
				userInterfaceEvent("Refresh Services");			
			}									
		}
		catch (IOException e) 
		{
			throw new ManagerException("Unable to start Manager - could not find all the XUL files",e);
		}				
	}
	
	public static void userInterfaceEvent(String event)
	{
		if (!hidden)
		{
			if (event.equals("Refresh Services"))
			{
				refreshServices();
				refreshDetails();
			}		
			else if (event.equals("Refresh Logger"))
			{
				
			}
			else if (event.equals("Refresh Statistics"))
			{
				refreshStatistics();
			}
			else if (event.equals("Refresh Details"))
			{
				refreshDetails();
			}
			else if (event.equals("Connected"))
			{
				String path = Catalog.getContainerInfo().getPath();
				
				// Verifica se os �cones j� foram carregados
				if (organizationIcon == null)
				{				
					URL organizationIconFile = ManagerScreen.class.getResource("/images/organizationIcon.gif");
					URL componentIconFile = ManagerScreen.class.getResource("/images/componentIcon.gif");
					URL agentIconFile = ManagerScreen.class.getResource("/images/agentIcon.gif");
					URL serviceIconFile = ManagerScreen.class.getResource("/images/serviceIcon.gif");
					URL folderIconFile = ManagerScreen.class.getResource("/images/folderIcon.gif");

					organizationIcon = tk.getImage(organizationIconFile);
					componentIcon = tk.getImage(componentIconFile);
					agentIcon = tk.getImage(agentIconFile);
					serviceIcon = tk.getImage(serviceIconFile);
					folderIcon = tk.getImage(folderIconFile);													
				}
				
				refreshConnection();
			}
			else if (event.equals("Disconnected"))
			{
				refreshDetails();
				refreshServices();				
				refreshStatistics();
				refreshConnection();
			}
			else if (event.equals("Agents Woken"))
			{
				Object wakeMenuItem = thinlet.find(mainScreen,"wakeMenuItem");
				Object killMenuItem = thinlet.find(mainScreen,"killMenuItem");
				
				thinlet.setBoolean(wakeMenuItem,"enabled",false);
				thinlet.setBoolean(killMenuItem,"enabled",true);
			}
			else if (event.equals("Agents Killed"))
			{
				Object wakeMenuItem = thinlet.find(mainScreen,"wakeMenuItem");
				Object killMenuItem = thinlet.find(mainScreen,"killMenuItem");
				
				thinlet.setBoolean(wakeMenuItem,"enabled",true);
				thinlet.setBoolean(killMenuItem,"enabled",false);				
			}
		}
	}
	
	public static void refreshBuildDetails(Object select) 
	{
		if (hidden)
		{
			return;
		}
		
		Object containerNameField = thinlet.find(buildScreen,"containerNameField");
		Object containerPathField = thinlet.find(buildScreen,"containerPathField");
		Object containerServerField = thinlet.find(buildScreen,"containerServerField");
		Object organizationComboBox = thinlet.find(buildScreen,"organizationComboBox");
		Object organizationNameField = thinlet.find(buildScreen,"organizationNameField");
		Object organizationPackageField = thinlet.find(buildScreen,"organizationPackageField");
		Object entityComboBox = thinlet.find(buildScreen,"entityComboBox"); 
		Object entityProtocolComboBox = thinlet.find(buildScreen,"entityProtocolComboBox");
		Object entityNameField = thinlet.find(buildScreen,"entityNameField");
		Object entityTypeComboBox = thinlet.find(buildScreen,"entityTypeComboBox");
		Object entityPackageField = thinlet.find(buildScreen,"entityPackageField");
		Object entityClassField = thinlet.find(buildScreen,"entityClassField");
		Object serviceComboBox = thinlet.find(buildScreen,"serviceComboBox");
		Object serviceNameField = thinlet.find(buildScreen,"serviceNameField");
		Object serviceScopeField = thinlet.find(buildScreen,"serviceScopeField");
		Object servicePathField = thinlet.find(buildScreen,"servicePathField");
		Object parameterTable = thinlet.find(buildScreen,"parameterTable");
		
		thinlet.setString(containerNameField,"text","");
		thinlet.setString(containerPathField,"text","");
		thinlet.setString(containerServerField,"text","");
		thinlet.setString(organizationComboBox,"text","");
		thinlet.setString(organizationNameField,"text","");
		thinlet.setString(organizationPackageField,"text","");
		thinlet.setString(entityComboBox,"text","");
		thinlet.setString(entityProtocolComboBox,"text","");
		thinlet.setString(entityNameField,"text","");
		thinlet.setString(entityTypeComboBox,"text","");
		thinlet.setString(entityPackageField,"text","");
		thinlet.setString(entityClassField,"text","");
		thinlet.setString(serviceComboBox,"text","");
		thinlet.setString(serviceNameField,"text","");
		thinlet.setString(serviceScopeField,"text","");
		thinlet.setString(servicePathField,"text","");
		thinlet.removeAll(parameterTable);
		
		String[] selection;
		
		try
		{
			Object selectedItem    = thinlet.getSelectedItem(select);
			String selectionString = thinlet.getString(selectedItem,"name"); 
			selection = selectionString.split("-");
						
			// Preenchendo o Painel			
			if (selection!=null && !selection[0].equals("folder"))
			{			
				ContainerInfo    ci = Catalog.getContainerInfo();
				OrganizationInfo oi = Catalog.getOrganizationByName(selection[1]);
				
				thinlet.setString(containerNameField,"text",ci.getName());
				thinlet.setString(containerPathField,"text",ci.getPath());
				thinlet.setString(containerServerField,"text",ci.getServerAddress()+":"+ci.getServerPort());
				
				thinlet.setString(organizationComboBox,"text",oi.getName());
				thinlet.setString(organizationNameField,"text",oi.getName());
				thinlet.setString(organizationPackageField,"text",oi.getPackageName());
				
				if (selection[0].equals("entity"))
				{
					EntityInfo ei = Catalog.getEntityByName(selection[1],selection[2]);
					
					thinlet.setString(entityComboBox,"text",ei.getName());
					thinlet.setString(entityProtocolComboBox,"text",ei.getProtocol());
					thinlet.setString(entityNameField,"text",ei.getName());
					thinlet.setString(entityTypeComboBox,"text",ei.getType());
					
					if (ei.getType().equals("agent"))
					{
						thinlet.setString(entityPackageField,"text",((NativeAgentInfo)ei).getPackageName());
						thinlet.setString(entityClassField,"text",((NativeAgentInfo)ei).getClassName());
					}
					else
					{
						thinlet.setString(entityPackageField,"text",((NativeComponentInfo)ei).getPackageName());
						thinlet.setString(entityClassField,"text",((NativeComponentInfo)ei).getClassName());
					}
						
					ServiceInfo si = (ServiceInfo)ei.getServices().iterator().next();
					
					thinlet.setString(serviceComboBox,"text",si.getName());
					thinlet.setString(serviceNameField,"text",si.getName());
					thinlet.setString(serviceScopeField,"text",si.getScope());
					thinlet.setString(servicePathField,"text",si.getPath());	
				}
			}
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		} 
		catch (MetaInfoException e) 
		{
			e.printStackTrace();
		}
	}

	public static void notifyUser(String message)
	{
		if (!hidden)
		{
			Object logList = thinlet.find(viewScreen,"logList");
			Object logEntry = Thinlet.create("item");
			
			thinlet.setString(logEntry,"text",new GregorianCalendar().getTime()+" | "+message);			
			thinlet.add(logList,logEntry);
		}
		else
		{
			System.out.println(message);
		}
	}
	
	public static void notifyUser(String message, boolean success)
	{
		if (!hidden)
		{
			Object logList = thinlet.find(viewScreen,"logList");
			Object logEntry = Thinlet.create("item");
			
			if (success)
			{
				thinlet.setIcon(logEntry,"icon",tk.getImage(Catalog.getContainerInfo().getPath()+"/images/serviceOk.gif"));
			}
			else
			{
				thinlet.setIcon(logEntry,"icon",tk.getImage(Catalog.getContainerInfo().getPath()+"/images/serviceError.gif"));
			}
			
			thinlet.setString(logEntry,"text",new GregorianCalendar().getTime()+" | "+message);			
			thinlet.add(logList,logEntry);
		}
		else
		{
			System.out.println(message);
		}	
	}
			
	private static void refreshDetails()
	{
		if (hidden)
		{
			return;
		}
		else if (!manager.isInitialized())
		{
			thinlet.setString(thinlet.find(viewScreen,"detailsTextArea"),"text","");			
		}
		else
		{
			// Vari�veis Thinlet 
			Object thinletDetails      = thinlet.find(viewScreen,"detailsTextArea");
			Object thinletServicesTree = thinlet.find(viewScreen,"servicesTree");
			
			// Vari�veis de Informa��o
			String            info="";
			EntityInfo		  entity;
			ServiceInfo       service;
			OrganizationInfo  organization;
			
			// Obtendo Strings com as Informa��es da Sele��o			
			String[] selection = null;
			
			try
			{
				Object selectedItem    = thinlet.getSelectedItem(thinletServicesTree);
				String selectionString = thinlet.getString(selectedItem,"name"); 
				selection = selectionString.split("-");
			}
			catch(IllegalArgumentException e){}
			
			// Limpando a �rea de Texto
			thinlet.removeAll(thinletDetails);
			
			// Preenchendo o Painel			
			if (selection!=null && !selection[0].equals("folder"))
			{				
				try 
				{
					organization = Catalog.getOrganizationByName(selection[1]);
					
					if (selection[0].equals("organization"))
					{
						info = organization.toString();
					}
					else if (selection[0].equals("entity"))
					{
						entity = Catalog.getEntityByName(organization.getName(),selection[2]);
						info   = entity.toString();
					}
					else if (selection[0].equals("service"))
					{
						service = Catalog.getServiceByName(organization.getName(),selection[2]);
						info    = service.toString();
					}										
				} 
				catch (CatalogException e) 
				{
					notifyUser("unable to display details due to severe error on the consistence of information about the structure, recommended to check structure.xml/services.xml and re-start the server");
					return;				
				}
				catch (MetaInfoException e)
				{
					notifyUser("unable to display details due to severe error on the consistence of information about the structure, recommended to check structure.xml/services.xml and re-start the server");
					return;				
				}		
			}
			else
			{
				if(selection!=null)
					info = selection[1]+" folder";
			}
			
			thinlet.setString(thinletDetails,"text",info);
		}
	}
	
	private static void refreshServices()
	{
		if (hidden)
		{
			return;
		}
		else if (!manager.isInitialized())
		{
			thinlet.removeAll(thinlet.find(viewScreen,"servicesTree"));
			thinlet.removeAll(thinlet.find(buildScreen,"structureTree"));		
		}
		else
		{			
			// Vari�veis Thinlet
			Object thinletStyleSelection = thinlet.find(viewScreen,"viewCheckBox");
			Object thinletServicesTree = thinlet.find(viewScreen,"servicesTree");
			Object thinletBuildTree = thinlet.find(buildScreen,"structureTree");
			
			// Vari�veis Thinlet Auxiliares
			Object thinletOrgNode;
			Object thinletAgentsNode;
			Object thinletComponentsNode;
			Object thinletEnttNode;
			Object thinletServNode;
			
			// Vari�veis de Informa��o
			Set<ServiceInfo> 	  services;
			Set<EntityInfo> 	  entities;
			Set<OrganizationInfo> organizations;
			
			EntityInfo		 enttInfo;
			ServiceInfo		 servInfo;		
			OrganizationInfo orgInfo;
							
			// Limpando as �rvores
			thinlet.removeAll(thinletServicesTree);
			thinlet.removeAll(thinletBuildTree);
			
			// Preenchendo �rvore de Servi�os
			if ((thinlet.getBoolean(thinletStyleSelection,"selected")))
			{
				// Varrendo Organiza��es
				organizations = Catalog.getContainerInfo().getOrganizations();
					
				for (Iterator iter = organizations.iterator();iter.hasNext();)
				{
					orgInfo = (OrganizationInfo)iter.next();
					
					// Criando N� Thinlet
					thinletOrgNode = Thinlet.create("node");
					thinlet.setIcon(thinletOrgNode,"icon",organizationIcon);
					thinlet.setString(thinletOrgNode,"text",orgInfo.getName());
					thinlet.setString(thinletOrgNode,"name","organization-"+orgInfo.getName());
					
					
					// Adicionando N� � �rvore
					thinlet.add(thinletServicesTree,thinletOrgNode);
					
					// Criando N�s Separadores de Agentes/Componentes
					thinletAgentsNode = Thinlet.create("node");
					thinlet.setString(thinletAgentsNode,"text","/Agents");
					thinlet.setString(thinletAgentsNode,"name","folder-"+"agents");
					thinlet.setIcon(thinletAgentsNode,"icon",folderIcon);
					thinletComponentsNode = Thinlet.create("node");
					thinlet.setString(thinletComponentsNode,"text","/Components");
					thinlet.setString(thinletComponentsNode,"name","folder-"+"components");
					thinlet.setIcon(thinletComponentsNode,"icon",folderIcon);
					
					// Adicionando N�s Separadores � Organiza��o
					thinlet.add(thinletOrgNode,thinletAgentsNode);
					thinlet.add(thinletOrgNode,thinletComponentsNode);
					
					// Varrendo Entidades
					entities = orgInfo.getEntities();
						
					for (Iterator j=entities.iterator();j.hasNext();)
					{
						enttInfo = (EntityInfo)j.next();
							
						// Criando N� Thinlet
						thinletEnttNode = Thinlet.create("node");
						thinlet.setString(thinletEnttNode,"text",enttInfo.getName());
						thinlet.setString(thinletEnttNode,"name","entity-"+orgInfo.getName()+"-"+enttInfo.getName());
							
						// Adicionando N� ao N� Pai
						if ((enttInfo.getType()).equals("agent"))
						{
							thinlet.setIcon(thinletEnttNode,"icon",agentIcon);
							thinlet.add(thinletAgentsNode,thinletEnttNode);
						}
						else
						{
							thinlet.setIcon(thinletEnttNode,"icon",componentIcon);
							thinlet.add(thinletComponentsNode,thinletEnttNode);
						}
												
						// Varrendo Servi�os
						services = enttInfo.getServices();
							
						for (Iterator k=services.iterator();k.hasNext();)
						{
							servInfo = (ServiceInfo)k.next();
								
							// Criando N� Thinlet
							thinletServNode = Thinlet.create("node");							
							thinlet.setIcon(thinletServNode,"icon",serviceIcon);
							thinlet.setString(thinletServNode,"text",servInfo.getName());
							thinlet.setString(thinletServNode,"name","service-"+orgInfo.getName()+"-"+servInfo.getName());
								
							// Adicionando N� ao N� Pai
							thinlet.add(thinletEnttNode,thinletServNode);	
						}
					}						
				}
			}
			else
			{
				// Varrendo Servi�os
				Collection<ServiceInfo> servs = Catalog.getServices().values();
				services = new TreeSet<ServiceInfo>(servs);
				
				for (Iterator i = services.iterator();i.hasNext();)
				{
					servInfo = (ServiceInfo)i.next();
					
					// Criando N� Thinlet
					thinletServNode = Thinlet.create("node");
					
					StringBuilder thinletText = new StringBuilder();
					
					thinletText.append(servInfo.getName());
					thinletText.append(" ( ");
					thinletText.append(servInfo.getEntity().getName());
					thinletText.append(" - ");
					thinletText.append(servInfo.getEntity().getOrganization().getName());
					thinletText.append(" ) ");
					
					thinlet.setIcon(thinletServNode,"icon",serviceIcon);
					thinlet.setString(thinletServNode,"text",thinletText.toString());
					thinlet.setString(thinletServNode,"name","service-"+servInfo.getEntity().getOrganization().getName()+"-"+servInfo.getName());
										
					thinlet.add(thinletServicesTree,thinletServNode);
				}
			}	
			
			// Preenchendo �rvore de Constru��o
			organizations = Catalog.getContainerInfo().getOrganizations();
				
			for (Iterator iter = organizations.iterator();iter.hasNext();)
			{
				orgInfo = (OrganizationInfo)iter.next();
				
				// Criando N� Thinlet
				thinletOrgNode = Thinlet.create("node");
				thinlet.setString(thinletOrgNode,"text",orgInfo.getName());
				thinlet.setIcon(thinletOrgNode,"icon",organizationIcon);
				thinlet.setString(thinletOrgNode,"name","organization-"+orgInfo.getName());
				
				
				// Adicionando N� � �rvore
				thinlet.add(thinletBuildTree,thinletOrgNode);
				
				// Varrendo Entidades
				entities = orgInfo.getEntities();
					
				for (Iterator j=entities.iterator();j.hasNext();)
				{
					enttInfo = (EntityInfo)j.next();
						
					// Criando N� Thinlet
					thinletEnttNode = Thinlet.create("node");
					
					StringBuilder thinletText = new StringBuilder();
					
					thinletText.append(enttInfo.getName());				
					
					thinlet.setString(thinletEnttNode,"text",thinletText.toString());
					thinlet.setString(thinletEnttNode,"name","entity-"+orgInfo.getName()+"-"+enttInfo.getName());
					
					if (enttInfo.getType().equals("agent"))
					{
						thinlet.setIcon(thinletEnttNode,"icon",agentIcon);
					}
					else
					{
						thinlet.setIcon(thinletEnttNode,"icon",componentIcon);
					}
					
					// Adicionando N� ao N� Pai
					thinlet.add(thinletOrgNode,thinletEnttNode);										
				}
			}
		}
	}

	private static void refreshStatistics()
	{
		if (hidden)
		{
			return;
		}
		else if (!manager.isInitialized())
		{
			// Vari�veis Thinlet
			Object thinletThreadField = thinlet.find(viewScreen,"threadCount");
			Object thinletMemoryField = thinlet.find(viewScreen,"memoryUsage");
			
			// Populando a Tela
			thinlet.setString(thinletThreadField,"text","");
			thinlet.setString(thinletMemoryField,"text","");
		}
		else
		{			
			// Vari�veis Thinlet
			Object thinletThreadField = thinlet.find(viewScreen,"threadCount");
			Object thinletMemoryField = thinlet.find(viewScreen,"memoryUsage");
			
			// Obtendo Informa��es
			Long memoryUsage = ExecutionPool.memoryUsage();
					
			// Populando a Tela
			thinlet.setString(thinletThreadField,"text",new Long(ExecutionPool.getActiveThreads()).toString());
			thinlet.setString(thinletMemoryField,"text",memoryUsage.toString());
		}
	}
	
	private static void refreshConnection()
	{
		if (hidden)
		{
			return;
		}
		else if (manager.isConnected())
		{			
			Object offlineLabel  = thinlet.find(viewScreen,"statusOffline");
			Object onlineLabel   = thinlet.find(viewScreen,"statusOnline");
			Object startMenuItem = thinlet.find(mainScreen,"startMenuItem");
			Object stopMenuItem  = thinlet.find(mainScreen,"stopMenuItem");
			Object resetMenuItem = thinlet.find(mainScreen,"resetMenuItem");
			
			thinlet.setBoolean(offlineLabel,"visible",false);
			thinlet.setBoolean(onlineLabel,"visible",true);
			thinlet.setBoolean(startMenuItem,"enabled",false);
			thinlet.setBoolean(stopMenuItem,"enabled",true);
			thinlet.setBoolean(resetMenuItem,"enabled",true);
		}
		else
		{
			Object offlineLabel = thinlet.find(viewScreen,"statusOffline");
			Object onlineLabel  = thinlet.find(viewScreen,"statusOnline");
			Object startMenuItem = thinlet.find(mainScreen,"startMenuItem");
			Object stopMenuItem  = thinlet.find(mainScreen,"stopMenuItem");
			Object resetMenuItem = thinlet.find(mainScreen,"resetMenuItem");
			
			thinlet.setBoolean(onlineLabel,"visible",false);
			thinlet.setBoolean(offlineLabel,"visible",true);
			thinlet.setBoolean(stopMenuItem,"enabled",false);
			thinlet.setBoolean(startMenuItem,"enabled",true);
			thinlet.setBoolean(resetMenuItem,"enabled",false);
		}
	}
}
