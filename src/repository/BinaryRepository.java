package repository;

import domain.Entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class BinaryRepository<T extends Entity> extends Repository<T> {
    private final String fileName;

    public BinaryRepository(Class<T> type, String fileName) {
        super(type);
        this.fileName = fileName;
        this.deserialize();
    }

    @Override
    public void add(T entity) throws Exception {
        super.add(entity);
        this.serialize();
    }

    @Override
    public void remove(int id) throws Exception {
        super.remove(id);
        this.serialize();
    }

    @Override
    public void update(int id, T newEntity) throws Exception {
        super.update(id, newEntity);
        this.serialize();
    }

    private void deserialize() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(this.fileName));
            this.entities = (ArrayList<T>) in.readObject();
            in.close();
        } catch (Exception e) {
            System.out.println("can't read " + this.typeName + " from binary file");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void serialize() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(this.fileName));
            out.writeObject(this.entities);
            out.close();
        } catch (Exception e) {
            System.out.println("can't write " + this.typeName + " to binary file");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
