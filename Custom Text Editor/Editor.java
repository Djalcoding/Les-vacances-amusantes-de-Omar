import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Editor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int step = 1;  
        String folderPath = "";
        String selectedFile = "";
        
        while(true){

            System.out.println("-----------------------------------");
            
            if(step == 1){
                System.out.println("Enter the folder path ");
                String input = scanner.nextLine().trim();
                File folder = new File(input);

                if(folder.isDirectory()){
                    folderPath = input;
                    step = 2;
                }
                else{
                    System.out.println("The folder path isn't valide. Please try again");
                }
            }

            else if(step == 2){
                File folder = new File(folderPath);
                File[] files = folder.listFiles();
                if (files == null) {
                    System.out.println("Cannot access files in this folder.");
                    step = 1;
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
                String input = scanner.nextLine().trim();

                if(input.equals("back")){
                    step = 1;
                }
                else if(input.equals("create")){
                    step = 9;
                }
                else{
                    File selected = new File(folderPath, input);

                    if (!selected.exists()) {
                        System.out.println("Invalid file or folder. Try again.");
                    } else if (selected.isFile()) {
                        selectedFile = input;
                        step = 3;
                    } else if (selected.isDirectory()) {
                        selectedFile = input;
                        step = 10;
                    }
                }
            }

            else if(step == 3){
                System.out.println("What action do wish to do :");
                System.out.println("Write `read` to read the file");
                System.out.println("Write `modify` to change the file");
                System.out.println("Write `rename` to rename the file");
                System.out.println("Write `run` to compile and run the java file");
                System.out.println("Write `delete` to delete the file");
                System.out.println("Write `back` to chose a different file");

                String input = scanner.nextLine().trim();

                if(input.equals("read")){
                    step = 4;
                }
                else if(input.equals("modify")){
                    step = 5;
                }
                else if(input.equals("run")){
                    step = 6;
                }
                else if(input.equals("delete")){
                    step = 7;
                }
                else if (input.equals("back")){
                    step = 2;
                }
                else if(input.equals("rename")){
                    step = 8;
                }
            }

            else if(step == 4){
                Path filePath = Paths.get(folderPath, selectedFile);
                try {
                    String str = Files.readString(filePath);
                    System.out.println("This is what the file contains :");
                    System.out.println(str);
                    step = 3;
                } catch(IOException e){
                    System.out.println("This file is not compatible with this action");
                    step = 3;
                }
            }

            else if(step == 5){
                Path filePath = Paths.get(folderPath, selectedFile);

                try {
                    String str = Files.readString(filePath);
                    System.out.println("Here's what the file contain :");
                    System.out.println(str);
                } catch(IOException e){
                    System.out.println("This file is not compatible with this action");
                    step = 3;
                }

                System.out.println("Write what you want to add to the file (it will overwrite the previous content)");
                String input = scanner.nextLine().trim();

                try{
                    Files.write(filePath, input.getBytes());
                    System.out.println("The modification as been done");
                }catch(IOException e){
                    System.out.println("This cannot be writin into the file. Please try again");
                }
                
                step = 3;
            }

            else if(step == 6){
                if (!selectedFile.endsWith(".java")) {
                    System.out.println("File is not a .java file.");
                    step = 3;
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
                        String line;
                        System.out.println("--- Program Output ---");
                        while ((line = reader.readLine()) != null) {
                            System.out.println(line);
                        }
                        System.out.println("----------------------");
                    }
                } catch (Exception e) {
                    System.out.println("Error running Java file.");
                }
                step = 3;
            }

            else if(step == 7){
                System.out.println("Are you sure you want to delete '" + selectedFile + "'? (yes/no)");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("yes")) {
                    File file = new File(folderPath, selectedFile);
                    if (file.delete()) {
                        System.out.println("File deleted.");
                        step = 1; 
                    } else {
                        System.out.println("Failed to delete file.");
                        step = 3;
                    }
                } else if (input.equals("no")) {
                    step = 3;
                } else {
                    System.out.println("Please answer 'yes' or 'no'.");
                }
            }

            else if(step == 8){
                System.out.println("Write the new name:");
                String input = scanner.nextLine().trim();

                Path oldPath = Paths.get(folderPath, selectedFile);
                Path newPath = Paths.get(folderPath, input);

                File oldFile = new File(oldPath.toString());
                File newFile = new File(newPath.toString());

                if(input.equals("back")){
                    step = 3;
                    continue;
                }

                if (newFile.exists()) {
                    System.out.println("A file or a folder with that name already exists.");
                } else {
                    boolean renamed = oldFile.renameTo(newFile);
                    if (renamed) {
                        System.out.println("Renamed successfully.");
                        selectedFile = input;
                        step = 3;
                    } else {
                        System.out.println("Failed to rename it.");
                    }
                }
            }

            else if(step == 9){
               System.out.println("do u want to create a file or a folder");
               System.out.println("Write `file` to create a file");
               System.out.println("Write `folder` to create a folder");
               String choice = scanner.nextLine().trim();
               
               if(choice.equals("file")){
                    System.out.println("Enter the name of the new file (include .txt or .java):");
                    String input = scanner.nextLine().trim();

                    Path filePath = Paths.get(folderPath, input);
                    File file = new File(filePath.toString());

                    if (file.exists()) {
                        System.out.println("A file with that name already exists.");
                    } else {
                        try {
                            boolean created = file.createNewFile();
                            if (created) {
                                System.out.println("File created successfully.");
                                step = 2;
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
                    String input = scanner.nextLine().trim();

                    Path path = Paths.get(folderPath, input); 
                    File folder = new File(path.toString());

                    if (folder.exists()) {
                        System.out.println("A folder with that name already exists.");
                    } else {
                        boolean created = folder.mkdir();
                        if (created) {
                            System.out.println("Folder created successfully.");
                            step = 2;
                        } else {
                            System.out.println("Failed to create folder.");
                        }
                    }
               }

               else{
                System.out.println("Plz write file or folder and anything else");
               }

            }

            else if(step == 10){
                System.out.println("Write `open` to enter this folder");
                System.out.println("Write `rename` to rename the folder");
                System.out.println("Write `delete` to delete the folder");
                System.out.println("Write `back` to return");

                String input = scanner.nextLine().trim();

                if(input.equals("open")){
                    folderPath = folderPath + File.separator + selectedFile;
                    step = 2;
                }
                else if(input.equals("rename")){
                    step = 8;
                }
                else if(input.equals("delete")){
                    step = 11;
                }
                else if(input.equals("back")){
                    step = 2;
                }
            }

            else if (step == 11) {
                System.out.println("Are you sure you want to delete the folder '" + selectedFile + "' and all its contents? (yes/no)");
                String input = scanner.nextLine().trim().toLowerCase();

                if (input.equals("yes")) {
                    Path directory = Paths.get(folderPath, selectedFile);
                    try {
                        Files.walk(directory)
                            .sorted(java.util.Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                    
                        System.out.println("Folder deleted successfully.");
                        step = 2;
                    } catch (IOException e) {
                        System.out.println("An error occurred while deleting the folder.");
                        step = 10; 
                    }
                } else if (input.equals("no")) {
                    step = 10;
                } else {
                    System.out.println("Please answer 'yes' or 'no'.");
                }
            }           
        }
    }
}

