package org.midas.as;

import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.manager.Manager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentServer 
{
	private static Logger LOG = LoggerFactory.getLogger(AgentServer.class);
	
	public static void initialize(String structureXML, String servicesXML)
	{
		initialize(true,true, structureXML, servicesXML);
	}
	
	public static void initialize(boolean online,boolean wakeAgents, String structureXML, String servicesXML) 
	{
		LOG.info(" __  __  ___  ____     _     ____               _     ____ ");
		LOG.info("|  \\/  ||_ _||  _ \\   / \\   / ___|             / \\   / ___| ");
		LOG.info("| |\\/| | | | | | | | / _ \\  \\___ \\   _____    / _ \\  \\___ \\ ");
		LOG.info("| |  | | | | | |_| |/ ___ \\  ___) | |_____|  / ___ \\  ___) |");
		LOG.info("|_|  |_||___||____//_/   \\_\\|____/          /_/   \\_\\|____/ ");
		LOG.info("=============================================================");
		
		try
		{
			LOG.info("Initializing Agent Server");
			Manager.getInstance().initialize(structureXML, servicesXML);
			
			if (online)
			{
				LOG.info("Connecting to MAS Server");
				Manager.getInstance().connect();
			}
			
			if (wakeAgents)
			{
				LOG.info("Starting Agents");
				Manager.getInstance().wakeAgents();
			}
		}
		catch(ManagerException e)
		{
			LOG.error("Could not initialize AgentServer", e);
		}
	}
	
	public static ServiceWrapper require(String organization,String service) throws ManagerException
	{
		return(Manager.getInstance().obtainService(organization,service,null));
	}	
}
