package org.midas.as.manager.manager.listeners;

import org.midas.as.broker.Broker;
import org.midas.as.broker.BrokerException;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;
import org.midas.as.manager.ManagerException;
import org.midas.as.manager.execution.Logger;
import org.midas.as.manager.manager.Manager;
import org.midas.as.manager.manager.ManagerScreen;

public class MainListener
{		
	public void connect()
	{
		try 
		{
			Manager.getInstance().connect();
		}
		catch (ManagerException e) 
		{
			Logger.addEntry("Unable to connect on MAS server, check if it�s online or if any firewall settings are blocking the communication");
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		Manager.getInstance().disconnect(true);
	}
	
	public void reset()
	{
		try 
		{
			Manager.getInstance().reset();
		}
		catch (ManagerException e) 
		{
			Logger.addEntry("Unable to properly reset, disconnecting to prevent errors");
			disconnect();
		}
	}
	
	public void refreshCatalog()
	{
		try 
		{
			Catalog.loadCatalog();
		}
		catch (CatalogException e) 
		{
			Logger.addEntry("Unable to Refresh Catalog");		
		}
		
		Logger.addEntry("Catalog Successfully Refreshed.");
		
		if (Manager.getInstance().isConnected())
		{
			try
			{
				Broker.registerOnServer();
			}
			catch (BrokerException e)
			{
				Logger.addEntry("Unable to propagate catalog refresh to MAS Server, disconnecting to keep consistence.");
				Manager.getInstance().disconnect(false);
			}
			
			Logger.addEntry("Catalog Sucessfully Updated on Mas Server.");
		}
		
		// Atualizando Interface		
		ManagerScreen.userInterfaceEvent("Refresh Services");		
	}
	
	public void exit()
	{
		Manager.getInstance().exit();
	}
	
	public void wakeAgents()
	{
		try
		{
			Manager.getInstance().wakeAgents();
		}
		catch(ManagerException e)
		{
			Logger.addEntry(e.getMessage());
			return;
		}
		
		// Atualizando Interface
		Logger.addEntry("Agents successfully awaken");
		ManagerScreen.userInterfaceEvent("Agents Woken");	
	}
	
	public void killAgents()
	{
		try 
		{
			Manager.getInstance().killAgents();
		}
		catch (ManagerException e) 
		{
			Logger.addEntry(e.getMessage());
			return;
		}
		
		// Atualizando Interface
		Logger.addEntry("Agents successfully killed.");
		ManagerScreen.userInterfaceEvent("Agents Killed");
	}
}
