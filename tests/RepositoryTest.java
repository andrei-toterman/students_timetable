import domain.Entity;
import repository.Repository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryTest {

    Repository<Entity> repo;

    @BeforeEach
    void setUp() {
        repo = new Repository<>(Entity.class);
    }

    @AfterEach
    void tearDown() {
        repo = null;
    }

    @Test
    void add() {
        assertEquals(0, repo.get().size());
        try {
            repo.add(new Entity(1));
            assertEquals(1, repo.get().size());
        } catch (Exception ignored) {
        }
        try {
            repo.add(new Entity(1));
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
        try {
            repo.add(new Entity(2));
            assertEquals(2, repo.get().size());
        } catch (Exception ignored) {
        }
    }

    @Test
    void get() {
        try {
            repo.add(new Entity(1));
        } catch (Exception ignored) {
        }
        try {
            assertEquals(1, repo.get(1).getId());
        } catch (Exception ignored) {
        }
        try {
            repo.get(2);
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    void remove() {
        try {
            repo.add(new Entity(1));
        } catch (Exception ignored) {
        }
        try {
            repo.remove(1);
            assertEquals(0, repo.get().size());
        } catch (Exception ignored) {
        }
        try {
            repo.remove(2);
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    void update() {
        try {
            repo.add(new Entity(1));
        } catch (Exception ignored) {
        }
        try {
            repo.update(1, new Entity(2));
            assertEquals(2, repo.get(2).getId());
        } catch (Exception ignored) {
        }
        try {
            repo.update(3, new Entity(1));
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    void testGet() {
        try {
            repo.add(new Entity(1));
            repo.add(new Entity(2));
        } catch (Exception ignored) {
        }
        var entities = repo.get();
        assertEquals(2, entities.size());
        assertEquals(1, entities.get(0).getId());
        assertEquals(2, entities.get(1).getId());
    }
}
