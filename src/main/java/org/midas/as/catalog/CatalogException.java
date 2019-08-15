package org.midas.as.catalog;

import org.midas.as.AgentServerException;


public class CatalogException extends AgentServerException 
{	
	public CatalogException(){}
	public CatalogException(Throwable arg0){super(arg0);}
	public CatalogException(String message){super(message);}
	public CatalogException(String arg0, Throwable arg1){super(arg0, arg1);}
}
