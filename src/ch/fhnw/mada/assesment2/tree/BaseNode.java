package ch.fhnw.mada.assesment2.tree;

import java.util.Iterator;

public class BaseNode implements Iterable<BaseNode> {
    public static int compareByPriority(BaseNode o1, BaseNode o2) {
        return (int) (o1.getPriority() - o2.getPriority());
    }

    private double priority;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private int value = -1;
    private BaseNode parent;

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


    @Override
    public Iterator<BaseNode> iterator() {
        var self = this;
        return new Iterator<BaseNode>() {
            private BaseNode current = self;

            @Override
            public boolean hasNext() {
                return current.getParent() != null;
            }

            @Override
            public BaseNode next() {
                current = current.getParent();
                return current;
            }
        };
    }
}
