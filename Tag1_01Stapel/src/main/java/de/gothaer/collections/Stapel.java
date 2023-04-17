package de.gothaer.collections;

public class Stapel {

    private int data[];
    private int index;

    public Stapel() {
        data = new int[10];
        index = 0;
    }

    public void push(int value) throws StapelException {
        if(isFull()) throw new StapelException("Overflow");
        data[index++] = value;
    }

    public int pop() throws StapelException {
        if(isEmpty()) throw new StapelException("Underflow");
        return data[--index];
    }

    public boolean isEmpty() {
        return index == 0;
    }

    public boolean isFull() {
        return index == data.length;
    }
}
