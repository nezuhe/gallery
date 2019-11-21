package pl.piotrchowaniec.gallery.models.mappers;

public abstract class Mapper<K, V> {

    public abstract V map(K key);
}
