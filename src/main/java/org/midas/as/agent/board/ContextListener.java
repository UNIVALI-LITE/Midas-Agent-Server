package org.midas.as.agent.board;

/**
 * The interface ContextListener must be implemented by an {@link Agent}, 
 * if it wants to join the special messaging group on the {@link Board} that
 * notifies any changes within the application global parameters.
 */
public interface ContextListener 
{
	/**
	 * Method that is invoked by the Board to notify an implementing Agent
	 * about the change or creation of an attribute. 
	 * 
	 * @param registerName  The created or updated attribute.
	 */
    public void registerChanged(String registerName);
}
