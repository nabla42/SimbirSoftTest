package org.nabla.db;

import org.nabla.utills.FileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class DBService {
    private static final Logger LOGGER = Logger.getLogger(FileLoader.class.getName());
    private static final String dbURL = "jdbc:mysql://localhost:3306/";
    private static final String user = "root";
    private static final String pass = "root";
    private final Connection con;
    public DBService() {
        try {
            Path dir = Files.createDirectories(Path.of("./log"));
            FileHandler fh = new FileHandler(dir + File.separator + "log.log");
            fh.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fh);

        } catch (IOException e) {
            e.printStackTrace();
        }
            con = getMySQLConnection();
    }
    public void createTable() {
        try {
            Statement s = con.createStatement();
            s.executeUpdate("create database if not exists statistics");
            s.executeUpdate("use statistics");
            s.executeUpdate("create table if not exists statistic " +
                    "(table_name varchar(50), " +
                    "unique_word varchar(50), " +
                    "word_count int)");
            s.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't create table", ex);
        }
    }
    public void uploadStatistic(Map<String, Integer> map, String tableName, int limit) {
        try {
            PreparedStatement ps = con.prepareStatement("insert into statistic values(?, ?, ?)");
            ps.setFetchSize(800);
                for(Map.Entry<String, Integer> entry : map.entrySet()) {
                    if(limit != 0) {

                    ps.setString(1, tableName);
                    ps.setString(2, entry.getKey());
                    ps.setInt(3, entry.getValue());
                    limit--;

                    ps.executeUpdate();
                    } else
                        break;
                }
            ps.close();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't create statistic table", ex);
        }
    }
    public Map<String, Integer> getStatistic(String tableName) {
        try {
            Map<String, Integer> map = new HashMap<>();
            PreparedStatement ps = con.prepareStatement("select * from statistic where table_name = ?");
            ps.setString(1, tableName);
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                map.put(res.getString("unique_word"), res.getInt("word_count"));
            }
            return map;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't get statistic from DB.", ex);
        }
        return null;
    }
    private Connection getMySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(dbURL, user, pass);

        } catch (ClassNotFoundException | SQLException ex) {
            LOGGER.log(Level.SEVERE, "Can't create SQL connection", ex);
        }
        return null;
    }
}
