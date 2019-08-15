/*
 * Created on 22/04/2005
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.midas.as.broker;

import java.util.List;
import java.util.Map;


public class Broker
{		
	private static Broker broker;
	
	private Broker()
	{
		
	}
	
	public static Broker getInstance()
	{
		if (broker == null)
		{
			broker = new Broker();
		}
		
		return broker;		
	}
	
	public boolean pingServer()
	{		
		return (Sender.pingServer()); 	
	}
			
	public static void registerOnServer() throws BrokerException
	{				
		Sender.registerOnServer();
	}

	public static boolean checkServerForService(String organization, String service) throws BrokerException
	{
		return(Sender.checkServerForService(organization,service));
	}

	public void require(String organization, String service, Map in, List out) throws BrokerException
	{
		Sender.requireServer(organization,service,in,out);		
	}
}