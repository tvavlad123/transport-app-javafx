package service;

import model.ValidationException;
import repository.IRepository;
import utils.Observable;
import utils.Observer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;


public class AbstractService<ID, E> implements IService<ID, E>, Observable<E> {

    private IRepository<ID, E> repository;
    private List<Observer<E>> observers;

    public AbstractService(IRepository<ID, E> repository) {
        this.repository = repository;
        observers = new ArrayList<>();
    }

    public AbstractService() {
    }

    @Override
    public void add(E item) throws ValidationException {
        repository.save(item);
        notifyObservers();
    }

    @Override
    public void update(ID id, E item) throws ValidationException {
        repository.update(id, item);
        notifyObservers();
    }

    @Override
    public void remove(ID id) {
        repository.delete(id);
        notifyObservers();
    }

    @Override
    public List<E> getAll() {
        return repository.findAll();
    }

    @Override
    public E find(ID id) {
        return repository.findOne(id);
    }

    @Override
    public List<E> filterAndSorter(List<E> lista, Predicate<E> filter, Comparator<E> comparator) {
        return lista
                .stream()
                .filter(filter)
                .sorted(comparator)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getSize() {
        return repository.size();
    }

    @Override
    public void addObserver(Observer<E> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<E> e) {
        observers.remove(e);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::NotifyOnEvent);
    }
}
