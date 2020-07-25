
package mpiproject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
/**
 *
 * @author ayseg
 */
public class MPIProject {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException{
        long startTime, endTime;
        startTime = System.currentTimeMillis();
        final String fileName = "news.txt";
        final int nGrams = 2;
        final int nofThreads = 10;
        
        Computing parallelComputing = new Computing(fileName, nGrams, nofThreads);
        parallelComputing.run();  
        
        endTime = System.currentTimeMillis();
        System.out.println("Total time to complete the job is " + (endTime-startTime)+ "ms");
    }    
        
}
    
    
    

