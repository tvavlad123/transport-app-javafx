package utils;

public interface Observable<T> {
    void addObserver(Observer<T> e);
    void removeObserver(Observer<T> e);
    void notifyObservers();
}
