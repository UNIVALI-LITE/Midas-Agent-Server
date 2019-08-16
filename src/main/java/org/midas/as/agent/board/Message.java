package org.midas.as.agent.board;

/**
 *	This class represents a Message. A message it´s written by an {@link Agent}
 *	on the {@link Board}, adressed to a determined group. The Board notifies 
 *	other Agents which have declared their interest on the target group.
 *  A message has several items:
 *	<ul>
 *	<li> The date it was written </li>
 *	<li> It´s priority level </li>
 *	<li> The group it was destinated </li>
 *	<li> The Agent that wrote it </li>
 *	<li> A textual content </li>
 *	</ul>
 *	<p>
 *	The class offers several methods for recovering and setting such
 *	information. 
 */
public class Message
{
	private long date;
	private int priority;
	private String group;
	private String agent;
	private String content;

	public Message(int PriorityType, String group, long date, String entity, String data)
	{
	    this.priority = PriorityType;
	    this.group = group;
	    this.date = date;
	    this.agent = entity;
	    this.content = data;
	}

    public Message()
    {
    	priority = 0;
		group = new String();
		date = 0;
		agent = new String();
		content = new String();
    }

    public int getPriorityType()
    {
        return this.priority;
    }

    public String getGroup()
    {
        return this.group;
    }

    public long getDate()
    {
        return this.date;
    }

    public String getAgent()
    {
        return this.agent;
    }

    public String getData()
    {
        return this.content;
    }

   	public void setPriorityType(int PriorityType)
   	{
        this.priority = PriorityType;
    }

   	public void setPriorityType(String PriorityType)
   	{
        this.priority = (Integer.parseInt(PriorityType));
    }

    public void setGroup(String group)
    {
        this.group = group;
    }

    public void setDate(long date)
    {
        this.date = date;
    }

    public void setAgent(String entity)
    {
        this.agent = entity;
    }

    public void setData(String data)
    {
        this.content = data;
    }

    /*
    public Message[] getMessageByEntity(Message[] msgs, String agentkey) 
    {
		//comparar com o campo agent
        return msgs;
	}

	public Message[] getMessageByDate(Message[] msgs, long datekey) 
	{
		//usar .equals()
		//comparar com o campo date
        return msgs;
	}

	public Message[] getMessageByGroup(Message[] msgs, String groupkey) 
	{
		//comparar com o campo group
        return msgs;
	}

	public Message[] getMessageByData(Message[] msgs, String datakey) 
	{
		//Usar int String.equals(Object obj)
		//Para verificar se a chave corresponde ao campo "String data"
        return msgs;
	}

	public Message[] getMessageByDataPiece(Message[] msgs, String piecekey) 
	{
		//Usar int String.lastIndexOf(String str) ou boolean regionMatches(int toffset, String other, int ooffset, int len)
		//Para verificar se a chave corresponde a um pedaçao do campo "String data"
        return msgs;
	}

	public Message[] getMessageByPriority(Message[] msgs, int priority) 
	{
		// comparar com o campo PriorityType
        return msgs;
	}
	*/
    
	@Override
	public String toString()
	{
		return ("Sender: "+agent+" | Content: "+content);
	}	
}
