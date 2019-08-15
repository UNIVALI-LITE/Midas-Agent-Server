/**
 * 
 */
package org.midas.as.agent.board;

import org.midas.as.AgentServerException;

/**
 * This exception is thrown by the Board class, and refers to 
 * exceptions and erros that may happen on the dealing with the
 * Board messages and attributes. 
 */
public class BoardException extends AgentServerException
{
	public BoardException(){super();}
	public BoardException(Throwable arg0){super(arg0);}
	public BoardException(String message){super(message);}
	public BoardException(String arg0, Throwable arg1){super(arg0, arg1);}
}
