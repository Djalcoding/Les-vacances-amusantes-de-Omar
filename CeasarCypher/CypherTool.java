package CeasarCypher;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CypherTool {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.err.println("Not enough arguments");
            return;
        }

        if(args.length > 2) {
            System.err.println("Too many arguments");
            return;
        }   

        String filename = args[0];
        int k = 1;

        if (args.length == 2) {
            try {
                k = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Argument 2 is not a number");
                return;
            }
        }

        Path filePath = Path.of(filename);
        if (!Files.exists(filePath)) {
            System.err.println(filename + " doesn't exist");
            return;
        }

        k %= 26;  
        String ans = "";
        String str = Files.readString(filePath);

        for (int i = 0; i < str.length(); i++) {
            if (k == 0) {
                ans = str;
                break;
            }

            char c = str.charAt(i);

            if (c != ' ') {
                if (c >= 'a' && c <= 'z') {
                    c = (char) ((c - 'a' + k) % 26 + 'a');
                } else if (c >= 'A' && c <= 'Z') {
                    c = (char) ((c - 'A' + k) % 26 + 'A');
                }
            }

            ans += c;
        }

        System.out.println(ans);
    }
}
