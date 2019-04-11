package org.midas.as.manager.execution;

import org.midas.as.manager.ManagerException;

/**
 * This exception is thrown by the Manager class, when there are
 * problems regardind the obtainence of a ServiceWrapper. The most 
 * common problem happens when the desired service does not exist.
 */
public class ServiceWrapperException extends ManagerException
{
	public ServiceWrapperException(){super();}
	public ServiceWrapperException(String arg0, Throwable arg1){super(arg0, arg1);}
	public ServiceWrapperException(String message){super(message);}
	public ServiceWrapperException(Throwable arg0){super(arg0);}
}
