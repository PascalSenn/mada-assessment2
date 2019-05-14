package ch.fhnw.mada.assesment2.tree;

public class BranchNode extends BaseNode {
    private BaseNode left;
    private BaseNode right;

    public BranchNode() {
        super(0);
    }

    public BranchNode(BaseNode left, BaseNode right) {
        super(left.getPriority() + right.getPriority());
        setLeft(left);
        setRight(right);
    }

    public void setNode(char character, BaseNode node) {
        if (character == '1') {
            setRight(node);
        } else {
            setLeft(node);
        }
    }

    public BaseNode getNode(char character) {
        if (character == '1') {
            return getRight();
        } else {
            return getLeft();
        }
    }

    public BaseNode getLeft() {
        return left;
    }

    public void setLeft(BaseNode left) {
        left.setValue(0);
        left.setParent(this);
        this.left = left;
    }

    public BaseNode getRight() {
        return right;
    }

    public void setRight(BaseNode right) {
        right.setParent(this);
        right.setValue(1);
        this.right = right;
    }

    @Override
    public double getPriority() {
        return left.getPriority() + right.getPriority();
    }
}
