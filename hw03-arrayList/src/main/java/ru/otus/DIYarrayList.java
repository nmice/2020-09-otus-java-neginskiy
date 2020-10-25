package ru.otus;

import java.util.*;

public class DIYarrayList<T> implements List<T> {

    private static final int DEFAULT_CAPACITY = 16;

    public DIYarrayList() {
        this(DEFAULT_CAPACITY);
    }

    public DIYarrayList(int capacity) {
        this.array = new Object[capacity];
    }

    private Object[] array;
    private int currentIndex = 0;

    @Override
    public int size() {
        return currentIndex;
    }

    @Override
    public boolean isEmpty() {
        return currentIndex == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    private class DIYiterator<T> implements Iterator<T> {
        int cursor;

        @Override
        public boolean hasNext() {
            return cursor < currentIndex;
        }

        @Override
        public T next() {
            return (T) array[cursor++];
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new DIYiterator<T>();
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(array, currentIndex);
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T t) {
        if (currentIndex == array.length - 1) {
            grow();
        }
        array[currentIndex++] = t;
        return true;
    }

    private void grow() {
        int newCapacity = array.length * 2;
        Object[] growingArray = new Object[newCapacity];
        System.arraycopy(array, 0, growingArray, 0, currentIndex);
        array = growingArray;
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get(int index) {
        return (T) array[index];
    }

    @Override
    public T set(int index, T element) {
        T returnedObject = (T) array[index];
        array[index] = element;
        return returnedObject;
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    private class DIYlistIterator<T> extends DIYiterator<T> implements ListIterator<T> {
        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public T previous() {
            return (T) array[cursor - 1];
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            array[cursor - 1] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DIYlistIterator<T>();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean isNotFirstElement = false;
        for (Object o : this) {
            if (isNotFirstElement) {
                sb.append(", ");
            }
            sb.append(o.toString());
            isNotFirstElement = true;
        }
        sb.append("]");
        return sb.toString();
    }
}
