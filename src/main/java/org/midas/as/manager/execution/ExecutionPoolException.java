package org.midas.as.manager.execution;

import org.midas.as.manager.ManagerException;

/**
 * This exception is thrown by the ExecutionPool class, when there 
 * are problems regardind the operation of the ExecutionPool.
 */
public class ExecutionPoolException extends ManagerException
{
	public ExecutionPoolException(){super();}
	public ExecutionPoolException(String arg0, Throwable arg1){super(arg0, arg1);}
	public ExecutionPoolException(String message){super(message);}
	public ExecutionPoolException(Throwable arg0){super(arg0);}
}
