/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.*;
import java.util.Scanner;

import static org.junit.Assert.*;



public class HCTreeTester{
    HCTree test1;
    int[] array1;
    HCTree test2;
    int[] array2;
    HCTree test3;
    int[] array3;
    FileOutputStream outFile1;
    DataOutputStream out1;
    BitOutputStream bitOut1;
    FileInputStream inFile1;
    DataInputStream in1;
    BitInputStream bitIn1;

    byte symbol1;
    byte symbol2;
    byte symbol3;
    byte symbol4;

    @org.junit.Before
    public void setUp() throws Exception {
    test1 = new HCTree();
    array1 = new int[256];
    array1['a'] = 17;
    array1['b'] = 8;
    array1['c'] = 7;
    array1['d'] = 14;
    array1['e'] = 9;
    array1['f'] = 1;
    test2 = new HCTree();
    array2 = new int[256];
    array2['d'] = 10;
    array2['s'] = 10;
    array2['c'] = 10;
    array2['3'] = 10;
    array2['0'] = 10;
    test3 = new HCTree();
    array3 = new int[256];
    array3['a'] = 42;

//    //FileOutputStream outFile = new FileOutputStream(System.out);
//    DataOutputStream out = new DataOutputStream(System.out);
//
//    FileInputStream inFile = new FileInputStream("");
//    DataInputStream in = new DataInputStream(inFile);
//    BitInputStream bitIn = new BitInputStream(in);


    }

    @org.junit.Test
    public void buildTree() throws IOException {
        test1.buildTree(array1);
        test1.inorder(test1.getRoot());
        Scanner reader1 = new Scanner(new File("src/check1.txt"));
        while (reader1.hasNext()){
            String str1 = reader1.nextLine();
            System.out.println(str1);
        }
        test2.buildTree(array2);
        test2.inorder(test2.getRoot());
        Scanner reader2 = new Scanner(new File("src/check2.txt"));
        while (reader2.hasNext()){
            String str2 = reader2.nextLine();
            System.out.println(str2);
        }
        test3.buildTree(array3);
        test3.inorder(test3.getRoot());
        Scanner reader3 = new Scanner(new File("src/check3.txt"));
        while (reader3.hasNext()){
            String str3 = reader3.nextLine();
            System.out.println(str3);
        }

    }

    @org.junit.Test
    public void encode() throws IOException {
        outFile1 = new FileOutputStream("src/try.txt");
        out1 = new DataOutputStream(outFile1);
        bitOut1 = new BitOutputStream(out1);
        symbol1 = 100;
        symbol2 = 101;
        symbol3 = 98;
        symbol4 = 97;
        test1.buildTree(array1);
//        test1.inorder(test1.getRoot());
        out1.writeInt(4);
        test1.encode(symbol1, bitOut1);
        test1.encode(symbol2, bitOut1);
        test1.encode(symbol3, bitOut1);
        test1.encode(symbol4, bitOut1);
        outFile1.close();
        out1.close();

    }

    @org.junit.Test
    public void decode() throws IOException {
        inFile1 = new FileInputStream("src/try.txt");
        in1 = new DataInputStream(inFile1);
        bitIn1 = new BitInputStream(in1);
        int byteCount = in1.readInt();

        test1.buildTree(array1);
        System.out.println(test1.decode(bitIn1));
        System.out.println(test1.decode(bitIn1));
        System.out.println(test1.decode(bitIn1));
        System.out.println(test1.decode(bitIn1));
    }
}