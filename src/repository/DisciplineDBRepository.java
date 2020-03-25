package repository;

import domain.Discipline;

import java.sql.*;

public class DisciplineDBRepository extends Repository<Discipline> {
    private final String     fileName;
    private       Connection conn = null;

    public DisciplineDBRepository(Class<Discipline> type, String fileName) {
        super(type);
        this.fileName = fileName;
        this.openConnection();
        this.createSchema();
        this.readDisciplines();
    }

    private void openConnection() {
        try {
            if (conn == null || conn.isClosed())
                conn = DriverManager.getConnection("jdbc:sqlite:" + this.fileName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createSchema() {
        try {
            final Statement statement = conn.createStatement();
            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS Discipline(id int, [name] varchar(50), credits int, [type] varchar(50))");
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema: " + e.getMessage());
        }
    }

    private void readDisciplines() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Discipline");
            ResultSet         rs        = statement.executeQuery();
            while (rs.next())
                this.entities.add(new Discipline(rs.getInt("id"),
                                                 rs.getString("name"),
                                                 rs.getInt("credits"),
                                                 Discipline.Type.valueOf(rs.getString("type"))
                ));
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Discipline entity) throws Exception {
        super.add(entity);
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Discipline VALUES(?, ?, ?, ?)");
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setInt(3, entity.getCredits());
            statement.setString(4, entity.getType().toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(int id) throws Exception {
        super.remove(id);
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Discipline WHERE id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Discipline newEntity) throws Exception {
        super.update(id, newEntity);
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE Discipline SET id=?, [name]=?, credits=?, [type]=? WHERE id=?");
            statement.setInt(1, newEntity.getId());
            statement.setString(2, newEntity.getName());
            statement.setInt(3, newEntity.getCredits());
            statement.setString(4, newEntity.getType().toString());
            statement.setInt(5, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
