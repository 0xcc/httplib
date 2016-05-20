package mike.httplib.cache;

public interface ICache<K,V> {
	
    public V get(K key);

    public void put(K key, V value);

    public void remove(K key);

}
