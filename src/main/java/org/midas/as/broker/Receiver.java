package org.midas.as.broker;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.midas.as.manager.execution.ServiceWrapper;
import org.midas.as.manager.execution.ServiceWrapperException;
import org.midas.as.manager.manager.Manager;

public class Receiver extends HttpServlet
{
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		System.out.println("DoGet");
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		System.out.println("DoPost");
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException,IOException
	{
		System.out.println("DoService");
		
		// Recuperando Tipo da Requisi��o
		String requisitionType = req.getParameter("type");
		
		// SE o par�metro n�o existir
		if (requisitionType == null)
		{
			// Lan�ando Exce��o para Requisi��o Inv�lida
			throw new ServletException("Invalid Requisition - Expected parameter 'type' with requisition type {provide,ping}");
		}
		
		// Processando Requisi��o
		if (requisitionType.equals("provide"))
		{
			provideRequest(req,res);
		}
		else if (requisitionType.equals("ping"))
		{
			pingRequest(req,res);			
		}
		else
		{
			// Lan�ando Exce��o para Requisi��o Inv�lida
			throw new ServletException("Invalid Requisition - Expected parameter 'type' with requisition type {provide,ping}");
		}		
	}
	
	public void pingRequest(ServletRequest req, ServletResponse res) throws IOException
	{		
		ObjectOutputStream out = new ObjectOutputStream(res.getOutputStream());			
		out.writeUTF("pong...");
		out.close();
	}
	
	public void provideRequest (HttpServletRequest req, HttpServletResponse res) throws IOException 		
	{
		Map     in;
		List    out;
		String  service;
		String  organization;
		
		// Recuperando Entrada da Requisi��o
		try
		{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(req.getInputStream()));
			
			organization = ois.readUTF();
			service		 = ois.readUTF();
			in			 = (Map)ois.readObject();
			
			ois.close();
		}		
		catch (ClassNotFoundException e)
		{
			// Processando Resposta
			res.setStatus(HttpServletResponse.SC_OK);
			
			// Recuperando resposta do container alvo 
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(res.getOutputStream()));
			
			oos.writeUTF("error");
			oos.writeObject(new BrokerException("Container: Could not process provide request, unexpected error while recovering Map object with the requisition parameters").getMessage());
			
			oos.flush();
			oos.close();
			
			return;
		}
		
		// Executando Servi�o
		try
		{
			ServiceWrapper wrapper = Manager.getInstance().obtainService(organization,service,null);
			wrapper.setParameters(in);
			out = wrapper.run();				
			
			// Processando Resposta
			res.setStatus(HttpServletResponse.SC_OK);
			
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(res.getOutputStream()));					
							
			oos.writeUTF("success");
			oos.writeObject(out);
			
			oos.flush();
			oos.close();
		}
		catch (ServiceWrapperException e)
		{
			// Processando Resposta
			res.setStatus(HttpServletResponse.SC_OK);
			
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(res.getOutputStream()));
							
			oos.writeUTF("error");
			oos.writeObject(e.getMessage());
			
			oos.flush();
			oos.close();			
		}
		catch (InterruptedException e)
		{
			// Processando Resposta
			res.setStatus(HttpServletResponse.SC_OK);
			
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(res.getOutputStream()));					
							
			oos.writeUTF("error");
			oos.writeObject(e.getMessage());
			
			oos.flush();
			oos.close();			
		}
		catch (ExecutionException e)
		{
			// Processando Resposta
			res.setStatus(HttpServletResponse.SC_OK);
			
			ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(res.getOutputStream()));
							
			oos.writeUTF("error");
			oos.writeObject(e.getMessage());
			
			oos.flush();
			oos.close();			
		}		
	}
}
