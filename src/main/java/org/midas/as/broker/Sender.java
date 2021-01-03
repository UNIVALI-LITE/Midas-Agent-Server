package org.midas.as.broker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.midas.as.catalog.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sender
{	
	private static Logger LOG = LoggerFactory.getLogger(Receiver.class);
	
	public static boolean pingServer()
	{					
		try
		{			
			URL url = new URL("http://"+Catalog.getServerAddress()+":"+Catalog.getServerPort()+"/masserver/trader?type=ping");									
			HttpURLConnection uc  = (HttpURLConnection)url.openConnection();
				
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
										
			ObjectInputStream in = new ObjectInputStream(uc.getInputStream());					
			in.close();
						
			uc.disconnect();
			
			return true;
		}
		catch (MalformedURLException e) 
		{		
			e.printStackTrace();
			return false;
		}
		catch (IOException e) 
		{			
			e.printStackTrace();
			return false;
		} 	
	}
			
	public static void registerOnServer(String port) throws BrokerException
	{				
		try 
		{
			URL url = new URL("http://"+Catalog.getServerAddress()+":"+Catalog.getServerPort()+"/masserver/trader?type=register");
			HttpURLConnection uc  = (HttpURLConnection)url.openConnection();
				
			uc.setRequestProperty("Content-Type", "application/octet-stream");
			
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
									         
			ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(uc.getOutputStream()));		    
			out.writeObject(Catalog.getContainerInfos(port));	
			
			out.flush();
			out.close();
			
			ObjectInputStream in =	new ObjectInputStream(new BufferedInputStream(uc.getInputStream()));
				
			in.close();
			
			uc.disconnect();			
		} 
		catch (MalformedURLException e) 
		{		
			throw new BrokerException("Invalid Server URL",e);
		}
		catch (IOException e) 
		{			
			throw new BrokerException("Error on data transfer to server",e);
		} 
	}

	public static void requireServer(String organization, String service, Map in, List out, String requesterName) throws BrokerException
	{
		try 
		{
			LOG.info("Requiring "+organization+"."+service);
			
			URL url = new URL("http://"+Catalog.getServerAddress()+":"+Catalog.getServerPort()+"/masserver/trader?type=provide");
			HttpURLConnection uc  = (HttpURLConnection)url.openConnection();
				
			uc.setRequestProperty("Content-Type", "application/octet-stream");
			
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
									         
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(uc.getOutputStream()));
			oos.writeUTF(requesterName);
			oos.writeUTF(organization);
			oos.writeUTF(service);
			oos.writeObject(in);	
			
			oos.flush();
			oos.close();
			
			ObjectInputStream ois =	new ObjectInputStream(new BufferedInputStream(uc.getInputStream()));
			
			String message = ois.readUTF();
			
			if (message.equals("success"))
			{
				out.addAll((List)ois.readObject());
			}
			else if (message.equals("error"))
			{		
				throw new BrokerException((String)ois.readObject());
			}
			else
			{
				throw new BrokerException("Unable to proccess provide request: Invalid response from Trader " +
										  "or target Container");
			}
				
			ois.close();
			
			uc.disconnect();			
		} 
		catch (MalformedURLException e) 
		{		
			e.printStackTrace();
			throw new BrokerException("Invalid Server URL",e);
		}
		catch (IOException e) 
		{			
			e.printStackTrace();
			throw new BrokerException("Error on data transfer to server",e);
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new BrokerException("Error on data transfer from server, wrong return type",e); 			
		} 		
	}

	public static boolean checkServerForService(String organization, String service) throws BrokerException
	{
		boolean exists = false;
		
		try 
		{
			URL url = new URL("http://"+Catalog.getServerAddress()+":"+Catalog.getServerPort()+"/masserver/trader?type=verify");
			HttpURLConnection uc  = (HttpURLConnection)url.openConnection();
				
			uc.setRequestProperty("Content-Type", "application/octet-stream");
			
			uc.setDoOutput(true);
			uc.setDoInput(true);
			uc.setUseCaches(false);
									         
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(uc.getOutputStream()));		    
			oos.writeUTF(organization);
			oos.writeUTF(service);
			
			oos.flush();
			oos.close();
			
			ObjectInputStream ois =	new ObjectInputStream(new BufferedInputStream(uc.getInputStream()));
			
			exists = ois.readBoolean();	
			ois.close();
			
			uc.disconnect();	
			
			return exists;
		} 
		catch (MalformedURLException e) 
		{		
			throw new BrokerException("Invalid Server URL",e);
		}
		catch (IOException e) 
		{			
			throw new BrokerException("Error on data transfer to server",e);
		}				
	}
}
