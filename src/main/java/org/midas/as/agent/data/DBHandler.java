/*
 * DBPool.class
 * 
 * Data de Cria��o: 17/04/2005  
 */
package org.midas.as.agent.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The DBHandler (DatabaseHandler) class encapsulates a connection object
 * within a data source. It acts by mediating the access to the database,
 * by providing methods that abstract the proccess of quering and updating
 * data. 
 */
public class DBHandler 
{
	// Variável que guarda a conexão com o BD	
	private Connection connection;
	
	/**
	 * Recebe um objeto de conexão como parâmetro e o encapsula.
	 * 
	 * @param connection - objeto de conexão com um BD.
	 */ 
	protected DBHandler(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * Creates a new Statement for the data source.
	 * 
	 * @return Statement object
	 * 
	 * @throws SQLException  in case the Statement creation fails 
	 */
	public Statement createStatement() throws SQLException
	{
		return (connection.createStatement());
	}
	
	/**
	 * Creates a new PreparedStatement for the data source.
	 * 
	 * @param query  Query to be prepared within the statement
	 * 
	 * @return PreparedStatement object
	 * 
	 * @throws SQLException  in case the PreparedStatement creation fails 
	 */
	public PreparedStatement createPreparedStatement(String query) throws SQLException
	{	
		return connection.prepareStatement(query);
	}
	
	/**
	 * Process an update query.
	 * 
	 * @param query  Query to be processed 
	 */
	public void runSQL (String query) throws SQLException
	{
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(query);		
	}
	
	/**
	 * Process a select query and returns the ResultSet back.
	 * 
	 * @param query  Query to be processed
	 * 
	 * @return ResultSet object with query result 
	 */
	public ResultSet getSQL (String query) throws SQLException
	{
		Statement stmt = connection.createStatement();
		
		return (stmt.executeQuery(query));
	}
	
	
	/**
	 * Destroy the handler, closing the encapsulated connection and
	 * freeing system resources. 
	 */
	public void close() throws SQLException
	{
		// Fecha a conexão
		connection.close();
		
		// Finaliza o objeto
		try
		{
			this.finalize();
		}
		catch (Throwable e){}
	}
}
