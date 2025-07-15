import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class Editor {
        enum Step {
            FOLDER_SELECT,   
            BROWSE,             
            FILE_ACTION,         
            READ_FILE,    
            MODIFY_FILE,  
            RUN_FILE,     
            DELETE_FILE,  
            RENAME_ITEM,         
            CREATE_ITEM,        
            FOLDER_ACTION,       
            DELETE_FOLDER  
        }


    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        Step step = Step.FOLDER_SELECT;
        File folder;
        Path filePath;
        String folderPath = "";
        String selectedFile = "";

        
        while(true){

            System.out.println("-----------------------------------");

            switch (step){

                case FOLDER_SELECT:
                System.out.println("Welcome to Omar new Text Editor");
                System.out.println("Just follow the instruction we give you");
                System.out.println("If you want to go back in yours steps just write `back` and to exit the program write `exit`");
                System.out.println("");
                System.out.println("Enter the folder path ");
                String input = scanner.nextLine().trim();
                folder = new File(input);

                if(input.equals("exit")) System.exit(0);

                if(folder.isDirectory()){
                    folderPath = input;
                    step = Step.BROWSE;
                }
                else{
                    System.out.println("The folder path isn't valide. Please try again");
                }
                break;

                case BROWSE:
                folder = new File(folderPath);
                File[] files = folder.listFiles();

                if (files == null) {
                    System.out.println("Cannot access files in this folder.");
                    step = Step.FOLDER_SELECT;
                    continue;
                }
                    
                System.out.println("Here's the files in the folder :");
                for(File file : files){
                    if (file.isFile()) {
                        System.out.println("[File] " + file.getName());
                    } else if (file.isDirectory()) {
                        System.out.println("[Folder] " + file.getName());
                    }
                }

                System.out.println("Write the file you wish to perform an action or write `create` to create a file or folder");
                input = scanner.nextLine().trim();

                if(input.equals("exit")) System.exit(0);

                if(input.equals("back")){
                    step = Step.FOLDER_SELECT;
                }
                else if(input.equals("create")){
                    step = Step.CREATE_ITEM;
                }
                else{
                    File selected = new File(folderPath, input);

                    if (!selected.exists()) {
                        System.out.println("Invalid file or folder. Try again.");
                    } else if (selected.isFile()) {
                        selectedFile = input;
                        step = Step.FILE_ACTION;
                    } else if (selected.isDirectory()) {
                        selectedFile = input;
                        step = Step.FOLDER_ACTION;
                    }
                }
                break;

                case FILE_ACTION:
                System.out.println("What action do wish to do :");
                System.out.println("Write `read` to read the file");
                System.out.println("Write `modify` to change the file");
                System.out.println("Write `rename` to rename the file");
                System.out.println("Write `run` to compile and run the java file");
                System.out.println("Write `delete` to delete the file");
                System.out.println("Write `back` to chose a different file");

                input = scanner.nextLine().trim();

                if(input.equals("exit")) System.exit(0);

                if(input.equals("read")){
                    step = Step.READ_FILE;
                }
                else if(input.equals("modify")){
                    step = Step.MODIFY_FILE;
                }
                else if(input.equals("run")){
                    step = Step.RUN_FILE;
                }
                else if(input.equals("delete")){
                    step = Step.DELETE_FILE;
                }
                else if (input.equals("back")){
                    step = Step.BROWSE;
                }
                else if(input.equals("rename")){
                    step = Step.RENAME_ITEM;
                }
                break;

                case READ_FILE:
                filePath = Paths.get(folderPath, selectedFile);
                try {
                    String str = Files.readString(filePath);
                    System.out.println("This is what the file contains :");
                    System.out.println(str);
                    step = Step.FILE_ACTION;
                } catch(IOException e){
                    System.out.println("This file is not compatible with this action");
                    step = Step.FILE_ACTION;
                }
                break;

                case MODIFY_FILE:
                filePath = Paths.get(folderPath, selectedFile);

                try {
                    String str = Files.readString(filePath);
                    System.out.println("Here's what the file contain :");
                    System.out.println(str);
                } catch(IOException e){
                    System.out.println("This file is not compatible with this action");
                    step = Step.FILE_ACTION;
                    break;
                }

                System.out.println("Do you want ot overide it or append to previous text in the file");
                System.out.println("Write `append` to append or `override` to override");
                String mode = scanner.nextLine().trim();

                if(mode.equals("exit")) System.exit(0);

                System.out.println("Write what you want to add to the file (write `END` in a line to end modification)");
                input = scanner.nextLine().trim();
                StringBuilder content = new StringBuilder();
                String line;

                if(input.equals("exit")) System.exit(0);

                try{
                    while (!(line = scanner.nextLine()).equals("END")) {
                        content.append(line).append(System.lineSeparator());
                    }

                    if(mode.equals("append"))Files.write(filePath, content.toString().getBytes(), StandardOpenOption.APPEND);
                    else Files.write(filePath, content.toString().getBytes());

                    System.out.println("The modification as been done");
                }catch(IOException e){
                    System.out.println("This cannot be writin into the file. Please try again");
                }
                
                step = Step.FILE_ACTION;
                break;

                case RUN_FILE:
                if (!selectedFile.endsWith(".java")) {
                    System.out.println("File is not a .java file.");
                    step = Step.FILE_ACTION;
                    continue;
                }
                try {
                    Process compile = Runtime.getRuntime().exec("javac " + folderPath + File.separator + selectedFile);
                    compile.waitFor();
                    if (compile.exitValue() != 0) {
                        System.out.println("Compilation failed.");
                    } else {
                        String className = selectedFile.substring(0, selectedFile.length() - 5);
                        Process run = Runtime.getRuntime().exec("java -cp " + folderPath + " " + className);
                        java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(run.getInputStream()));
                        String linee;
                        System.out.println("--- Program Output ---");
                        while ((linee = reader.readLine()) != null) {
                            System.out.println(linee);
                        }
                        System.out.println("----------------------");
                    }
                } catch (Exception e) {
                    System.out.println("Error running Java file.");
                }
                step = Step.FILE_ACTION;
                break;

                case DELETE_FILE:
                System.out.println("Are you sure you want to delete '" + selectedFile + "'? (yes/no)");
                input = scanner.nextLine().trim().toLowerCase();

                if(input.equals("exit")) System.exit(0);

                if (input.equals("yes")) {
                    File file = new File(folderPath, selectedFile);
                    if (file.delete()) {
                        System.out.println("File deleted.");
                        step = Step.BROWSE; 
                    } else {
                        System.out.println("Failed to delete file.");
                        step = Step.FILE_ACTION;
                    }
                } else if (input.equals("no")) {
                    step = Step.FILE_ACTION;
                } else {
                    System.out.println("Please answer 'yes' or 'no'.");
                }
                break;

                case RENAME_ITEM:
                System.out.println("Write the new name:");
                input = scanner.nextLine().trim();

                if(input.equals("exit")) System.exit(0);

                Path oldPath = Paths.get(folderPath, selectedFile);
                Path newPath = Paths.get(folderPath, input);

                File oldFile = new File(oldPath.toString());
                File newFile = new File(newPath.toString());

                if(input.equals("back")){
                    step = selectedFile.contains(".") ? Step.FILE_ACTION : Step.FOLDER_ACTION;
                    continue;
                }

                if (newFile.exists()) {
                    System.out.println("A file or a folder with that name already exists.");
                } else {
                    boolean renamed = oldFile.renameTo(newFile);
                    if (renamed) {
                        System.out.println("Renamed successfully.");
                        selectedFile = input;
                        step = selectedFile.contains(".") ? Step.FILE_ACTION : Step.FOLDER_ACTION;
                    } else {
                        System.out.println("Failed to rename it.");
                    }
                }

                break;

                case CREATE_ITEM:
                System.out.println("do u want to create a file or a folder");
                System.out.println("Write `file` to create a file");
                System.out.println("Write `folder` to create a folder");
                String choice = scanner.nextLine().trim();

                if(choice.equals("exit")) System.exit(0);
               
                if(choice.equals("file")){
                    System.out.println("Enter the name of the new file (include .txt or .java):");
                    input = scanner.nextLine().trim();

                    if(input.equals("exit")) System.exit(0);

                    filePath = Paths.get(folderPath, input);
                    File file = new File(filePath.toString());

                    if (file.exists()) {
                        System.out.println("A file with that name already exists.");
                    } else {
                        try {
                            boolean created = file.createNewFile();
                            if (created) {
                                System.out.println("File created successfully.");
                                step = Step.BROWSE;
                            } else {
                                System.out.println("File could not be created.");
                            }
                        } catch (IOException e) {
                            System.out.println("An error occurred while creating the file.");
                        }
                    }
               }

                else if(choice.equals("folder")){
                    System.out.println("Enter the name of the new folder:");
                    input = scanner.nextLine().trim();

                    if(input.equals("exit")) System.exit(0);

                    Path path = Paths.get(folderPath, input); 
                    folder = new File(path.toString());

                    if (folder.exists()) {
                        System.out.println("A folder with that name already exists.");
                    } else {
                        boolean created = folder.mkdir();
                        if (created) {
                            System.out.println("Folder created successfully.");
                            step = Step.BROWSE;
                        } else {
                            System.out.println("Failed to create folder.");
                        }
                    }
                }

                else{
                System.out.println("Please write file or folder");
                }

                break;

                case FOLDER_ACTION:
                System.out.println("Write `open` to enter this folder");
                System.out.println("Write `rename` to rename the folder");
                System.out.println("Write `delete` to delete the folder");
                System.out.println("Write `back` to return");

                input = scanner.nextLine().trim();

                if(input.equals("exit")) System.exit(0);

                if(input.equals("open")){
                    folderPath = folderPath + File.separator + selectedFile;
                    step = Step.BROWSE;
                }
                else if(input.equals("rename")){
                    step = Step.RENAME_ITEM;
                }
                else if(input.equals("delete")){
                    step = Step.DELETE_FOLDER;
                }
                else if(input.equals("back")){
                    step = Step.BROWSE;
                }
                break;

                case DELETE_FOLDER:
                System.out.println("Are you sure you want to delete the folder '" + selectedFile + "' and all its contents? (yes/no)");
                input = scanner.nextLine().trim().toLowerCase();

                if(input.equals("exit")) System.exit(0);

                if (input.equals("yes")) {
                    Path directory = Paths.get(folderPath, selectedFile);
                    try {
                        Files.walk(directory)
                            .sorted(java.util.Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                    
                        System.out.println("Folder deleted successfully.");
                        step = Step.BROWSE;
                    } catch (IOException e) {
                        System.out.println("An error occurred while deleting the folder.");
                        step = Step.FOLDER_ACTION; 
                    }
                } else if (input.equals("no")) {
                    step = Step.FOLDER_ACTION;
                } else {
                    System.out.println("Please answer 'yes' or 'no'.");
                }
                break;

            }         
        }
    }
}

