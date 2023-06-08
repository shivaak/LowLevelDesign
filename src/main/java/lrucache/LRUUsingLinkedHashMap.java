package lrucache;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUUsingLinkedHashMap {

    private LinkedHashMap<Integer, Integer> lru;

    private int MAX_CAPACITY;

    LRUUsingLinkedHashMap(int capacity){

        this.MAX_CAPACITY = capacity;

        lru = new LinkedHashMap<>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > MAX_CAPACITY;
            }
        };
    }

    public void put(Integer key, Integer value){
        lru.put(key, value);
    }

    public Integer get(Integer key){
        return lru.get(key);
    }

    public LinkedHashMap<Integer, Integer> getCache(){
        return lru;
    }

    public static void main(String[] args) {

        LRUUsingLinkedHashMap cache = new LRUUsingLinkedHashMap(5);

        cache.put(1, 1);
        cache.put(2, 2);
        cache.put(3, 3);
        cache.put(4, 4);
        cache.put(5, 5);
        cache.get(1);
        System.out.println(cache.getCache());
        cache.put(6, 6);
        System.out.println(cache.getCache());

    }
}
