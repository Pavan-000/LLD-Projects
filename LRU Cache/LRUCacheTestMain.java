import java.util.*;

class LRUCache {
    class Node{
        int key, value;
        Node prev, next;
        Node(int k, int v){
            key = k;
            value = v;
        }
    }

    private int capacity;
    private Map<Integer, Node> mp;
    private Node head, tail;

    public LRUCache(int capacity){
        this.capacity = capacity;

        mp = new HashMap<>();

        //dummy head and tail to avoid null checks
        head = new Node(0, 0);
        tail = new Node(0, 0);
        head.next = tail;
        tail.prev = head;
    }

    public void put(int key, int value){
        if(mp.containsKey(key)){
            Node node = mp.get(key);
            node.value = value;
            remove(node);
            insertToHead(node);
        }else{
            if(mp.size() == capacity){
                Node lru = tail.prev;
                remove(lru);
                mp.remove(lru.key);

            }
            Node newNode = new Node(key, value);
            insertToHead(newNode);
            mp.put(key, newNode);
        }
    }

    public int get(int key){
        if(!mp.containsKey(key)){
            return -1;
        };

        Node node = mp.get(key);
        remove(node);
        insertToHead(node);
        return node.value;
    }            


    private void remove(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void insertToHead(Node node){
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    
}

public class LRUCacheTestMain{
    public static void main(String args[]){
        LRUCache cache = new LRUCache(3);

        System.out.println(("---------------- Test1  : Basic put and get --------------------"));
        cache.put(1, 10);
        cache.put(2, 20);
        cache.put(3, 30);

        System.out.println(cache.get(1));   // expect 10
        System.out.println(cache.get(2));   // expect 20
        System.out.println(cache.get(3));   // expect 30

        System.out.println("-------  ------ Test 2 : Update Value -----------------------------");

        cache.put(1, 15);
        System.out.println(cache.get(1));   // expect 15

        System.out.println("------------- Test 3 : Eviction when exceeded --------------------");
        cache.put(4, 20);
        System.out.println(cache.get(2));   // expect -1;
        System.out.println(cache.get(1));   // expect 10;
        System.out.println(cache.get(3));   // expect 20;
        System.out.println(cache.get(4));   // expect 40;

        System.out.println("------------- Test 4 : Update Recency --------------------");
        cache = new LRUCache(2);
        cache.put(1, 10);
        cache.put(2, 20);
        cache.get(1);
        cache.put(3, 30);

        System.out.println(cache.get(1));   // expect 40;
        System.out.println(cache.get(2));   // expect -1;
        System.out.println(cache.get(3));   // expect 30;

        System.out.println("---- Test 5: Get Non-Existing Key ----");
        System.out.println(99); // expect -1


        System.out.println("---- Test 6: Capacity One ----");
        cache = new LRUCache(1);
        cache.put(1, 100);
        System.out.println(cache.get(1)); // expect 100
        cache.put(2, 200);
        System.out.println(cache.get(1)); // expect -1
        System.out.println(cache.get(2)); // expect 200

        
    }
}