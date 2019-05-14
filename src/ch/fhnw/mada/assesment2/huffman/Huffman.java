package ch.fhnw.mada.assesment2.huffman;

import ch.fhnw.mada.assesment2.tree.BaseNode;
import ch.fhnw.mada.assesment2.tree.BranchNode;
import ch.fhnw.mada.assesment2.tree.LeafNode;

import javax.swing.text.DefaultEditorKit;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Huffman {
    private Map<Character, LeafNode> codeMap = new HashMap<>();
    private Map<Character, Integer> probabilityMap = new HashMap<>();
    private BranchNode root;


    public Huffman() {

    }

    public Huffman(Map<Character, Integer> probabilityMap) {
        this.probabilityMap = probabilityMap;
        generateTreeFromCodeMap();
    }

    private void generateTreeFromCodeMap() {
        List<BaseNode> list = new ArrayList<>();
        for (var entry : probabilityMap.entrySet()) {
            var node = new LeafNode(entry.getKey(), entry.getValue());
            list.add(node);
            codeMap.put(node.getCharacter(), node);
        }

        while (list.size() > 1) {
            list.sort((x, y) -> (int) (x.getPriority() - y.getPriority()));
            var lowest = list.get(0);
            var secondLowest = list.get(1);
            list.remove(0);
            list.remove(0);
            list.add(new BranchNode(lowest, secondLowest));
        }
        root = (BranchNode) list.get(0);
    }

    public String serializeHuffmanCode() {
        return codeMap.entrySet()
                .stream()
                .map((x) -> (int) x.getKey() + ":" + x.getValue().getCode())
                .reduce((x, y) -> x + "-" + y)
                .orElse("");
    }

    public void deserializeHuffmanCode(String huffmanCode) {
        var parts = huffmanCode.split("-");
        var rootNode = new BranchNode();
        var leafNodes = new HashMap<Character, LeafNode>();
        for (var part : parts) {
            var results = part.split(":");
            char character = (char) Integer.valueOf(results[0]).intValue();
            var position = results[1];
            var node = setLeafOnBranchNode(rootNode, character, position);
            leafNodes.put(node.getCharacter(), node);
        }
        this.root = rootNode;
        this.codeMap = leafNodes;

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

    private LeafNode setLeafOnBranchNode(BranchNode root, char character, String path) {
        var cursor = path.charAt(0);
        if (path.length() == 1) {
            var leafNode = new LeafNode(character, 0);
            root.setNode(cursor, leafNode);
            return leafNode;
        } else {
            var branchNode = root.getNode(cursor);
            if (branchNode == null) {
                branchNode = new BranchNode();
                root.setNode(cursor, branchNode);
            }
            return setLeafOnBranchNode((BranchNode) branchNode, character, path.substring(1));
        }
    }


}
