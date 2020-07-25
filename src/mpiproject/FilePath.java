
package mpiproject;

/**
 *
 * @author ayseg
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilePath {
    static long start1, end1;
       
    public static List<Character> readTextFromFile(String fileName) {
              
        final String filePath =  "C:\\Users\\ayseg\\OneDrive\\Belgeler\\NetBeansProjects\\MPIProject\\" + fileName;
              
        start1 = System.currentTimeMillis();
        try (Stream<String> fileStream = Files.lines(Paths.get(filePath))) {
        end1 = System.currentTimeMillis();    
        System.out.println("File loading time is "+(end1-start1)+"ms");
            return fileStream.flatMap(FilePath::mapToChars)
                    .collect(Collectors.toList());                 
        } catch (IOException e) {
            e.printStackTrace();
            return null;              
        }          
    }
    private static Stream<Character> mapToChars(String s) {
       
        return s.toLowerCase()
                .replaceAll("[^a-zA-Z-öüğşİç]", "")               
                .chars()
                .mapToObj(c -> (char) c);       
    }

    public static void printSet(String key, Integer value) throws FileNotFoundException, IOException {
        System.out.println("ngram: " + key + ", value: " + value);
              
    }
}
