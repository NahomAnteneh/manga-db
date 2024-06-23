package com.manga.storage;

import java.util.ArrayList;
import java.util.List;

/**
 * A B-Tree data structure that stores key-value pairs.
 * 
 * @param <Key> the type of the keys, which must implement Comparable
 * @param <Value> the type of the values
 * 
 */
public class BTree<Key extends Comparable<Key>, Value> {
    private static final int T = 4; // minimum degree of the tree
    private Node<Key, Value> root;

    public BTree() {
        root = new LeafNode<>();
    }

    public void insert(Key key, Value value) {
        root = root.insert(key, value);
        if (root.isOverflow()) {
            Node<Key, Value> newRoot = new InternalNode<>();
            newRoot.children.add(root);
            root = newRoot;
            // root.splitChild(0);
        }
    }

    public Value search(Key key) {
        return root.search(key);
    }

    public void delete(Key key) {
        root = root.delete(key);
        if (root instanceof InternalNode && root.keys.isEmpty()) {
            root = ((InternalNode<Key, Value>) root).children.get(0); // Adjust root if necessary
        }
    }

    public List<Value> searchRange(Key low, Key high) {
        List<Value> result = new ArrayList<>();
        root.searchRange(low, high, result);
        return result;
    }

    private abstract class Node<Key extends Comparable<Key>, Value> {
        protected List<Key> keys;
        protected List<Node<Key, Value>> children;

        public Node() {
            keys = new ArrayList<>();
            children = new ArrayList<>();
        }

        public abstract Node<Key, Value> insert(Key key, Value value);

        public abstract Value search(Key key);

        public abstract Node<Key, Value> delete(Key key);

        public abstract void searchRange(Key low, Key high, List<Value> result);

        public boolean isOverflow() {
            return keys.size() >= 2 * T - 1;
        }

        public boolean isUnderflow() {
            return keys.size() < T - 1;
        }
    }

    private class LeafNode<Key extends Comparable<Key>, Value> extends Node<Key, Value> {
        private List<Value> values;

        public LeafNode() {
            super();
            values = new ArrayList<>();
        }

        @Override
        public Node<Key, Value> insert(Key key, Value value) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) < 0) {
                i++;
            }
            keys.add(i, key);
            values.add(i, value);
            return handleOverflow();
        }

        @Override
        public Value search(Key key) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) < 0) {
                i++;
            }
            if (i < keys.size() && keys.get(i).compareTo(key) == 0) {
                return values.get(i);
            }
            return null;
        }

        @Override
        public Node<Key, Value> delete(Key key) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) < 0) {
                i++;
            }
            if (i < keys.size() && keys.get(i).compareTo(key) == 0) {
                keys.remove(i);
                values.remove(i);
            }
            return handleUnderflow();
        }

        @Override
        public void searchRange(Key low, Key high, List<Value> result) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(low) < 0) {
                i++;
            }
            while (i < keys.size() && keys.get(i).compareTo(high) <= 0) {
                result.add(values.get(i));
                i++;
            }
        }

        private Node<Key, Value> handleOverflow() {
            if (isOverflow()) {
                LeafNode<Key, Value> newLeaf = new LeafNode<>();
                int mid = keys.size() / 2;

                newLeaf.keys.addAll(keys.subList(mid, keys.size()));
                newLeaf.values.addAll(values.subList(mid, values.size()));
                keys.subList(mid, keys.size()).clear();
                values.subList(mid, values.size()).clear();

                return newLeaf;
            }
            return this;
        }

        private Node<Key, Value> handleUnderflow() {
            if (isUnderflow() && this != root) {
                return null; // Cannot underflow below root in this implementation
            }
            return this;
        }
    }

    private class InternalNode<Key extends Comparable<Key>, Value> extends Node<Key, Value> {
        public InternalNode() {
            super();
        }

        @Override
        public Node<Key, Value> insert(Key key, Value value) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) < 0) {
                i++;
            }
            Node<Key, Value> child = children.get(i);
            Node<Key, Value> newChild = child.insert(key, value);
            if (newChild != child) {
                keys.add(i, newChild.keys.get(0));
                children.add(i + 1, newChild);
                if (isOverflow()) {
                    return handleOverflow();
                }
            }
            return this;
        }

        @Override
        public Value search(Key key) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) <= 0) {
                i++;
            }
            return children.get(i).search(key);
        }

        @Override
        public Node<Key, Value> delete(Key key) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(key) < 0) {
                i++;
            }
            Node<Key, Value> child = children.get(i);
            Node<Key, Value> newChild = child.delete(key);
            if (newChild == null) {
                children.remove(i);
                keys.remove(i);
                return handleUnderflow();
            } else if (newChild != child) {
                keys.set(i, newChild.keys.get(0));
            }
            return this;
        }

        @Override
        public void searchRange(Key low, Key high, List<Value> result) {
            int i = 0;
            while (i < keys.size() && keys.get(i).compareTo(low) < 0) {
                i++;
            }
            while (i < keys.size() && keys.get(i).compareTo(high) <= 0) {
                children.get(i).searchRange(low, high, result);
                i++;
            }
            if (i < children.size()) {
                children.get(i).searchRange(low, high, result);
            }
        }

        private Node<Key, Value> handleOverflow() {
            if (isOverflow()) {
                InternalNode<Key, Value> newInternal = new InternalNode<>();
                int mid = keys.size() / 2;

                newInternal.keys.addAll(keys.subList(mid + 1, keys.size()));
                newInternal.children.addAll(children.subList(mid + 1, children.size()));
                keys.subList(mid, keys.size()).clear();
                children.subList(mid + 1, children.size()).clear();

                return newInternal;
            }
            return this;
        }

        private Node<Key, Value> handleUnderflow() {
            if (isUnderflow() && this != root) {
                return null; // Cannot underflow below root in this implementation
            }
            return this;
        }
    }
}
