import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Comparator;

public class Main {
    public static void main(String[] args)  {
        File folder =  new File(args[0]);
        File[] files = folder.listFiles();
        String option = args[1];

        switch (option) {
            case "-s":
                Arrays.sort(files, Comparator.comparingLong(File::length));
                for(File file: files){
                    System.out.println(file.getName() + ": "+ file.length());
                }
                break;
            case "-t":
                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                for(File file: files){
                    System.out.println(file.getName() + ": "+ file.lastModified());
                }
                break;
            default:
                Arrays.sort(files);
                System.err.println(files);
        }


    }   
}
