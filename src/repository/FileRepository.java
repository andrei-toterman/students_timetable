package repository;

import domain.Entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;

public class FileRepository<T extends Entity> extends Repository<T> {
    private final String         fileName;
    private       Constructor<T> csvConstructor;

    public FileRepository(Class<T> type, String fileName) throws NoSuchMethodException {
        super(type);
        this.fileName       = fileName;
        this.csvConstructor = this.type.getConstructor(String.class);
        this.readFromFile();
    }

    @Override
    public void add(T entity) throws Exception {
        super.add(entity);
        this.writeToFile();
    }

    @Override
    public void remove(int id) throws Exception {
        super.remove(id);
        this.writeToFile();
    }

    @Override
    public void update(int id, T newEntity) throws Exception {
        super.update(id, newEntity);
        this.writeToFile();
    }

    private void readFromFile() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.fileName));
            String         line;
            while ((line = br.readLine()) != null)
                this.entities.add(this.csvConstructor.newInstance(line));
            br.close();
        } catch (Exception e) {
            System.out.println("can't read " + this.typeName + " from text file");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void writeToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName));
            for (T entity : this.entities)
                bw.write(entity.toCsvLine() + System.lineSeparator());
            bw.close();
        } catch (Exception e) {
            System.out.println("can't write " + this.typeName + " to text file");
            e.printStackTrace();
            System.exit(0);
        }
    }
}
