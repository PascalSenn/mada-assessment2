package ch.fhnw.mada.assesment2.huffman;

import java.util.HashMap;
import java.util.Map;

public class HuffmanBuilder {
    private Map<Character, Integer> codeMap = new HashMap<>();
    public HuffmanBuilder() {

    }

    public void add(char character) {
        codeMap.put(character, codeMap.getOrDefault(character, 0) + 1);
    }

    public Huffman build() {
        return new Huffman(codeMap);
    }

}
