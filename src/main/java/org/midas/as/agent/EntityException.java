package org.midas.as.agent;

import org.midas.as.AgentServerException;
import org.midas.as.agent.templates.Agent;

/**
 * This exception is a high level exceptin that represents the 
 * exceptions that may be thrown by an {@link Agent} or a 
 * {@link Component}.
 * 
 * @see LifeCycleException
 * @see ServiceException
 */
public class EntityException extends AgentServerException
{
	public EntityException(){}
	public EntityException(Throwable arg0){super(arg0);}
	public EntityException(String message){super(message);}
	public EntityException(String arg0, Throwable arg1){super(arg0, arg1);}
}
