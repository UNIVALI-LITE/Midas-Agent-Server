package org.midas.as.agent.data;

import org.midas.as.AgentServerException;

/**
 * This exception is thrown by the {@link DBPool} and {@link DBHandler} classes, 
 * and refers to exceptions and erros that may happen on the dealing with database
 * connectivity.
 */
public class DatabaseException extends AgentServerException
{
	public DatabaseException()
	{
		super();
	}

	public DatabaseException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}

	public DatabaseException(String message)
	{
		super(message);
	}

	public DatabaseException(Throwable arg0)
	{
		super(arg0);
	}
}
