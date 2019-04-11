/*
 * DBPool.class
 * 
 * Data de Criação: 17/04/2005  
 */
package org.midas.as.agent.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.midas.as.catalog.Catalog;
import org.midas.as.catalog.CatalogException;


/**
 * The DBPool (Databse Pool) class, is responsible for the declared data sources
 * within the architecture. It provides methods for an {@link Agent} or a {@link Componente} 
 * to recover a {@link DBHandler} (Database Handler) object for a specific data source.
 * It also provides a bridge for Hibernate Mapped sources, by reading pointed Hibernate 
 * configuration files, and abstracting the hard code proccess of factoring Hibernate sessions.
 */
public class DBPool 
{	
	private static HashMap connections = new HashMap(3);
	private static SessionFactory factory;
	
	/**
	 * Returns an Hibernate session for the specified dataSource
	 * 
	 * @param dataSource  Data source name
	 * 
	 * @return Session object for the data source
	 * 
	 * @throws HibernateException  in case the configuration file is inexistant or mal-formed
	 */
	public static Session getHibernateSession (String dataSource) throws HibernateException
	{			
		return getFactory().openSession();		
	}
	
	private static SessionFactory getFactory () throws HibernateException
	{
		if (factory == null)
			factory = new Configuration().configure().buildSessionFactory();
		return factory;		
	}
	
	/**
	 * Returns a Database Handler for the specifiedd ado um determinado source definido 
	 * no catálogo de estrutura.
	 * 
	 * @param dataSource  Data source name
	 * 
	 * @return DBHandler - DBHandler that abstracts the connection within desired data source
	 *  
	 * @throws DatabaseException  in case there are connection or driver problems within the data source
	 * @throws CatalogException  in case there are erros within the Catalog meta-data regarding the data source
	 */	
	public static DBHandler getDatabaseHandler(String dataSource) throws DatabaseException, CatalogException
	{
		// SE a fonte de dados já foi carregada
		if (connections.containsKey(dataSource))
		{
			// Recupera a String de Conexão do HashMap
			String connString = (String)connections.get(dataSource);
			
			// Obtém uma nova conexão
			Connection conn = null;
			
			try
			{
				conn = DriverManager.getConnection(connString);
			}
			catch (SQLException e)
			{
				throw new DatabaseException("Error while connecting to DataSource driver",e);
			}
			
			// Retorna o manipulador
			return (new DBHandler(conn));
		}
		// SENÃO
		else
		{						
			// Recupera os valores do Catálogo
			String driver     = Catalog.getDatabaseDriver(dataSource);
			String connString = Catalog.getDatabaseConnectionString(dataSource);
				
			// Carrega o driver indicado
			try
			{
				Class.forName(driver);
			}
			catch (ClassNotFoundException e)
			{
				throw new DatabaseException("Unable to find datasource - "+dataSource+" - driver, "+driver+" must be on classpath",e);
			}
			
			// Insere a string de conexão no HashMap
			connections.put(dataSource,connString);

			// Obtém uma nova conexão			
			Connection conn = null;
			
			try
			{
				conn = DriverManager.getConnection(connString);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			// Instancia um manipulador sobre esta conexão
			DBHandler newHandler = new DBHandler(conn);
						
			// Retorna o manipulador
			return newHandler;			
		}
	}	
}
