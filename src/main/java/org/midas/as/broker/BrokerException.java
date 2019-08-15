package org.midas.as.broker;

import org.midas.as.AgentServerException;

/**
 * Esta exceção é disparada caso hajam problemas de comunicação com
 * o servidor. As causas podem ser problemas na rede ou requisições
 * mal construídas.
 */
public class BrokerException extends AgentServerException 
{
	public BrokerException(){}
	public BrokerException(Throwable arg0){super(arg0);}
	public BrokerException(String message){super(message);}
	public BrokerException(String arg0, Throwable arg1){super(arg0, arg1);}
}

