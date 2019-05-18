/*
 * NAME: Zhaoyi Guo
 * PID: A15180402
 */
import java.io.*;
import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * The Huffman Coding Tree
 */
public class HCTree {

    private static final int NUM_CHARS = 256; // alphabet size of extended ASCII

    private HCNode root; // the root of HCTree
    private HCNode [] leaves = new HCNode[NUM_CHARS]; // the leaves of HCTree that contain all the symbols

    /**
     * The Huffman Coding Node
     */
    protected class HCNode implements Comparable<HCNode> {

        byte symbol; // the symbol contained in this HCNode
        int freq; // the frequency of this symbol
        HCNode c0, c1, parent; // c0 is the '0' child, c1 is the '1' child
        
        /**
         * Initialize a HCNode with given parameters
         * @param symbol the symbol contained in this HCNode
         * @param freq the frequency of this symbol
         */
        HCNode(byte symbol, int freq) {
            this.symbol = symbol;
            this.freq = freq;
        }

        /**
         * Getter for symbol
         * @return the symbol contained in this HCNode
         */
        byte getSymbol() {
            return this.symbol;
        }

        /**
         * Setter for symbol
         * @param symbol the given symbol
         */
        void setSymbol(byte symbol) {
            this.symbol = symbol;
        }

        /**
         * Getter for freq
         * @return the frequency of this symbol
         */
        int getFreq() {
            return this.freq;
        }

        /**
         * Setter for freq
         * @param freq the given frequency
         */
        void setFreq(int freq) {
            this.freq = freq;
        }

        /**
         * Getter for '0' child of this HCNode
         * @return '0' child of this HCNode
         */
        HCNode getC0() {
            return c0;
        }

        /**
         * Setter for '0' child of this HCNode
         * @param c0 the given '0' child HCNode
         */
        void setC0(HCNode c0) {
            this.c0 = c0;
        }

        /**
         * Getter for '1' child of this HCNode
         * @return '1' child of this HCNode
         */
        HCNode getC1() {
            return c1;
        }

        /**
         * Setter for '1' child of this HCNode
         * @param c1 the given '1' child HCNode
         */
        void setC1(HCNode c1) {
            this.c1 = c1;
        }

        /**
         * Getter for parent of this HCNode
         * @return parent of this HCNode
         */
        HCNode getParent() {
            return parent;
        }

        /**
         * Setter for parent of this HCNode
         * @param parent the given parent HCNode
         */
        void setParent(HCNode parent) {
            this.parent = parent;
        }

        /**
         * Check if the HCNode is leaf
         * @return if it's leaf, return 1. Otherwise, return 0.
         */
        boolean isLeaf() {
            return (c0 == null) && (c1 == null);
        }
        
        public String toString() {
            return "Symbol: " + this.symbol + "; Freq: " + this.freq;
        }

        /**
         * method that compares twoo nodes
         * @param o
         * @return 1 if the current node is greater than the other one, -1 otherwise
         */
        public int compareTo(HCNode o) {
            //  return positive number if “this” is considered
            //  “larger” than the given object.
            // false otherwise.
            int result = 0;
            if (getFreq() > o.getFreq())
                result = 1;
            else if (getFreq() < o.getFreq())
                result = -1;
            else {
                if (getSymbol() > o.getSymbol())
                    result = 1;
                else
                    result = -1;
            }
            return result;
        }
    }

    /**
     * getter for the root
     * @return root
     */
    public HCNode getRoot() {
        return root;
    }

    /**
     * setter for the root
      * @param root
     */
    public void setRoot(HCNode root) {
        this.root = root;
    }

    /**
     * put the freq into a binary tree
      * @param freq
     */
    public void buildTree(int[] freq) {

        // initialize a priority queue of HCNode
        PriorityQueue<HCNode> pq = new PriorityQueue<>();
        // create all leaf nodes of your HCTree with frequency
        // values corresponding to each symbol in the freq array
        for (int i = 0; i < freq.length; i++) {
            if (freq[i] != 0) {
                HCNode curNode = new HCNode((byte) i, freq[i]);
                //add them to a Java built-in priority queue
                pq.add(curNode);
                leaves[i] = curNode;
            }

        }
        // build the HCTree using HCNode currently stored
        // in the priority queue.
        //while the size of pq is larger than 1
        while (pq.size() > 1) {
            //    pop the two nodes from the prority queue
            HCNode first = pq.remove();
            HCNode second = pq.remove();
            //    create a new node that has c0 points to the first one and c1 to the other
            HCNode parent = new HCNode(first.getSymbol(), first.getFreq() + second.getFreq());
            parent.setC0(first);
            parent.setC1(second);
            //    update parent of the two nodes we poped
            first.setParent(parent);
            second.setParent(parent);
            //    add the new node back to queue
            pq.add(parent);
        }

        setRoot(pq.remove());


    }

    /**
     * encode the symbol into bitString and stores it into BitOutputStream out
     * @param symbol
     * @param out
     * @throws IOException
     */
    public void encode(byte symbol, BitOutputStream out) throws IOException{

        // For a given symbol, use the HCTree built before to find its
        // encoding bits and write those bits to the given BitOutputStream.
        HCNode curNode = leaves[symbol & 0xff];
        ArrayList<Integer> bytes = new ArrayList<>();
        while (curNode.parent != null) {
            if (curNode.parent.getC0() == curNode)
                bytes.add(0);
            else
                bytes.add(1);
            curNode = curNode.parent;
        }
        for (int i = bytes.size() - 1; i >= 0; i--) {
            out.writeBit(bytes.get(i));

        }

    }

    /**
     * decode the bitstrign from BitInputStream in, and gets its symbol
     * @param in
     * @return the symbol of the founded node
     * @throws IOException
     */
    public byte decode(BitInputStream in) throws IOException{

        // Decodes the bits from BitInputStream and
        // returns a byte that represents the symbol
        // that is encoded by a sequence of bits from BitInputStream.
        HCNode curNode = this.root;

        while (curNode.getC0() != null && curNode.getC1() != null) {
            int bit = in.readBit();
            if (bit == 0)
                curNode = curNode.getC0();
            else
                curNode = curNode.getC1();
        }

        // found the leaf node and return the symbol
        return curNode.getSymbol();
    }

    /**
     * method that prints out our HCTree and see if it matches our drawing
     * @param root
     */
    public void inorder(HCNode root) {

        if (root == null)
            return;

        inorder(root.getC0());
        if (root.getC0() == null && root.getC1() == null) {
            System.out.println(root.toString());
            return;
        }
        inorder(root.getC1());
    }

    /**
     * compressed the  tree for later use of decompressing the file
     * @param node
     * @param out
     * @throws IOException
     */
    public void encodeHCTree(HCNode node, BitOutputStream out) throws IOException{

        // encode the structure of the HCTree and write it to the BitOutputStream
        // node == null? return
        if (node == null)
            return;
        // if node is a leaf => write 1, symbol
        if (node.getC0() == null && node.getC1() == null) {
            out.writeBit(1);
            out.writeByte(node.getSymbol());
        }
        // else => write 0
        else {
            out.writeBit(0);
        }
        // recursive call node.c0, node.c1
        encodeHCTree(node.c0, out);
        encodeHCTree(node.c1, out);

    }

    /**
     * method for implementing decode of the tree the tree
     * @param in
     * @return
     * @throws IOException
     */
    public HCNode decodeHCTree(BitInputStream in) throws IOException {

        // decode bits from the given BitInputStream and build the original HCTree
        int bit = in.readBit();
        HCNode result = null;
        // if bit == 1
        //   recursive call on the rest of in, and put the returned node at c0
        //   then recursive call on the rest of in, and put the returned node ct c1
        //   connect them(setting parent, symbol)
        //   return the node we created.
        if (bit == 0) {
            HCNode left = decodeHCTree(in);
            HCNode right = decodeHCTree(in);
            HCNode parent = new HCNode(left.getSymbol(), 0);
            parent.setC0(left);
            parent.setC1(right);
            left.setParent(parent);
            right.setParent(parent);
            result = parent;
        }
        //else
        //   read the next byte which is the symbol
        //   create a node with the symbol
        //   return this node.
        else {
            byte symbol = in.readByte();
            HCNode node = new HCNode(symbol, 0);
            result = node;
        }
        return result;
    }

}
