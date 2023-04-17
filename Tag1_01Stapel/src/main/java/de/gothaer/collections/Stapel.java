package de.gothaer.collections;

import java.util.ArrayList;
import java.util.List;

public class Stapel {

    private List<Integer> data;


    public Stapel() {
        data = new ArrayList<>();

    }

    public void push(int value) throws StapelException {
        if(isFull()) throw new StapelException("Overflow");
        data.add(value);
    }

    public int pop() throws StapelException {
        if(isEmpty()) throw new StapelException("Underflow");
        return data.remove(data.size()-1);
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public boolean isFull() {
        return data.size() >= 10;
    }
}
