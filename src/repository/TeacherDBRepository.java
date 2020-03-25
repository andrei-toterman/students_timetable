package repository;

import domain.Teacher;

import java.sql.*;

public class TeacherDBRepository extends Repository<Teacher> {
    private final String     fileName;
    private       Connection conn = null;

    public TeacherDBRepository(Class<Teacher> type, String fileName) {
        super(type);
        this.fileName = fileName;
        this.openConnection();
        this.createSchema();
        this.readTeachers();
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
                    "CREATE TABLE IF NOT EXISTS Teacher(id int, [name] varchar(50), email varchar(50), [rank] varchar(50), cnp int)");
            statement.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] createSchema: " + e.getMessage());
        }
    }

    private void readTeachers() {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Teacher");
            ResultSet         rs        = statement.executeQuery();
            while (rs.next())
                this.entities.add(new Teacher(rs.getInt("id"),
                                              rs.getString("name"),
                                              rs.getString("email"),
                                              Teacher.Rank.valueOf(rs.getString("rank")),
                                              rs.getLong("cnp")
                ));
            rs.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void add(Teacher entity) throws Exception {
        super.add(entity);
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Teacher VALUES(?, ?, ?, ?, ?)");
            statement.setInt(1, entity.getId());
            statement.setString(2, entity.getName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getRank().toString());
            statement.setInt(5, (int) entity.getCnp());
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
            PreparedStatement statement = conn.prepareStatement("DELETE FROM Teacher WHERE id=?");
            statement.setInt(1, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int id, Teacher newEntity) throws Exception {
        super.update(id, newEntity);
        try {
            PreparedStatement statement = conn.prepareStatement(
                    "UPDATE Teacher SET id=?, [name]=?, email=?, [rank]=?, cnp=? WHERE id=?");
            statement.setInt(1, newEntity.getId());
            statement.setString(2, newEntity.getName());
            statement.setString(3, newEntity.getEmail());
            statement.setString(4, newEntity.getRank().toString());
            statement.setInt(5, (int) newEntity.getCnp());
            statement.setInt(6, id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
