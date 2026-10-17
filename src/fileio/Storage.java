package fileio;

public interface Storage<T> {

    boolean save(String fileName, T data);

    T load(String fileName);
}