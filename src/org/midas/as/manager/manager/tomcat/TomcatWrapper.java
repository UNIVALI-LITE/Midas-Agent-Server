package org.midas.as.manager.manager.tomcat;

public class TomcatWrapper 
{
	TomcatStartup tomcat;
	
	public void start(String path,int port) throws Exception
	{
		tomcat = new TomcatStartup();
		tomcat.setPath(path);
		tomcat.startTomcat(port);			
	}
	
	public void stop() throws Exception
	{		
		tomcat.stopTomcat();			
	}
}
