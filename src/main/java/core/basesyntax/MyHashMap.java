package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private int capacity = DEFAULT_CAPACITY;
    private int size;
    private Element[] elements = new Element[capacity];

    @Override
    public void put(K key, V value) {
        if (size > elements.length * LOAD_FACTOR) {
            resize();
        }
        int index = key == null ? 0 : Math.abs(key.hashCode()) % capacity;
        if (elements[index] == null) {
            elements[index] = new Element(key, value, null);
            size++;
        } else {
            processAndPut(index, key, value);
        }
    }

    public void processAndPut(int index, K key, V value) {
        Element<K, V> elementCopy = elements[index];
        boolean flag = false;
        while (elementCopy != null) {
            if (key == elementCopy.key || key.equals(elementCopy.key)) {
                elementCopy.value = value;
                return;
            }
            elementCopy = elementCopy.next;
        }
        if (!flag) {
            Element<K, V> newElement = new Element<>(key, value, elements[index]);
            elements[index] = newElement;
            size++;
        }
    }

    private void resize() {
        if (size > elements.length * LOAD_FACTOR) {
            size = 0;
            Element[] newElements = elements;
            elements = new Element[capacity * 2];
            for (Element<K, V> elementItem : newElements) {
                while (elementItem != null) {
                    put(elementItem.key, elementItem.value);
                    elementItem = elementItem.next;
                }
            }
            capacity *= 2;
        }
    }

    @Override
    public V getValue(K key) {
        if (key == null) {
            return (V) elements[0].value;
        }

        int index = key == null ? 0 : Math.abs(key.hashCode()) % capacity;
        Element<K, V> newElementItem = elements[index];
        while (newElementItem != null) {
            if (key == newElementItem.key || key.equals(newElementItem.key)) {
                return newElementItem.value;
            }
            newElementItem = newElementItem.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private static class Element<K, V> {
        private K key;
        private V value;
        private Element next;

        private Element(K key, V value, Element next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
