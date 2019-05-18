/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.*;

/**
 * Decompress the first given file to the second given file using Huffman coding
 */
public class Decompress {
    private static final int EXP_ARG = 2; // number of expected arguments

    /**
     * method that decompresses the file while put it in a new file
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Check if the number of arguments is correct
        if (args.length != EXP_ARG) {
            System.out.println("Invalid number of arguments.\n" +
                    "Usage: ./uncompress <infile outfile>.\n");
            return;
        }

        FileInputStream inFile = new FileInputStream(args[0]);
        DataInputStream in = new DataInputStream(inFile);
        BitInputStream bitIn = new BitInputStream(in);

        FileOutputStream outFile = new FileOutputStream(args[1]);
        DataOutputStream out = new DataOutputStream(outFile);
        // create a tree
        HCTree hctree = new HCTree();
        // record the number of bytes in the file that is compressed
        int bytesCompressed = in.readInt();
        hctree.setRoot(hctree.decodeHCTree(bitIn));

        //decode
        for (int i = 0; i < bytesCompressed; i++) {
            out.writeByte(hctree.decode(bitIn));
        }


        inFile.close();
        in.close();
        outFile.close();
        out.close();
        return;
    }
}
