package org.midas.as.proxy;

import org.midas.as.AgentServerException;

public class ProxyException extends AgentServerException 
{
	public ProxyException(){}
	public ProxyException(Throwable arg0){super(arg0);}
	public ProxyException(String message){super(message);}
	public ProxyException(String arg0, Throwable arg1){super(arg0, arg1);}
}
