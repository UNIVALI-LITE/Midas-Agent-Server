/**
 * 
 */
package org.midas.as.agent.templates;

import org.midas.as.agent.EntityException;

/**
 * This exception is thrown by the {@link Agent} whenever problems occurs within
 * its life cycle.
 */
public class LifeCycleException extends EntityException
{
	public LifeCycleException(){super();}
	public LifeCycleException(Throwable arg0){super(arg0);}
	public LifeCycleException(String message){super(message);}
	public LifeCycleException(String arg0, Throwable arg1){super(arg0, arg1);}
}
