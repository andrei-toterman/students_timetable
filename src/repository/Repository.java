package repository;

import domain.Entity;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Repository<T extends Entity> {
    protected final Class<T>     type;
    protected final String       typeName;
    protected       ArrayList<T> entities;

    public Repository(Class<T> type) {
        this.type     = type;
        this.typeName = this.type.getName().substring(this.type.getName().lastIndexOf('.') + 1);
        this.entities = new ArrayList<>();
    }

    private int indexById(int id) throws Exception {
        return IntStream.range(0, this.entities.size())
                        .filter(index -> this.entities.get(index).getId() == id)
                        .findFirst()
                        .orElseThrow(() -> new Exception(this.typeName + " with id " + id + " not found"));
    }

    public void add(T entity) throws Exception {
        if (this.entities.contains(entity))
            throw new Exception(this.typeName + " with id " + entity.getId() + " already exists");
        this.entities.add(entity);
    }

    public T get(int id) throws Exception {
        return this.entities.get(this.indexById(id));
    }

    public void remove(int id) throws Exception {
        this.entities.remove(this.indexById(id));
    }

    public void update(int id, T newEntity) throws Exception {
        this.entities.set(this.indexById(id), newEntity);
    }

    public ArrayList<T> get() {
        return this.entities;
    }
}
