package org.midas.as.manager.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.midas.as.AgentServer;
import org.midas.as.agent.board.Board;
import org.midas.as.agent.templates.Agent;
import org.midas.as.broker.Broker;
import org.midas.as.broker.BrokerException;
import org.midas.as.broker.Receiver;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.execution.ServiceWrapperException;
import org.midas.as.manager.tasks.SynchronizerTask;
import org.midas.as.proxy.Factory;
import org.midas.metainfo.EntityInfo;
import org.midas.metainfo.NativeAgentInfo;
import org.midas.metainfo.OrganizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Manager {
	private static Logger LOG = LoggerFactory.getLogger(AgentServer.class);

	// Singleton
	private static Manager manager;

	// Variáveis de estado
	private static boolean initialized = false;
	private static boolean connected = false;
	private static boolean agentsAlive = false;
	private static boolean stopping;

	// Variáveis que guardam as Rotinas de Funcionamento
	private static Map<String, FutureTask> onlineTasks = new HashMap<>();
	private static ExecutorService onlineTasksPool = Executors.newFixedThreadPool(1);

	private static Map<String, Server> serversList = new HashMap<>();

	private static List<FutureTask> standAloneTasks = new ArrayList<FutureTask>(10);
	private static ExecutorService standAloneTasksPool = Executors.newFixedThreadPool(1);

	public static Map<String, Agent> lifeCycleAgents = new HashMap<String, Agent>();
	public static Map<String, FutureTask<Object>> lifeCycleTasks = new HashMap<String, FutureTask<Object>>();
	public static ExecutorService lifeCycleTasksPool = Executors.newCachedThreadPool();

	/**
	 * Construtor privado para forçar o uso do padrão singleton
	 */
	private Manager() {

	}

	/**
	 * Método que retorna uma instância única de Manager
	 */
	public static Manager getInstance() {
		if (manager == null) {
			manager = new Manager();
		}

		return manager;
	}

	/**
	 * Rotina de inicialização do MAS. Se utiliza do modelo de recursos para
	 * recuperar a lista de serviços, o URL do servidor, e o nome do MAS e do modelo
	 * de mensagens para efetivar o cadastramento do MAS no TS.
	 */
	public String initialize(String structureXML, String servicesXML) throws ManagerException {
		// 1. Carrega o catálogo
		String port;
		try {
			port = Catalog.loadCatalog(structureXML, servicesXML);
		} catch (CatalogException e) {
			throw new ManagerException("Unable to load catalog", e);
		}
		// 2. Carrega o Board
		Board.initializeBoard();

		// 4. Inicializa tarefas StandAlone
		// standAloneTasks.add(new FutureTask<Object>(new StatisticsTask()));

		// 5. Disparando tarefas StandAlone
		// for (FutureTask task : standAloneTasks)
		// {
		// standAloneTasksPool.execute(task);
		// }

		// 6. Sinalizando Estado
		stopping = false;
		initialized = true;

		try {
			Integer serverPort = Integer.valueOf(Catalog.getContainerInfos(port).getContainerPort());
			Server server = new Server(serverPort);

			ServletHandler handler = new ServletHandler();
			server.setHandler(handler);
			handler.addServletWithMapping(Receiver.class, "/agentserver/broker/receiver");

			server.start();
			serversList.put(port, server);
		} catch (Exception e) {
			throw new ManagerException("Unable to start Jetty, check that port "
					+ Catalog.getContainerInfos(port).getContainerPort() + " is free.", e);
		}

		return port;
	}

	public void connect(String port) throws ManagerException {
		// 1. Registrando no MAS
		try {
			Broker.registerOnServer(port);
		} catch (BrokerException e) {
			throw new ManagerException("Unable to register on MAS", e);
		}

		// 3. Inicializando tarefas online
		onlineTasks.put(port, new FutureTask<Object>(new SynchronizerTask(port)));

		// 4. Disparando tarefas online
		for (FutureTask task : onlineTasks.values()) {
			onlineTasksPool.execute(task);
		}

		// 5. Atualizando Variáveis de Estado
		connected = true;

		// 6. Atualizando Interface com o Usuário
		// ManagerScreen.userInterfaceEvent("Connected");
	}

	@SuppressWarnings("rawtypes")
	public void disconnect(String port, boolean intentional) throws ManagerException {
		// 1. Atualizando variáveis de estado
		stopping = true;
		connected = false;

		// 1. Registrando no MAS
		try {
			Broker.deregisterOnServer(port);
		} catch (BrokerException e) {
			throw new ManagerException("Unable to deregister on MAS", e);
		}

		// 2. Avisando Finalização se Necessário
		if (!intentional)
			LOG.info("Disconnected from server - Appears to be offline or out of synchronization");
		else
			LOG.warn("Disconnected from server");

		// 3. Atualizando Interface com o Usuário
		// ManagerScreen.userInterfaceEvent("Disconnected");
		
		//mata e removendo agentes instanciados
		killAgents(port);
		removeAgents(port);

		//parando servidor
		try
		{
			serversList.get(port).stop();
			serversList.remove(port);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		// 4. Terminando tarefas online
		for (FutureTask task : onlineTasks.values())
		{
			if ( (!task.isCancelled()) && (!task.isDone()) )
				task.cancel(true);
		}

		Catalog.removeContainer(port);
	}	
	
	public void reset(String port) throws ManagerException
	{
		disconnect(port, true);
		connect(port);		
	}
		
	@SuppressWarnings("rawtypes")
	public void removeAgents(String port) throws ManagerException
	{
		// 1. Inicializa Ciclo de Vida dos Agentes
		Set<OrganizationInfo> orgs = Catalog.getContainerInfos(port).getOrganizations();
		
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
						Agent agent = (Agent)(Factory.getInstance().instantiateEntity(orgPackage+"."+entPackage+"."+entClassName, port));
						lifeCycleAgents.remove(agent.getClass().toString().substring(6)+port);
						lifeCycleTasks.remove(agent.getClass().toString().substring(6)+port);								
						
						LOG.info("Removing "+agent.getClass().getSimpleName()+"on port "+port);
					}
					catch (Exception e)
					{	
						throw new ManagerException("Unable to remove agents life cycle, could not instantiate "+entClassName,e);											
					}
				}
			}			
		}
	}
		
	@SuppressWarnings("rawtypes")
	public void wakeAgents(String port) throws ManagerException
	{
		// 1. Inicializa Ciclo de Vida dos Agentes
		Set<OrganizationInfo> orgs = Catalog.getContainerInfos(port).getOrganizations();
		
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
						Agent agent = (Agent)(Factory.getInstance().instantiateEntity(orgPackage+"."+entPackage+"."+entClassName, port));
						lifeCycleAgents.put(agent.getClass().toString().substring(6)+port,agent);
						lifeCycleTasks.put(agent.getClass().toString().substring(6)+port,new FutureTask<Object>(agent));										
						
						LOG.info("Waking "+agent.getClass().getSimpleName()+"on port "+port);
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
	
	@SuppressWarnings("rawtypes")
	public void killAgents(String port) throws ManagerException
	{
		for (Entry<String,Agent> agent : lifeCycleAgents.entrySet()) {
			if(agent.getKey().contains(port))
			{
				agent.getValue().alive = false;
			}
		}
		
		try 
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e) 
		{		
			e.printStackTrace();
		}

		for (Entry<String,FutureTask<Object>> task : lifeCycleTasks.entrySet()) {
			if(task.getKey().contains(port))
			{
				if (!task.getValue().isDone())
				{
					task.getValue().cancel(true);
				}
			}
		}
		
		for (Entry<String,FutureTask<Object>> task : lifeCycleTasks.entrySet()) {
			if(task.getKey().contains(port))
			{
				if (!(task.getValue().isDone()||task.getValue().isCancelled()))
				{
					// TODO: Melhorar espefica��o do erro
					throw new ManagerException("Unable to terminate all the agents, please re-start the container, and check the lyfe-cycle implementations.");
				}
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

