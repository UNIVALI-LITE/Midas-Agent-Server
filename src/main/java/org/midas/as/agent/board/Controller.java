package org.midas.as.agent.board;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class offers operational support to the {@link Board}, by 
 * providing the hard code operations that invoke the {@link MessageListener} 
 * and {@link ContextListener} interface methods implemented on the registered
 * Agents.
 */
public class Controller 
{
    //private static short msgcount = 0;
    //private static String strfile = new String();
    //private static String targetdir = new String();
    //private static char limits[] = {(char)2, (char)3};
    //private static String STX = new String(limits, 0,1);
    //private static String ETX = new String(limits, 1,1);
    
    /*private static short plusmsgcount()
    {
    	if (msgcount > 5000) 
    	{
    		msgcount = 0;
    	}
    	
    	msgcount++;
    	return msgcount;
    }*/

    /*
     * Cleans the Board message log.
     * 
     * @param msgsFolder  The folder where the messages are persisted
     *
	public void cleanLog(String msgsFolder) 
	{
		try
		{
            File path = new File(msgsFolder);

            if ((path.exists()) && (path.isDirectory()))
            {
            	File[] files = path.listFiles();

            	for(int i = 0; i < files.length; i++)
            	{
            		FileInputStream readfile = new FileInputStream(files[i]);
            	  
            		if ((files[i].isFile()) && (files[i].getName().endsWith(".msg")))
            		{
            			files[i].delete();
            		}
            	}
           	}
		}		
		catch(IOException e)
		{
            System.out.println("Fail to read msg file(s) !");
        }
    }*/

	/**
	 * Used by the Board to send a message for a group of listeners.
	 * 
	 * @param listeners  Array with the target listening agents
	 * @param msg  The message to be sent
	 */
	public void messageNotify(ArrayList<MessageListener> listeners, Message msg) 
	{
        // PARA todos os ouvintes
		for (MessageListener listener : listeners)
        {
			// Envia mensagem
			listener.boardChanged(msg);
        }
	}

	/**
	 * Used by the Board to notify a group of listeners that an global attribute
	 * has been created or updated.
	 * 
	 * @param listeners  Array with the target listening agents
	 * @param registerName  The name of the created/updated attribute
	 */
    public void contextNotify(List<ContextListener> listeners, String registerName) 
    {
    	for (ContextListener listener : listeners)
    	{
    		listener.registerChanged(registerName);
    	}
    }
    
	/*
	 * Reads a determined number of messages from the log, from the newest to 
	 * the oldest one.
	 * 
	 * @param msgsFolder  The folder where the messages are persisted
	 * @param maxFiles  The maximum number of returned messages
	 *
    public static Message[] readLog(String msgsFolder, int maxfiles)
    {
    	//Message auxmsg;
    	Message[] MessagesArrayType = new Message[1];
    	
    	/*
    	ArrayList pmsgs = new ArrayList(maxfiles);
    	String read = new String();
    
    	try
    	{
    		File path = new File(msgsFolder);
    	    		
    		if ((path.exists()) && (path.isDirectory()))
    		{
    			String[] files = path.list();
    		    			
    			QuickSort(0 ,((files.length)-1), files);
    			
    			for(int i = 0; ((i < maxfiles)&&(maxfiles <= (files.length))); i++)
    			{
    				FileInputStream readfile = new FileInputStream(msgsFolder+"/"+files[i]);
    				File auxf = new File(files[i]);
    			
    				if ((auxf.isFile()) && (auxf.getName().endsWith(".msg")))
    				{
    					int fret =0;
    					while (fret != -1)
    					{
    						fret = readfile.read();
    						if (fret != -1)
    							read += (char)fret;
    					}
    				}
    				else
    				{
    					maxfiles++;
    				}
    			}
    		}

    		else
    		{
    			if (!(path.mkdirs()))
    				System.out.println("Fail to create new folder(s) !");
    		}
    		//test to print data file reads
    		//System.out.println(read);
    	}
    	catch(IOException e)
    	{
    		System.out.println("Fail to read msg file(s) !");
    	}
    	

    	StringTokenizer st = new StringTokenizer(read, STX, false);
    	
    	//loop para encher persistedMessages
    	while(st.hasMoreTokens())
    	{
    		auxmsg = new Message();
    		auxmsg.setPriorityType(st.nextToken());
    		auxmsg.setGroup(st.nextToken());
    		auxmsg.setDate(Long.parseLong(st.nextToken()));
    		auxmsg.setAgent(st.nextToken());
    		auxmsg.setData(st.nextToken());
    		pmsgs.add(auxmsg);
    	}

    	pmsgs.trimToSize();
    	
    	//!!! Talvez não funcione esse tipo de casting !!!
    	MessagesArrayType = new Message[pmsgs.size()];    	
    	pmsgs.toArray(MessagesArrayType);
    	*
    	return(MessagesArrayType);
	}
	
	/**
	 * Returns the current date on the yyyyMMddHHmmss format.
	 * 
	 * @return String  Current date
	 */
	public static String getDate()
	{
		Date _date;
		SimpleDateFormat sDate = new SimpleDateFormat();
		sDate.applyPattern("yyyyMMddHHmmss");
		_date = new Date(System.currentTimeMillis());
		return (sDate.format(_date));
	}/*
	
	/**
	 * Used by the Board to persist a message on the Log
	 * 
	 * @param msg {@link Message} to be persisted
	 * 
	 * @return boolean  true if the message has been succefully persisted
	 *
	public boolean writeMessageLog(Message msg)
	{
		String filename = getDate() + Integer.toString(plusmsgcount());
		String msgdata = new String(STX + Integer.toString((msg.getPriorityType()))+ STX + msg.getGroup()+ STX + msg.getDate()+ STX + msg.getAgent()+ STX + msg.getData());
	
		try
		{
			File boardFolder = new File("board");
			
			if (!boardFolder.canRead())
			{
				boardFolder.mkdir();
			}
			
			String targetFile = "board/" + targetdir.trim() + filename + ".msg";
			FileWriter msgfile = new FileWriter(targetFile);
			PrintWriter out = new PrintWriter(msgfile, true);
			out.println(msgdata);
			out.close();
		}
		catch(IOException e)
		{
			System.out.println("Erro de IO !!!");
			return false;
		}
		return true;
	}
	
	private static void QuickSort(int p, int q, String[] array)
	{
		if (p < q)
		{
			int x = QuickParticao(p, q, array);
			QuickSort(p, x - 1, array);
			QuickSort(x + 1, q, array);
		}
	}
	
	private static int QuickParticao(int p, int q, String array[])
	{
		int j = p - 1;
		String aux = new String((array[q]));
	
		for (int i = p; i <= q; i++)
		{
			if ((array[i].compareTo(aux)) < 1)
				QuickTroca(array, i, ++j);
		}
		return j;
	}
	
	private static void QuickTroca(String array[], int i, int j)
	{
		String aux = new String((array[i]));
		array[i] = array[j];
		array[j] = aux;
	}      */
}
