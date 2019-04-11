/**
 * Created on 14/11/2004
 * @author Aluizio Haendchen Filho
 */

package org.midas.as.manager.execution;
 
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The ExecutionPool holds the responsability of processing the {@link ServiceWrapper} objects. 
 * It uses a CachedThreadPool, and offers methods for the execution of services both in 
 * synchorouns and assynchronous mode. It also marks several QoS metrics, such as the number of
 * active requisitions and the memory consumed by the Container. 
 *  
 * @author Administrador
 *
 */
public class ExecutionPool 
{
	private static AtomicLong activeThreads = new AtomicLong(0);	
	public  static ExecutorService executor = Executors.newCachedThreadPool();
			
	/**
	 * Method invoked by a ServiceWrapper requisition when it must run in a synchronous mode.
	 * 
	 * @param service ServiceWrapper object, that in fact implements the Callable interface
	 * 
	 * @return A List with the result of the service processing
	 * 
	 * @throws InterruptedException  In case the pool interrupts the execution 
	 * @throws ExecutionException  In case any exception is thrown from the service
	 */
	public static List runService(Callable<List> service) throws InterruptedException, ExecutionException
	{		
		return (submitService(service).get());					
	}
	
	/**
	 * Method invoked by a ServiceWrapper requisition when it must run in an assynchronous mode.
	 * 
	 * @param service ServiceWrapper object, that in fact implements the Callable interface
	 * 
	 * @return  A Future object that allows further recover of the List response
	 * 
	 * @throws InterruptedException  In case the pool interrupts the execution 
	 * @throws ExecutionException  In case any exception is thrown from the service
	 */	
	public static Future<List> submitService(Callable<List> service)  
	{
		return executor.submit(service);
	}
	
	/**
	 * Method invoked by a ServiceWrapper requisition when it starts operating to increase 
	 * the total active thread count.
	 */
	public static void increaseThreadCount()
	{
		activeThreads.incrementAndGet();
	}

	/**
	 * Method invoked by a ServiceWrapper requisition when it starts operating to decrease 
	 * the total active thread count.
	 */
	public static void decreaseThreadCount()
	{
		activeThreads.decrementAndGet();
	}
	
	/**
	 * Method that provides the total memory consumed by the Agent Container.
	 * 
	 * @return The memory count 
	 */
	public static long memoryUsage()
	{
		return Runtime.getRuntime().totalMemory();
	}
	
	/**
	 * Method that provides the total active requisitons count in the Agent Container. 
	 * 
	 * @return The thread count
	 */	
	public static long getActiveThreads()
	{
		return activeThreads.get();
	}
}
