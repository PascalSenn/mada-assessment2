package ch.fhnw.mada.assesment2.huffman;

import ch.fhnw.mada.assesment2.tree.BaseNode;
import ch.fhnw.mada.assesment2.tree.BranchNode;
import ch.fhnw.mada.assesment2.tree.LeafNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanBuilder {
    private Map<Character, Integer> probabilityMap = new HashMap<>();
    private Map<Character, LeafNode> codeMap = new HashMap<>();
    private BranchNode root;

    public HuffmanBuilder() {

    }

    public void add(char character) {
        probabilityMap.put(character, probabilityMap.getOrDefault(character, 0) + 1);
    }

    public Huffman build() {
        generateTreeFromCodeMap();
        return new Huffman(codeMap, root);
    }

    public Huffman build(String huffmanCode) {
        var parts = huffmanCode.split("-");
        root = new BranchNode();
        codeMap = new HashMap<Character, LeafNode>();
        for (var part : parts) {
            var results = part.split(":");
            char character = (char) Integer.valueOf(results[0]).intValue();
            var position = results[1];
            var node = setLeafOnBranchNode(root, character, position);
            codeMap.put(node.getCharacter(), node);
        }
        return new Huffman(codeMap, root);
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

    private void generateTreeFromCodeMap() {
        List<BaseNode> list = new ArrayList<>();
        for (var entry : probabilityMap.entrySet()) {
            var node = new LeafNode(entry.getKey(), entry.getValue());
            list.add(node);
            codeMap.put(node.getCharacter(), node);
        }

        while (list.size() > 1) {
            list.sort(BaseNode::compareByPriority);
            var lowest = list.get(0);
            var secondLowest = list.get(1);
            list.remove(0);
            list.remove(0);
            list.add(new BranchNode(lowest, secondLowest));
        }
        root = (BranchNode) list.get(0);
    }

}
