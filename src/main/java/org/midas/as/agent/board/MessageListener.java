package org.midas.as.agent.board;

import org.midas.as.agent.board.Message;

/**
 * The interface MessageListener must be implemented by an {@link Agent}, 
 * if it wants to join a messaging group on the {@link Board}.
 */
public interface MessageListener 
{
	/**
	 * Method that is invoked by the Board to deliver a {@link Message}
	 * to an implementing Agent. 
	 * 
	 * @param Message  The message object.	
	 */
    public void boardChanged(Message msg);
}
