package ch.fhnw.mada.assesment2.huffman;

import ch.fhnw.mada.assesment2.tree.BaseNode;
import ch.fhnw.mada.assesment2.tree.BranchNode;
import ch.fhnw.mada.assesment2.tree.LeafNode;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Huffman {
    private Map<Character, LeafNode> codeMap = new HashMap<>();
    private BranchNode root;

    public Huffman(Map<Character, LeafNode> codeMap, BranchNode root) {
        this.codeMap = codeMap;
        this.root = root;
    }


    public String serializeHuffmanCode() {
        return codeMap.entrySet()
                .stream()
                .map((x) -> (int) x.getKey() + ":" + x.getValue().getCode())
                .reduce((x, y) -> x + "-" + y)
                .orElse("");
    }

    private String toBinaryString(int b) {
        var tempString = Integer.toBinaryString(b & 0xFF);
        var builder = new StringBuilder(tempString).reverse();
        while (builder.length() < 8) {
            builder.append(0);
        }
        return builder.reverse().toString();
    }

    public String decompressString(FileInputStream fis) throws IOException {
        var bytes = fis.readAllBytes();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        var inputString = IntStream.generate(buffer::get)
                .limit(buffer.remaining())
                .mapToObj(this::toBinaryString)
                .reduce((x, y) -> x + y).orElse("");
        inputString = inputString.substring(0, inputString.lastIndexOf("1"));
        BaseNode currentNode = root;
        var resultBuilder = new StringBuilder();
        for (var character : inputString.toCharArray()) {
            var branchNode = (BranchNode) currentNode;
            currentNode = branchNode.getNode(character);
            if (currentNode instanceof LeafNode) {
                resultBuilder.append(((LeafNode) currentNode).getCharacter());
                currentNode = root;
            }
        }
        return resultBuilder.toString();
    }

    public String compressString(String text) {
        return text.chars()
                .mapToObj(x ->
                        codeMap.get((char) x).getCode()
                ).collect(Collectors.joining());
    }


}
