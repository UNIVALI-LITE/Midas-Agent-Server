package org.midas.as.manager.manager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.midas.as.agent.board.Board;
import org.midas.as.agent.templates.Agent;
import org.midas.as.broker.Broker;
import org.midas.as.broker.BrokerException;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.Logger;
import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.execution.ServiceWrapperException;
import org.midas.as.manager.manager.tomcat.TomcatWrapper;
import org.midas.as.manager.tasks.StatisticsTask;
import org.midas.as.manager.tasks.SynchronizerTask;
import org.midas.as.proxy.Factory;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.NativeAgentInfo;
import org.midas.metainfo.OrganizationInfo;



public class Manager 
{
	// Singleton
	private static Manager manager;
	
	// Variáveis de estado
	private static boolean initialized = false;
	private static boolean connected   = false;
	private static boolean agentsAlive = false;
	private static boolean stopping;
	
	// Variáveis que Manipulam o Tomcat
	private final static TomcatWrapper tomcat = new TomcatWrapper();    
		
	// Variáveis que guarda as Rotinas de Funcionamento
	private static List<FutureTask>  	  onlineTasks 	  = new ArrayList<FutureTask>(10);
	private static ExecutorService 		  onlineTasksPool = Executors.newFixedThreadPool(1);
	
	private static List<FutureTask>  	  standAloneTasks 	  = new ArrayList<FutureTask>(10);
	private static ExecutorService 		  standAloneTasksPool = Executors.newFixedThreadPool(1);
		
	private static Map<String,Agent>   			  lifeCycleAgents	  = new HashMap<String,Agent>(); 
	private static Map<String,FutureTask<Object>> lifeCycleTasks      = new HashMap<String,FutureTask<Object>>();
	private static ExecutorService 		  		  lifeCycleTasksPool  = Executors.newCachedThreadPool();
			
	/**
	 * Construtor privado para forçar o uso do padrão singleton
	 */ 
	private Manager()
	{
		
	}
	
	/**
	 * Método que retorna uma instância única de Manager
	 */
	public static Manager getInstance()
	{
		if (manager == null)
		{
			manager = new Manager();
		}
		
		return manager;
	}
	
	/**
	 * Rotina de inicialização do MAS. Se utiliza do modelo de recursos para 
	 * recuperar a lista de serviços, o URL do servidor, e o nome do MAS 
	 * e do modelo de mensagens para efetivar o cadastramento do MAS no TS.
	 */
	public void initialize() throws ManagerException
	{		
		// 1. Carrega o catálogo
		try
		{
			Catalog.loadCatalog();
		}
		catch(CatalogException e)
		{
			throw new ManagerException("Unable to load catalog",e);			
		}
		
		// 2. Carrega o Board
		Board.initializeBoard();
		
		// 3. Verifica Imagens
		File imagesDirectory = new File(Catalog.getContainerInfo().getPath()+"/images"); 
						
		if (!imagesDirectory.isDirectory())
		{
			throw new ManagerException("Unable to find userinterface images, check that path description on structure.xml points to a valid MIDAS Agent Server installation");
		}
						
		// 4. Inicializando tarefas StandAlone
		standAloneTasks.add(new FutureTask<Object>(new StatisticsTask()));
		
		// 5. Disparando tarefas StandAlone
		for (FutureTask task : standAloneTasks)
		{
			standAloneTasksPool.execute(task);
		}
		
		// 6. Sinalizando Estado
		stopping = false;
		initialized = true;
	}
	
	public void connect() throws ManagerException
	{
		// 1. Subindo o serviço do tomcat						
		try 
		{			
			File tomcatDirectory = new File(Catalog.getContainerInfo().getPath()+"/tomcat"); 
			
			if (tomcatDirectory.isDirectory())
				tomcat.start(Catalog.getContainerInfo().getPath()+"/tomcat",Integer.parseInt(Catalog.getContainerInfo().getContainerPort()));
			else
			{
				throw new ManagerException("Unable to find tomcat engine, check that path description on structure.xml points to a valid MIDAS Agent Server installation");
			}
		} 
		catch (Exception e) 
		{
			throw new ManagerException("Unable to start tomcat, check that port "+Catalog.getContainerInfo().getContainerPort()+" is free.",e);
		}
			
		// 2. Registrando no MAS
		try 
		{
			Broker.registerOnServer();
		} 
		catch (BrokerException e) 
		{
			try
			{
				tomcat.stop();
			}
			catch (Exception e1){}
			
			throw new ManagerException("Unable to register on MAS",e);
		}
				
		// 3. Inicializando tarefas online
		onlineTasks.add(new FutureTask<Object>(new SynchronizerTask()));
								
		// 4. Disparando tarefas online
		for (FutureTask task : onlineTasks)
		{
			onlineTasksPool.execute(task);
		}		
		
		// 5. Atualizando Variáveis de Estado
		connected = true;
		
		// 6. Atualizando Interface com o Usuário		
		Logger.addEntry("Connected on server");
		ManagerScreen.userInterfaceEvent("Connected");				
	}
	
	public void disconnect(boolean intentional)
	{		
		// 1. Atualizando variáveis de estado
		stopping=true;
		connected=false;
		
		// 2. Avisando Finalização se Necessário
		if (!intentional)			
			Logger.addEntry("Disconnected from server - Appears to be offline or out of synchronization");
		else
			Logger.addEntry("Disconnected from server");
		
		// 3. Atualizando Interface com o Usuário		
		ManagerScreen.userInterfaceEvent("Disconnected");
		
		// 4. Terminando tarefas online
		for (FutureTask task : onlineTasks)
		{
			if ( (!task.isCancelled()) && (!task.isDone()) )
				task.cancel(true);
		}

		// 5. Parando serviço do tomcat
		try 
		{
			tomcat.stop();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	
	
	public void reset() throws ManagerException
	{
		disconnect(true);
		connect();		
	}
		
	public void wakeAgents() throws ManagerException
	{
		// 1. Inicializa Ciclo de Vida dos Agentes
		Set<OrganizationInfo> orgs = Catalog.getContainerInfo().getOrganizations();
		
		for (OrganizationInfo org : orgs)
		{
			String orgPackage = org.getPackageName();
			
			Set<EntityInfo> entities = org.getEntities();
			
			for(EntityInfo ent : entities)
			{				
				if(ent.getType().equals("agent") && ent.getProtocol().equals("native"))
				{
					String entPackage = ((NativeAgentInfo)ent).getPackageName();
					String entClassName = ((NativeAgentInfo)ent).getClassName();
					
					try
					{
						
						Agent agent = (Agent)(Factory.getInstance().instantiateEntity(orgPackage+"."+entPackage+"."+entClassName));
						lifeCycleAgents.put(agent.getClass().toString().substring(6),agent);
						lifeCycleTasks.put(agent.getClass().toString().substring(6),new FutureTask<Object>(agent));										
					}
					catch (Exception e)
					{	
						throw new ManagerException("Unable to start agents life cycle, could not instantiate "+entClassName,e);											
					}
				}
			}			
		}
			
		for (FutureTask task : lifeCycleTasks.values())
		{
			lifeCycleTasksPool.execute(task);		
		}
		
		agentsAlive = true;
	}
	
	public void killAgents() throws ManagerException
	{
		for (Agent agent : lifeCycleAgents.values())
		{				
			agent.alive = false;
		}
		
		try 
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}
		
		for (FutureTask task : lifeCycleTasks.values())
		{
			if (!task.isDone())
			{
				task.cancel(true);
			}
		}
		
		for (FutureTask task : lifeCycleTasks.values())
		{
			if (!(task.isDone()||task.isCancelled()))
			{
				// TODO: Melhorar espeficação do erro
				throw new ManagerException("Unable to terminate all the agents, please re-start the container, and check the lyfe-cycle implementations.");
			}
		}
		
		agentsAlive = false;
	}
	
	public boolean isInitialized()
	{
		return initialized;
	}
	
	public boolean isStopping()
	{		
		return stopping;		
	}
	
	public boolean isConnected()
	{
		return connected;
	}	
	
	public boolean isAgentsAlive()
	{
		return agentsAlive;
	}
		
	public ServiceWrapper obtainService(String organization,String service,Agent agent) 
		throws ServiceWrapperException
	{
		if ( !Catalog.hasService(organization,service) )
		{
			try
			{
				if ( !isConnected() || !Broker.checkServerForService(organization,service) )
					throw new ServiceWrapperException("Invalid service -"+service+"- " +
													  "for organization -"+organization+"-");
			}			
			catch (BrokerException e)
			{

				throw new ServiceWrapperException("Invalid service -"+service+"- for " +
												  "organization -"+organization+"-",e);
			}
		}	
		
		return(new ServiceWrapper(organization,service,agent));		
	}

	public void exit()
	{
		System.exit(0);		
	}
}

