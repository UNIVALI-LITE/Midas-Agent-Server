package org.midas.as;

import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.manager.Manager;
import org.midas.as.manager.manager.ManagerScreen;


public class AgentServer 
{
	public static void initialize() throws ManagerException
	{
		Manager.getInstance().initialize();
	}
	
	public static ServiceWrapper require(String organization,String service) throws ManagerException
	{
		return(Manager.getInstance().obtainService(organization,service,null));
	}
	
	public static void loadManager() throws ManagerException 
	{
		if (!Manager.getInstance().isInitialized())
		{
			initialize();
		}
		
		ManagerScreen.show();		
	}

	public static void connect() throws ManagerException
	{
		if (!Manager.getInstance().isInitialized())
		{
			initialize();
			Manager.getInstance().connect();
		}		
	}		
}
