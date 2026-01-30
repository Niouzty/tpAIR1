package fr.uit.univparis8.tpair.tpair1.dao;

import java.util.List;

public abstract class DAO<T> {
    public abstract T create(T obj);
    public abstract T find(int id);
    public abstract List<T> findAll();
    public abstract boolean update(T obj);
    public abstract boolean delete(int id);
}
