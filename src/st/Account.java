package st;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class Account {

    private static Connection con;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/satellite_tracker?serverTimezone=UTC", "root", "1234");
        } catch (Throwable e) {
            assert(false);
        }
    }

    private int id;

    public Account(String username, String password) throws Exception {
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        password = toHexString(hash);

        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT `id`, `password_hash` FROM `accounts` WHERE `username` = '" + username + "' LIMIT 1;");

        rs.next();
        id = rs.getInt(1);
        String passwordStored = rs.getString(2);

        if(!password.equals(passwordStored)) {
            throw new Exception("Wrong username or password.");
        }
    }

    public void saveSatellite(Set<Integer> norads) {
        try {

            Statement statement = con.createStatement();
            statement.execute("DELETE FROM `favourites` WHERE `account_id` = '" + id + "';");

            for(int norad : norads) {
                statement.execute("INSERT INTO `favourites` (`account_id`, `norad_id`) VALUES(" + id + ", " + norad + ")");
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getSatellites() {
        List<Integer> norads = new Vector<>();

        try {

            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM `favourites` WHERE `account_id` = " + id + ";");

            while(rs.next()) {
                norads.add(rs.getInt(2));
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }

        return norads;

    }

    public static Account createAccount(String username, String password) throws Exception {
        byte[] hash = null;

        try {
            hash = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        String passwordHex = toHexString(hash);

        try {
            Statement statement = con.createStatement();
            statement.execute("INSERT INTO `accounts` (`username`, `password_hash`) VALUES('" + username + "', '" + passwordHex + "');");
        }catch(SQLException e) {
            throw new Exception("Username already taken.");
        }

        return new Account(username, password);

    }

    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);

    private static String toHexString(byte[] b) {
        byte[] hexChars = new byte[b.length * 2];

        for (int j = 0; j < b.length; j++) {
            int v = b[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }

        return new String(hexChars, StandardCharsets.UTF_8);
    }

}
