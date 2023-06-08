package lrucache;

import java.util.HashMap;
import java.util.Map;

public class LRUCustom {

    class Node {
        int key;
        int val;
        Node next, previous;
        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", key, val);
        }
    }

    private Map<Integer, Node> map;
    private Node head, tail;
    private final int MAX_CAPACITY;

    public LRUCustom(int capacity){
        this.MAX_CAPACITY = capacity;
        head = new Node(-1,-1);
        tail = new Node(-1,-1);
        head.next = tail;
        tail.previous = head;
        map = new HashMap<>();
    }

    public void put(int key, int value){
        Node node;
        if(map.containsKey(key)){
            node = map.get(key);
            node.previous.next = node.next;
            node.next.previous=node.previous;
        }else{
            if(map.size()==MAX_CAPACITY){
                removeLRU();
            }
            node = new Node(key, value);
        }
        map.put(key, node);
        insertToHead(node);
    }

    private void insertToHead(Node node) {
        //insert next to head
        node.next = head.next;
        head.next.previous = node;
        head.next = node;
        node.previous = head;
    }

    private void removeLRU() {
        Node nodeTobeRemoved = tail.previous;
        map.remove(nodeTobeRemoved.key);
        nodeTobeRemoved.previous.next = tail;
        tail.previous=nodeTobeRemoved.previous;
    }

    public int get(int key){
        if(!map.containsKey(key)){
            return -1;
        }

        Node node = map.get(key);
        node.previous.next = node.next;
        node.next.previous=node.previous;

        insertToHead(node);
        return node.val;
    }

    public void printCache(){
        Node temp = head;
        System.out.print("[");
        while(temp!=null){
            if(temp.val!=-1){
                System.out.print(temp);
                System.out.print(" -> ");
            }
            temp=temp.next;
        }
        System.out.println("]");
    }


    public static void main(String[] args) {
        LRUCustom cache = new LRUCustom(5);
        cache.printCache();

        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4);
        cache.put(5, 5);
        cache.printCache();
        System.out.println(cache.get(1));
        cache.printCache();
        cache.put(6, 6);
        cache.printCache();
        System.out.println(cache.get(4));
        cache.printCache();
        System.out.println(cache.get(100));
        cache.printCache();
        System.out.println(cache.get(3));
        cache.printCache();
        cache.put(7, 7);
        cache.printCache();

    }
}
