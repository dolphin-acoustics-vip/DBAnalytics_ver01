package org.apache.maven.mavenproject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

/**
 * Initialise Database.
 */
public class InitializeDB
{
    public static void main(String[] args) throws SQLException, IOException {
    	
    	// Deal with args exceptions (too little/many arguments than expected)
        if (args.length != 1) {
            System.err.println("Expected 1 argument, but got: " + args.length);
		    System.err.println("Usage: java InitializeDB.java <FileName.db>");
		    System.exit(-1);
        }


        String fileName = "";
        try {
            fileName = args[0];
            if (!fileName.contains(".db")) throw new IllegalArgumentException();
        }
        catch (IllegalArgumentException e) {
            System.out.println("The argument must be a string.");
            System.exit(1);
        };
	
        
//        // Create Directory if it does not already exist
//        Path dirPath = Paths.get(".\\src\\main\\java\\org\\apache\\maven\\mavenproject\\databases");
//        if (!Files.exists(dirPath)){
//        	Files.createDirectory(dirPath);
//        }
         
        
//        Path path = Paths.get(".\\src\\main\\java\\org\\apache\\maven\\mavenproject\\databases\\"+fileName);
        Path path = Paths.get(".\\"+fileName);
        // Delete file if it already exists.
        if (Files.exists(path)){
            Files.delete(path); 
        }
        Path file = Files.createFile(path);


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
            System.out.println("Database Initialized: " + fileName);

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
