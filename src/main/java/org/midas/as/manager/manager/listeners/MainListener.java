package org.midas.as.manager.manager.listeners;

import org.midas.as.broker.Broker;
import org.midas.as.broker.BrokerException;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.manager.Manager;
import org.midas.as.manager.manager.ManagerScreen;

public class MainListener
{	
	public final String port;

	public MainListener(String port) {
		this.port = port;
	}
	
	public void connect()
	{
		try 
		{
			Manager.getInstance().connect(port);
		}
		catch (ManagerException e) 
		{
//			Logger.addEntry("Unable to connect on MAS server, check if it�s online or if any firewall settings are blocking the communication");
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try {
			Manager.getInstance().disconnect(port, true);
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void reset()
	{
		try 
		{
			Manager.getInstance().reset(port);
		}
		catch (ManagerException e) 
		{
//			Logger.addEntry("Unable to properly reset, disconnecting to prevent errors");
			disconnect();
		}
	}
	
	public void refreshCatalog(String structureXML, String servicesXML)
	{
		try 
		{
			Catalog.loadCatalog(structureXML, servicesXML);
		}
		catch (CatalogException e) 
		{
//			Logger.addEntry("Unable to Refresh Catalog");		
		}
		
//		Logger.addEntry("Catalog Successfully Refreshed.");
		
		if (Manager.getInstance().isConnected())
		{
			try
			{
				Broker.registerOnServer(port);
			}
			catch (BrokerException e)
			{
//				Logger.addEntry("Unable to propagate catalog refresh to MAS Server, disconnecting to keep consistence.");
				try {
					Manager.getInstance().disconnect(port, false);
				} catch (ManagerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
//			Logger.addEntry("Catalog Sucessfully Updated on Mas Server.");
		}
		
		// Atualizando Interface		
		//ManagerScreen.userInterfaceEvent("Refresh Services");		
	}
	
	public void exit()
	{
		Manager.getInstance().exit();
	}
	
	public void wakeAgents()
	{
		try
		{
			Manager.getInstance().wakeAgents(port);
		}
		catch(ManagerException e)
		{
//			Logger.addEntry(e.getMessage());
			return;
		}
		
		// Atualizando Interface
//		Logger.addEntry("Agents successfully awaken");
		//ManagerScreen.userInterfaceEvent("Agents Woken");	
	}
	
	public void killAgents()
	{
		try 
		{
			Manager.getInstance().killAgents();
		}
		catch (ManagerException e) 
		{
//			Logger.addEntry(e.getMessage());
			return;
		}
		
		// Atualizando Interface
//		Logger.addEntry("Agents successfully killed.");
		//ManagerScreen.userInterfaceEvent("Agents Killed");
	}
}
