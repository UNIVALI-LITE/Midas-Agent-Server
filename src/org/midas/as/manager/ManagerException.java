package org.midas.as.manager;

import org.midas.as.AgentServerException;

public class ManagerException extends AgentServerException 
{
	public ManagerException(){}
	public ManagerException(Throwable arg0){super(arg0);}
	public ManagerException(String message){super(message);}
	public ManagerException(String arg0, Throwable arg1){super(arg0, arg1);}
}
