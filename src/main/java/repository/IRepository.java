package repository;

import model.ValidationException;

import java.util.List;

public interface IRepository<ID, T> {

    int size();

    void save(T entity) throws ValidationException;

    void update(ID id, T entity) throws ValidationException;

    void delete(ID id);

    T findOne(ID id);

    List<T> findAll();
}
