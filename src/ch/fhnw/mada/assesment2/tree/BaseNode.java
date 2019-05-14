package ch.fhnw.mada.assesment2.tree;

public class BaseNode {

    private double priority;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value = -1;
    private BaseNode  parent;

    public BaseNode(double priority) {
        this.priority = priority;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public BaseNode getParent() {
        return parent;
    }

    public void setParent(BaseNode parent) {
        this.parent = parent;
    }
}
