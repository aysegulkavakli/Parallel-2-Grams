
package mpiproject;
/**
 *
 * @author ayseg
 */
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadComputing implements Callable<ConcurrentHashMap<String, Integer>>{

    private int n;
    private double start, stop;
    private ConcurrentHashMap<String, Integer> grams;
    private List<Character> file;
    private StringBuilder builder;

    ThreadComputing(double start, double stop, int n, List<Character> file) { 
        this.start = start;
        this.stop = stop;
        this.n = n;
        this.file = file;
        this.grams = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<String, Integer> call() throws IOException {
       
        if (stop > file.size() - 1) {
            stop = file.size() - 1;
        }
        for (double i = this.start + n - 1; i <= this.stop; i++) {
            builder = new StringBuilder();

            for (double j = n - 1; j >= 0; j--) {
                builder.append(this.file.get((int) (i - j)));
            }
            if (this.n == 2) {

                String key = builder.toString();

                if (!this.grams.containsKey(key)) {
                    this.grams.put(builder.toString(), 1);
                } else {
                    if (this.grams.containsKey(key)) {
                        this.grams.put(builder.toString(), this.grams.get(key) + 1);
                    }
                }
            } else {
                System.out.println("invalid n");
            }
        }
        PrintWriter out = new PrintWriter(new FileWriter("out_20150807013.txt"));
        out.println(grams);
        return grams;
    }    
}
