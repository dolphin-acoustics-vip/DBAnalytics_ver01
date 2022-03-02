package org.apache.maven.mavenproject;

import java.io.*;
import java.sql.*;
import java.util.*; 

/**
 * Initialise Database.
 */
public class InitializeDB
{
    public static void main(String[] paramArrayOfString) throws SQLException {

		
            String fileName = "pmg.db";
            File file = new File("src/../" + fileName);

            // Delete file if it already exists.
            if (file.exists()){
                file.delete(); 
            }


            Scanner sc;
            Connection connection = null;
            String txtFile = "src/../DDL.txt";

            // Access DDL file
            try {
                sc = new Scanner(new File(txtFile));
            } catch (FileNotFoundException e) {
                System.out.println("File not found: " + txtFile.substring(txtFile.lastIndexOf("\\")+1).trim());
                return;
            } 

            
            try {
                // Establish connection and create a statement
                String url = "jdbc:sqlite:" + fileName;
                connection = DriverManager.getConnection(url);
                Statement statement = connection.createStatement();

                // Execute each like of the txtFile as an SQL update
                while (sc.hasNextLine()) {
                    statement.executeUpdate(sc.nextLine()); 
                }

                // Close Scanner
                sc.close();
                statement.close();
                System.out.println("Database Initialized.");

            } catch (SQLException e) {
                System.out.println(e.getMessage());

            } finally {
                // Close connection
                if (connection != null) {
                    connection.close(); 
                }
            } 
        }
    
}
