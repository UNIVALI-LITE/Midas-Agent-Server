/**
 * 
 */
package org.midas.as.agent.templates;

import org.midas.as.agent.EntityException;

/**
 * This exception is thrown by the {@link Agent} of the {@link Component} whenever problems 
 * occurs with the processing of their services.
 */
public class ServiceException extends EntityException
{
	public ServiceException(){super();}
	public ServiceException(Throwable arg0){super(arg0);}
	public ServiceException(String message){super(message);}
	public ServiceException(String arg0, Throwable arg1){super(arg0, arg1);}
}
