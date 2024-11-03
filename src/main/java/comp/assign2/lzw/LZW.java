package comp.assign2.lzw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Mahmoud
 */

public class LZW {

    private static ArrayList<String> dic = new ArrayList<>();
    private static ArrayList<Integer> tags = new ArrayList<>();
    
        private static void fillDic() {
                for(int i = 65; i < 123; i++) {

                    if (i > 90 && i <= 96) {
                        continue;
                    }

                    String letter = "";
                    letter += ((char)i);
                    dic.add(letter);
            }
    
            // for(int i = 0; i < 52; i++) {
            //     System.out.println(dic.get(i));
            // }
        }

        public static String readString() {
            String inputtxt = "";
            try {
                File input = new File("src/input.txt");
                Scanner inputScanner = new Scanner(input);

                inputtxt = inputScanner.nextLine();
                inputScanner.close();

                return inputtxt;
            } catch (FileNotFoundException e) {
                System.out.println("Error: File not found");
                return "";
            }
        }

        public static void WriteString(String outputtxt, String path) {
            try {
                File output = new File("src/" + path);
                output.createNewFile();

                FileWriter outputWriter = new FileWriter(output);
                outputWriter.write(outputtxt);
                outputWriter.close();

            } catch (IOException e) {
                System.out.println("Error: Failed to write output");
            }
        }

        public static void compress(String txt) {

            int i = 0;
            while (i < txt.length()-1) {
                int j = 0;
                int lastmatchLocationInDic = 0;
                int matchlength;
                String searchtxtPart = "";
                String lastmatchedString = "";

                searchtxtPart += txt.charAt(i+j);

                for (int k = 0; k < dic.size(); k++) {
                    if (searchtxtPart.equals(dic.get(k))) {
                        lastmatchedString = searchtxtPart;

                        if(i + j + 1 > txt.length()-1) {
                            lastmatchLocationInDic = k;
                            break;
                        }

                        searchtxtPart += txt.charAt(i + ++j);
                        lastmatchLocationInDic = k;
                        
                    }
                }

                matchlength = lastmatchedString.length();
                if ( i+j+1 < txt.length() ) {
                    dic.add(lastmatchedString + txt.charAt(i+j));
                    
                }
                tags.add(lastmatchLocationInDic);

                i += matchlength;
            }
        }

        public static String dicompress() {
            String dicompressedtxt = "";

            dicompressedtxt += dic.get(tags.get(0));
            dic.add(dic.get(tags.get(0)));
            for (int i = 1; i < tags.size(); i++) {
                // Append the first char of the current tag to the end of the last element in the dictionary
                int lastIndex = dic.size() - 1;
                dic.set(lastIndex, dic.get(lastIndex) + dic.get(tags.get(i)).charAt(0));

                // Update the processed data with the current tag's value in the dictionary
                dicompressedtxt += dic.get(tags.get(i));

                // Add the current tag's value (from dictionary) at the end of the dictionary
                // note: it will be updated at the beginning of the next iteration
                dic.add(dic.get(tags.get(i)));

            }

            return dicompressedtxt;
        }


        public static void main(String[] args) {
            fillDic();
            String txtTOCompress = readString();

            compress(txtTOCompress);

            String PrintString = "";
            for (Integer tag : tags) {
                PrintString += tag.toString() + "\n";
            }
            WriteString(PrintString, "tagsOutput.txt");

            PrintString = "";
            for (String dic : dic) {
                PrintString += dic + "\n";
            }
            WriteString(PrintString, "dicOutput.txt");

            dic.clear();
            fillDic();

            String dicompressedtxt = dicompress();
            WriteString(dicompressedtxt, "DicompressedOutput.txt");
    }
}
