/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * class that compress the file into bitstrings
 */
public class Compress {
    private static final int NUM_CHARS = 256; // alphabet size of extended ASCII

    private HCTree.HCNode root; // the root of HCTree

    private static final int EXP_ARG = 2; // number of expected arguments

    /**
     * method that compresses the given file and put the bitStrings into a new file
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        // Check if the number of arguments is correct
        if (args.length != EXP_ARG) {
            System.out.println("Invalid number of arguments.\n" +
                                "Usage: ./compress <infile outfile>.\n");
            return;
        }

        // read all the bytes from the given file and make it to a byte array
        byte[] input = Files.readAllBytes(Paths.get(args[0]));
        int[] freq = new int[NUM_CHARS];
        for (int i = 0; i < input.length; i++) {
            int ascii = input[i] & 0xff;
            freq[ascii]++;
        }
        // create a new tree and put the freq into the tree
        HCTree hctree = new HCTree();
        hctree.buildTree(freq);
        FileOutputStream file = new FileOutputStream(args[1]);
        DataOutputStream out  = new DataOutputStream(file);
        BitOutputStream bitOut = new BitOutputStream(out);
        // stores the number of bytes in the file we want to compress
        out.writeInt(input.length);
        hctree.encodeHCTree(hctree.getRoot(), bitOut);
        for (int i = 0; i < input.length; i++) {
            hctree.encode(input[i], bitOut);
        }
        
        // There might be several padding bits in the bitOut that we haven't written, so flush it first.
        bitOut.flush();
        out.close();
        file.close();
    }
}
