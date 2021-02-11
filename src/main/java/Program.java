


import java.sql.*;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;


public class Program {

    //константы подключения
    static String url = "jdbc:mysql://localhost:3306/new";
    static String login = "root";
    static String password = "1234";
    static String driver = "com.mysql.cj.jdbc.Driver";
    static String table_name = "users";
    static Connection con = null;
    static Scanner scanner = new Scanner(System.in);

    /**
     * Метод подключения к базе данных
     * @return
     */
    public static Connection getConnect() {
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, login, password);
            System.out.println("Подключение успешно!");
        } catch (SQLException e) {
            System.out.println("Подключение не удалось");
        } catch (ClassNotFoundException g) {
            System.out.println("Драйвер не найден");
        }
        return con;
    }

    public static void closeConnect() {
        Connection connection = null;
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Метод ввода данных в базу данных
     */
    public static void insertValue() {
        Random random = new Random();
        String query = "insert into " + table_name
                + " (user_firstname, user_secondname, user_login, user_password, unique_id)" + " values (?, ?, ?, ?, ?)";
        System.out.println("Введите имя пользователя: ");
        String usName = scanner.nextLine();
        System.out.println("Введите фамилию пользователя: ");
        String usSecondName = scanner.nextLine();
        System.out.println("Введите логин пользователя: ");
        String usLogin = scanner.nextLine();
        System.out.println("Введите пароль пользователя: ");
        String usPassword = scanner.nextLine();
        String uniqueId = UUID.randomUUID().toString();

        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, usName);
            preparedStatement.setString(2, usSecondName);
            preparedStatement.setString(3, usLogin);
            preparedStatement.setString(4, usPassword);
            preparedStatement.setString(5, uniqueId);
            preparedStatement.execute();
            System.out.println("Данные введены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Вывод всех данных таблицы
     */
    public static void showDatabaseAllValue() {
        String sql = "select * from " + table_name;
      //  String sh = "select user_name " + "from " + table_name;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.execute();
            ResultSet rec = preparedStatement.getResultSet();
            while (rec.next()) {
                System.out.println(rec.getInt("idusers") + " FirstName: " +
                        rec.getString("user_firstname") + " SecondName: " +
                        rec.getString("user_secondname") + " Login: " +
                        rec.getString("user_login") + " Password: " +
                        rec.getString("user_password") + " UniqueId: " +
                        rec.getString("unique_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Поиск строк по фамилии
     */
    public static void selectRowBySecondName() {
        System.out.println("Введите искомую фамилию: ");
        String searchSecondName = scanner.nextLine();
        String select = "SELECT * FROM " + table_name + " WHERE user_secondname LIKE " + "'%" + searchSecondName + "%'";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(select);
            preparedStatement.execute();
            ResultSet rec = preparedStatement.getResultSet();
            while (rec.next()) {
                System.out.println(rec.getInt("idusers") + " FirstName: " +
                        rec.getString("user_firstname") + " SecondName: " +
                        rec.getString("user_secondname") + " Login: " +
                        rec.getString("user_login") + " Password: " +
                        rec.getString("user_password") + " UniqueId: " +
                        rec.getString("unique_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Метод удаления всех данных из таблицы
     */
    public static void removeValuesInTable() {
        String del = "truncate table " + table_name;

        try {
            PreparedStatement preparedStatement = con.prepareStatement(del);
            preparedStatement.execute();
            ResultSet rec = preparedStatement.getResultSet();
            System.out.println("Все данные из таблицы удалены!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        while (true) {
            System.out.println("Menu: " +
                    "\n1. Ввод значений в базу данных" +
                    "\n2. Вывод всех значений базы данных" +
                    "\n3. Удаление всех данных из таблицы" +
                    "\n4. Поиск по фамилии" +
                    "\n5. Выход");
            String choice = scanner.nextLine();
            if (choice.equals("1")) {
                System.out.println("=== Введите свои данные ===");
                insertValue();
            } else if (choice.equals("2")) {
                showDatabaseAllValue();
            } else if (choice.equals("3")) {
                removeValuesInTable();
            } else if (choice.equals("4")) {
                selectRowBySecondName();
            }
            else {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("Пока!!!");
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        getConnect();
        init();
        closeConnect();
    }


}