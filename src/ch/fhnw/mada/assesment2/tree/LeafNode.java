package ch.fhnw.mada.assesment2.tree;

public class LeafNode extends BaseNode {
    private char character;

    public LeafNode(char character, double priority) {
        super(priority);
        setValue(-1);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public String getCode() {
        var resultBuilder = new StringBuilder();
        resultBuilder.append(getValue());
        for (var node : this) {
            if (node.getValue() != -1) {
                resultBuilder.append(node.getValue());
            }
        }
        return resultBuilder.reverse().toString();
    }
}
