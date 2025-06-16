package n7.mcdalang.util.file;

import java.io.*;

public final class FileUtils {

    private FileUtils() {}

    public static String readFile(File file) throws IOException {
        StringBuilder content = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        }
    }

    public static void writeFile(File filePath, String content) throws IOException {
        try(FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }
}
