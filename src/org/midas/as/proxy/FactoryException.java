package org.midas.as.proxy;


public class FactoryException extends ProxyException
{
	public FactoryException(){}
	public FactoryException(Throwable arg0){super(arg0);}
	public FactoryException(String message){super(message);}
	public FactoryException(String arg0, Throwable arg1){super(arg0, arg1);}	
}
