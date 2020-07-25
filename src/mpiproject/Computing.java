
package mpiproject;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author ayseg
 */
public class Computing {
    private String fileName;
    private int nGrams;   
    private int nofThreads;  

    public Computing() {}

    public Computing(String fileName, int nGrams, int nofThreads) {
        this.fileName = fileName;
        this.nGrams = nGrams;
        this.nofThreads = nofThreads;
    }

    private void Merge(ConcurrentMap<String, Integer> nGrams, ConcurrentMap<String, Integer> finalGrams) {
        for (ConcurrentMap.Entry<String, Integer> entry : nGrams.entrySet()) {
            int newValue = entry.getValue();
            Integer existingValue = finalGrams.get(entry.getKey());
            if (existingValue != null) {
                newValue += existingValue;
            }
            finalGrams.put(entry.getKey(), newValue);
        }
    }

    public void run() throws InterruptedException, ExecutionException {
        List<Character> file = FilePath.readTextFromFile(this.fileName);

        long start, end;
        
        if(file != null) {
            start = System.currentTimeMillis();

            ConcurrentMap<String, Integer> finalGrams = parallelWorks(file, this.nGrams, this.nofThreads);

            end = System.currentTimeMillis();         

            System.out.println("Total time to compute the 2-grams " + (end - start) + " ms");
        }
    }

    public ConcurrentMap<String, Integer> parallelWorks(List<Character> file, int nGrams, int nofThreads) throws InterruptedException, ExecutionException {
        ConcurrentMap<String, Integer> finalGrams = new ConcurrentHashMap<>();
        List<Future<ConcurrentHashMap<String, Integer>>> futuresList;

        ExecutorService executor = Executors.newFixedThreadPool(nofThreads);
        CompletionService<ConcurrentHashMap<String, Integer>> completionService = new ExecutorCompletionService<>(executor);

        double fileLen = file.size();
        double k = Math.floor(fileLen / nofThreads);
        futuresList = IntStream.range(0, nofThreads)
                .mapToObj(i -> completionService.submit(instantiateThreads(file, nGrams, k, i)))
                .collect(Collectors.toList());

        for (Future<ConcurrentHashMap<String, Integer>> future : futuresList) {
            ConcurrentHashMap<String, Integer> grams = future.get();
            Merge(grams, finalGrams);
        }

        awaitTerminationAfterShutdown(executor);
        return finalGrams;
    }

    private ThreadComputing instantiateThreads(List<Character> file, int nGrams, double k, int i) {
        return new ThreadComputing(i * k, ((i + 1) * k) + (nGrams - 1) - 1, nGrams, file);
    }

    private void awaitTerminationAfterShutdown(ExecutorService threadPool) throws InterruptedException {
        threadPool.shutdown();
        if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
            threadPool.shutdownNow();
        }
    }
}
