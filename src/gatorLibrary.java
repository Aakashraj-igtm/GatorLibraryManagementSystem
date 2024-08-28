import java.io.*;

public class gatorLibrary {
    // Input Parameters: inputFileName
    // Writes Output to a file in the format inputFileName_output_file.txt
    private static void writeToFile(String inputFileName){
        inputFileName = inputFileName.substring(0, inputFileName.indexOf("."));
        try {
            File myObj = new File(inputFileName+"_"+"output_file.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter myWriter = new FileWriter(inputFileName+"_"+"output_file.txt");
            myWriter.write(String.valueOf(RedBlackTree.sb));
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    // Input Parameters: redBlackTree Instance, each line from inputFile, inputFileName
    // parse eachLine from inputFileName and performs respective operations to be formed on RBT
    private static void parseLine(RedBlackTree redBlackTree, String line, String inputFileName) {
        line = line.replaceAll("\"", "");
        if (line.contains("Quit")) {
            redBlackTree.quit();
            writeToFile(inputFileName);
            System.exit(0);
        }
        int idx1 = line.indexOf("(");
        int idx2 = line.indexOf(")");
        String[] ar = line.substring(idx1 + 1, idx2).split(",");
        //System.out.println(Arrays.toString(ar));
        String operation = line.substring(0, idx1);
        if (operation.equals("InsertBook")) {
            redBlackTree.insertBook(Integer.parseInt(ar[0].trim()), ar[1].trim(), ar[2].trim(), ar[3].trim(), -1);
        } else if (operation.equals("PrintBook")) {
            int bookId = Integer.parseInt(ar[0].trim());
            Book book = redBlackTree.printBook(bookId);
            if (book != null) {
                RedBlackTree.sb.append(book +"\n");
            }
            else {
                RedBlackTree.sb.append("Book " + bookId + " not found in the library" +"\n");
            }
        } else if (operation.equals("PrintBooks")) {
            redBlackTree.printBooks(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
        } else if (operation.equals("BorrowBook")) {
            redBlackTree.borrowBook(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()), Integer.parseInt(ar[2].trim()));
        } else if (operation.equals("ReturnBook")) {
            redBlackTree.returnBook(Integer.parseInt(ar[0].trim()), Integer.parseInt(ar[1].trim()));
        } else if (operation.equals("DeleteBook")) {
            redBlackTree.deleteBook(Integer.parseInt(ar[0].trim()));
        } else if (operation.equals("FindClosestBook")) {
            redBlackTree.findClosestBook(Integer.parseInt(ar[0].trim()));
        } else if (operation.equals("ColorFlipCount")) {
            redBlackTree.colorFlipCount();
        }
    }

    // Entry Point of the program, which reads inputFileName from command line arguments
    public static void main(String[] args) {
        try {
            String inputFile = args[0];
            File file = new File(inputFile);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String line;
            RedBlackTree redBlackTree = new RedBlackTree();
            while ((line = br.readLine()) != null) {
                parseLine(redBlackTree, line, args[0]);
            }
            writeToFile(inputFile);
            fr.close();    //closes the stream and release the resources
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


