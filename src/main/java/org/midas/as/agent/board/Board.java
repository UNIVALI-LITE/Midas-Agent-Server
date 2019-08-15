package org.midas.as.agent.board;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.midas.as.agent.templates.Agent;

/**
 * Board is the class that implements the BlackBoard pattern, acting as 
 * a mean for an {@link Agent} to assynchronally communicate with others 
 * or to set and recover global attributes of the application.
 * <p>
 * It provides methods for the agents to sign on different messaging groups, 
 * to write a {@link Message} to any group, or broadcasting it. It also has
 * methods that allows agents to read and record attributes.
 * <p>
 * In order to sign on a message group, an agent has to implement the 
 * {@link Message Listener} interface. It´s also possible for an agent to be warned whenever 
 * there´s a change within the global attributes, for that it has to implement
 * the {@link ContextListener} interface, and sign on the board. 
 */
public class Board
{	
	// Controlador
	private static Controller controller = new Controller();
	
	// Variáveis Relativas as Mensagens
	private static Map<String,Object> 		   registers           = new ConcurrentHashMap<String,Object>();
	private static List<ContextListener>       contextListeners    = new CopyOnWriteArrayList<ContextListener>();
	
	//private static List<Message> 					      messages = new ConcurrentArrayList<Message>(40);
	private static Map<String,ArrayList<MessageListener>> groups   = new ConcurrentHashMap<String,ArrayList<MessageListener>>();
		
	//private static Message 	auxmsg;
    //private static Message[]  auxmsgs;
    //private static Message[] initMsgs;
		
    /**
     * Method that initializes the Board, it´s invoked on 
     * MIDAS initialization routine.
     * 
     * @param msgsSize  An Integer with the size of the messages cache.
     */
	public static void initializeBoard()
    {    	
    	File f = new File("board.bkp");
    	
    	if (!f.exists())
    	{
    		try 
    		{
				f.createNewFile();
			} 
    		catch (IOException e) 
    		{
				e.printStackTrace();
			}
    	}
    	
    	if (f != null)
    	{   		
    		try 
    		{
    			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
				
				boolean done = false;
				
				while(!done)
				{
					try 
					{
						registers = (Map<String,Object>)ois.readObject();						
					} 
					catch (ClassNotFoundException e) 
					{
						e.printStackTrace();
					}					
				}
			} 
    		catch (FileNotFoundException e) 
    		{
				e.printStackTrace();
			}
    		catch (EOFException e) 
    		{
				
			}
    		catch (IOException e)
    		{
    			e.printStackTrace();
    		}
    	}    	
    	
    	/*
    	Board.initMsgs = new Message[msgsSize];
    	Board.initMsgs = Controller.readLog("board", msgsSize);
    	
    	messages = new ArrayList(msgsSize);
    	
    	for(int i = 0; i < initMsgs.length; i++)
    	{
    		messages.add(initMsgs[i]);
    	}
    	    	
    	for (Message message : messages)
    	{
    		System.out.println (messages);
    	}
    	*/
    }        
    
    /**
     * Signs an Agent (Implementing the MessageListener interface}), on the 
     * Board, for a determined group. If the group does not exists, it´s created.
     *
     * @param group  Group name
     * @param listener  Listener reference to the implementing Agent 
     */
    public static void addMessageListener(String group,MessageListener listener)
    {    	
    	ArrayList<MessageListener> listeners;
    	
    	// SE o grupo existe
    	if (groups.containsKey(group))
    	{
    		// Recupera lista de ouvintes
    		listeners = groups.get(group);
    	}
    	//SENÂO
    	else
    	{
    		// Cria nova lista de ouvintes
    		listeners = new ArrayList<MessageListener>(10);
    		
    		// Adiciona lista ao mapa de grupos
    		groups.put(group,listeners);
    	}
    	
    	// Adiciona ouvinte a lista
    	listeners.add(listener);
    }
    
    
    /**
     * Signs an Agent (Implementing the ContextListener interface), on the 
     * Board, on the special group that notifies changes on the global attributes of the 
     * application.
     *
     * @param listener  Listener reference to the implementing Agent 
     */
    public static void addContextListener(ContextListener listener)
    {
    	contextListeners.add(listener);
    }
    
    /**
     * Writes a message on the Board for the members of a group.
     * 
     * @param priority  The level of the message prority
     * @param group  The name of the target group
     * @param content  The content of the message
     * @param author  Reference to the writing agent
     * 
     * @throws BoardException  In case the target group doesn´t exists
     */
    public static synchronized void writeOnBoard(int priority, String group,String content,Agent author) 
    	throws BoardException
    {
    	// SE o grupo existe
    	if (groups.containsKey(group))
    	{
    		// Recupera lista de ouvintes
    		ArrayList<MessageListener> listeners = groups.get(group);
    		
    		// Cria objeto da mensagem com a prioridade, grupo, data, escritor e conteúdo.
    		Message newmsg = new Message(priority, group,(Long.parseLong(Controller.getDate())), 
    									((author.getClass()).getName()), content);
    		
    		// Utiliza método do controlador para notificar grupo interessado
    		controller.messageNotify(listeners, newmsg);
    		
    		// Coloca mensagem na lista de mensagens disponíveis
    		//messages.add(newmsg);
    		
    		// Utiliza metodo do controlador para persistir mensagem
    		//controller.writeMessageLog(newmsg);    			
    	}
    	// SENÃO
    	else
    	{
    		throw new BoardException("Unable to write message - invalid group ("+group+")");
    	}
    }
    
    /**
     * Writes a broadcasting message for all the local Agents.
     * 
     * @param content  The content of the message
     * @param author  Reference to the writing agent
     */
    public static void writeForAll(String content,Agent author) 
    {
    	// Constrói mensagem
    	Message newmsg = new Message(0, "All", (Long.parseLong(Controller.getDate())), (author.getClass()).getName(), content);
    	
    	// Coloca mensagem na lista de mensagens disponíveis
    	//messages.add(newmsg);
    	
    	/*Escreve no log 
    	if(!(controller.writeMessageLog(newmsg)))
    		System.out.println("Cannot write log !");*/
    	
    	// Recupera todos os ouvintes
    	Set<MessageListener> listeners = new HashSet<MessageListener>();
    	Collection<ArrayList<MessageListener>> listenersGroups = groups.values();
    	    	
    	for (ArrayList<MessageListener> listenerGroup : listenersGroups)
    	{    		
    		for( MessageListener listener : listenerGroup )
    		{
    			listeners.add(listener);
    		}
    	}
    	
    	// Notifica todos os ouvintes
    	controller.messageNotify(new ArrayList<MessageListener>(listeners),newmsg);
    }
    
    /**
     * Set an attribute. If the attribute already exists, it´s updated.
     * 
     * @param name  The attribute name
     * @param value  The attribute value
     */    
    public static synchronized void setContextAttribute(String name, Object value)
    {    	
    	// Adicionando atributo
    	registers.put(name, value);
    	
    	// Avisando ouvintes do contexto
    	controller.contextNotify(contextListeners,name);
    	        	
  		// Persistindo Atributos
		try 
		{
			File f = new File("board.bkp");
			
			f.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(registers);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }  
    
    /**
     * Recover an attribute for the giving name. 
     * 
     * @param name  The attribute name
     * 
     * @throws BoardException  In case the attribute doesn´t exists.
     */ 
    public static Object getContextAttribute(String name) throws BoardException
    {
    	// SE existir o register solicitado
    	if (registers.containsKey(name))
    	{
    		return (registers.get(name));
    	}
    	//SENÃO
    	else
    	{
    		throw new BoardException("Invalid attribute name '"+name+"'");
    	}	
    }   
    
    public static synchronized void removeContextAttribute(String name) throws BoardException
    {
    	// SE existir o register solicitado
    	if (registers.containsKey(name))
    	{
    		registers.remove( name );
    	}
    	//SENÃO
    	else
    	{
    		throw new BoardException("Invalid attribute name '"+name+"'");
    	}
    	
  		// Persistindo Atributos
		try 
		{
			File f = new File("board.bkp");
			
			f.createNewFile();
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(registers);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }
    
    /*
     * Allow messages to be recovered from the Board, using a Date range as 
     * a filter criteria. 
     * 
     * @param Date  Start date
     * @param Date  End date
     * 
     * @return Message[]  The recovered messages
     *
    public static Message[] readOnBoardByDate(Date begin,Date end) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2]; 
    }
    
    /*
     * Allow messages to be recovered from the Board, using a Group as 
     * a filter criteria. 
     * 
     * @param String  Group name
     * 
     * @return Message[]  The recovered messages
     *
    public static  Message[] readOnBoardByGroup(String group) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2]; 
    }
    
    /*
     * Allow messages to be recovered from the Board, using a priority level as 
     * a filter criteria. 
     * 
     * @param int  Priority level
     * 
     * @return Message[]  The recovered messages
     *   
    public static  Message[] readOnBoardByPriority(int priority) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2]; 
    }
    
    
    /*
     * Allow messages to be recovered from the Board, using a Agent as 
     * a filter criteria. 
     * 
     * @param String  Agent name
     * 
     * @return Message[]  The recovered messages
     *    
    public static  Message[] readOnBoardByAgent(String Agent) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2]; 
    }
    
    /*
     * Allow messages to be recovered from the Board, using the exact
     * content as a filter criteria. 
     * 
     * @param String  Content of the message
     * 
     * @return Message[]  The recovered messages
     *
    public static  Message[] readOnBoardByContent(String content) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2];         
    }
    
    /**
     * Allow messages to be recovered from the Board, using parts of the
     * content as a filter criteria. 
     * 
     * @param String  Part of the content
     * 
     * @return Message[]  The recovered messages
     *
    public static  Message[] readOnBoardByContentPart(String content) 
    {
    	/*auxmsgs = new Message[messages.size()];
    	messages.toArray(auxmsgs);
    	return (auxmsg.getMessageByGroup(auxmsgs, group));
    	
    	return new Message[2]; 
    }    
    
    /*
     * Reset the board by erasing the messages cache, optionally may
     * also erase the persisted message log.
     * 
     * @param boolean  Boolean that defines whether the persisted log should or not be erased.
     *
    public static void cleanBoard(boolean eraseLog)
    {
    	messages.clear();
    
    	if(eraseLog)
    		controller.cleanLog(path);
    }          */ 
}



