import java.sql.*;

public class Main {
    private static Connection connection;
    private static Statement stmt;
    private static PreparedStatement pStmt;

    public static void main(String[] args) {
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        try {
            //createDB("JavaLesson2Level3");
            //fillByDefault5Values();
            //add("UltraStupid", 1, 73, 35);
            displayDataFromName("UltraStupid");
            //delete(2);
            //deleteDB("JavaLesson2Level3");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnect();
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:db1.db");
        stmt = connection.createStatement();
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void createDB(String dbName) throws SQLException {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (" +
                " id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                " studentName TEXT NOT NULL, " +
                " number_of_offTopic_questions INTEGER, " +
                " number_of_stupid_questions INTEGER, " +
                " age INTEGER NOT NULL )", dbName);
        stmt.executeUpdate(sql);
    }

    public static void fillByDefault5Values() throws SQLException {
        String sql = "INSERT INTO JavaLesson2Level3 (studentName, number_of_offTopic_questions, " +
                                                "number_of_stupid_questions, age) VALUES (?,?,?,?);";
        pStmt = connection.prepareStatement(sql);
        for (int i = 1; i < 6; i++) {
            pStmt.setString(1, "BadStudent" + i);
            pStmt.setInt(2, i * 5);
            pStmt.setInt(3, i * 3);
            pStmt.setInt(4, i + 28);
            pStmt.addBatch();
        }
        pStmt.executeBatch();
    }

    public static void add(String name, int qOffTopic, int qStupid, int age) throws SQLException {
        String sql = "INSERT INTO JavaLesson2Level3 (studentName, number_of_offTopic_questions, " +
                "number_of_stupid_questions, age) VALUES (?,?,?,?);";
        pStmt = connection.prepareStatement(sql);
        pStmt.setString(1, name);
        pStmt.setInt(2, qOffTopic);
        pStmt.setInt(3, qStupid);
        pStmt.setInt(4, age);
        pStmt.executeUpdate();
    }

    public static void delete(int id) throws SQLException {
        String sql = String.format("DELETE FROM JavaLesson2Level3 WHERE id = %s", id);
        stmt.execute(sql);
    }

    public static void displayDataFromName (String name) throws SQLException {
        String sql = "SELECT studentName, number_of_offTopic_questions, number_of_stupid_questions, age FROM JavaLesson2Level3 where studentName = \"" + name + "\"";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.println(rs.getString(1) + ": " + rs.getInt(2) + " " + rs.getInt(3) + " " + rs.getInt(4));
        }
    }

    public static void deleteDB(String dbName) throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", dbName);
        stmt.execute(sql);
    }
}
