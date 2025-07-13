import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;

public class tool {
    public static void main(String[] args)  {
        if (args.length == 1 && (args[0].equals("-h"))){
            System.out.println("Options:");
            System.out.println("  -s          Sort files by size (in MB)");
            System.out.println("  -t          Sort files by last modified date");
            System.out.println("  -h          Show this help message");
            System.out.println("If no option is provided, files will be listed alphabetically.");
            return;
        }

        File folder;
        File[] files;
        String option = "";

        if (args.length == 0) {
            System.err.println("No folder specified.");
            return;
        }

        folder = new File(args[0]);
        if (!folder.exists() || !folder.isDirectory()) {
            System.err.println("Error: The folder '" + args[0] + "' does not exist or is not a directory.");
            return;
        }

        files = folder.listFiles();
        if (files == null) {
            System.err.println("Error: Unable to read folder contents.");
            return;
        }

        if (args.length >= 2) {
            option = args[1];
        }
        if (args.length > 2) {
            System.err.println("Too many arguments");
            return;
        }

        switch (option) {
            case "-s":
                Arrays.sort(files, Comparator.comparingLong(File::length));
                for(File file: files){
                    double sizeMB = file.length() / (1024.0 * 1024.0);
                    System.out.println(file.getName() + ": " + sizeMB + " MB");
                }
                break;
            case "-t":
                Arrays.sort(files, Comparator.comparingLong(File::lastModified));
                for(File file: files){
                    Date lastModifiedDate = new Date(file.lastModified());
                    SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
                    System.out.println(file.getName() + ": "+ sdf.format(lastModifiedDate));
                }
                break;
             case "-h":
             System.out.println("");
                break;
            default:
                Arrays.sort(files);
                for (File file : files) {
                    System.out.println(file.getName());
                }
        }


    }   
}
