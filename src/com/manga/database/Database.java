package com.manga.database;

import java.util.ArrayList;
import java.util.List;

public class Database {
    
    static class Node {
        List<String> keys;
        List<Node> children;
        List<List<String>> rows;

        public Node() {
            // this.table = new ArrayList<>();
            this.children = new ArrayList<>();
            this.rows = new ArrayList<>();
        }
    }

   
    static class BTree {
        Node root;
        int t;

        public BTree(int t) {
            this.t = t;
            this.root = new Node();
        }

       
        public void createTable(String tableName, List<String> columns) {
            if (root.keys.isEmpty()) {
                root.keys.add(tableName);
                root.rows.add(columns);
            } else {
                insert(root, tableName, columns);
            }
        }

        private void insert(Node node, String key, List<String> columns) {
            
            if (node.keys.size() == 2 * t - 1) {
                Node newRoot = new Node();
                newRoot.children.add(node);
                splitChild(newRoot, 0, node);
                root = newRoot;
                insertNonFull(newRoot, key, columns);
            } else {
                insertNonFull(node, key, columns);
            }
        }

        private void insertNonFull(Node node, String key, List<String> columns) {
            int i = node.keys.size() - 1;
            if (node.children.isEmpty()) {
               
                while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                    i--;
                }
                node.keys.add(i + 1, key);
                node.rows.add(i + 1, columns);
            } else {
                // Traverse to the correct child node
                while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                    i--;
                }
                i++;
                if (node.children.get(i).keys.size() == 2 * t - 1) {
                    splitChild(node, i, node.children.get(i));
                    if (key.compareTo(node.keys.get(i)) > 0) {
                        i++;
                    }
                }
                insertNonFull(node.children.get(i), key, columns);
            }
        }

        private void splitChild(Node parent, int index, Node child) {
            Node newNode = new Node();
            parent.keys.add(index, child.keys.get(t - 1));
            parent.children.add(index + 1, newNode);
            newNode.keys.addAll(child.keys.subList(t, child.keys.size()));
            newNode.rows.addAll(child.rows.subList(t, child.rows.size()));
            child.keys.subList(t - 1, child.keys.size()).clear();
            child.rows.subList(t - 1, child.rows.size()).clear();
            if (!child.children.isEmpty()) {
                newNode.children.addAll(child.children.subList(t, child.children.size()));
                child.children.subList(t, child.children.size()).clear();
            }
        }
    }
}