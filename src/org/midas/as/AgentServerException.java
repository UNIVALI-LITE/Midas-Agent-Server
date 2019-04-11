package org.midas.as;

import org.midas.MidasException;

public class AgentServerException extends MidasException 
{
	public AgentServerException(){}
	public AgentServerException(Throwable arg0){super(arg0);}
	public AgentServerException(String message){super(message);}
	public AgentServerException(String arg0, Throwable arg1){super(arg0, arg1);}
}
