import java.io.*;
import java.sql.*;

public class ParseTxtToDB {
    private static Connection connection;
    private static Statement stmt;

    public static void main(String[] args) {
        try {
            connect();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        /*try {
            //createDB("Bobs");
            //deleteDB("Bobs");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        readFileAndPushToBD();
        disconnect();
    }
    public static void readFileAndPushToBD(){
        try {
            File file = new File("src/test.txt");
            //создаем объект FileReader для объекта File
            FileReader fr = new FileReader(file);
            //создаем BufferedReader с существующего FileReader для построчного считывания
            BufferedReader reader = new BufferedReader(fr);
            // считаем сначала первую строку
            String line = reader.readLine();
            String[] tokens;
            while (line != null) {
                System.out.println(line);
                //парсим строку
                tokens = line.split(" ");
                //конвертируем возраст в число
                int age = 0;
                try {
                    age = Integer.parseInt(tokens[2]);
                } catch (NumberFormatException e) {
                    System.err.println("Неправильный формат строки!");
                }
                //добавляем в БД
                add(tokens[1],age);
                // считываем остальные строки в цикле
                line = reader.readLine();
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:db2.db");
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
                " Name TEXT NOT NULL, " +
                " Age INTEGER NOT NULL )", dbName);
        stmt.executeUpdate(sql);
    }

    public static void add(String name, int age) throws SQLException {
        String sql = "INSERT INTO Bobs (Name, age) VALUES (?,?);";
        PreparedStatement pStmt = connection.prepareStatement(sql);
        pStmt.setString(1, name);
        pStmt.setInt(2, age);
        pStmt.executeUpdate();
    }

    public static void deleteDB(String dbName) throws SQLException {
        String sql = String.format("DROP TABLE IF EXISTS %s", dbName);
        stmt.execute(sql);
    }
}
