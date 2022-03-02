package org.apache.maven.mavenproject;

/**
 * This class populates the data tables created in InitializeID.java with random data.
 * Successful population of the data tables prints "Database Populated."
 */

import java.io.*;
import java.sql.*;
import java.util.*;
import java.text.ParseException;
import java.util.concurrent.ThreadLocalRandom;


class PopulateDB {

    /**
     * Method Executes an SQL Query and returns a single string value
     * @param conn [Connection] to enable the establishing online connection to database
     * @param SQLquery [String] contains query in SQL format which interacts with database when executed
     * @return String
     * @throws SQLException
     */
    public static String requestElement(Connection connection, String SQLquery) throws SQLException{
        
        String element = "";

        try {
            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(SQLquery);
            element = results.getString(1);
            
            if(statement != null) {
                statement.close();
            }

        } catch (SQLException e) {
            e.getMessage();
        }

        return element;
    }
    
    
    
    /**
     * Method Executes an SQL Query 
     * @param conn [Connection] to enable the establishing online connection to database
     * @param SQLquery [String] contains query in SQL format which interacts with database when executed
     * @return [ArrayList<String>]
     * @throws SQLException
     */
    public static ArrayList<String> requestDetails(Connection connection, String SQLquery) throws SQLException{
        
        ArrayList<String> details = new ArrayList<String>();

        try {

            Statement statement = connection.createStatement();
            ResultSet results = statement.executeQuery(SQLquery);

            do {
                details.add(results.getString(1));
            } while (results.next());

            if(statement != null) {
                statement.close();
            }

        } catch (SQLException e) {
            e.getMessage();
        }

        return details;
    }

    
    
    
    /**
     * 
     * @param connection [Connection] to enable the establishing online connection to database
     * @param movie [JSONObject] the specific movie JSON object to read and parse information from
     * @return [Boolean] indicating whether this movie has been successfully added to the database
     * @throws SQLException
     * @throws ParseException
     */
    public static void appendNormalThings(Connection connection) throws SQLException, ParseException {

        PreparedStatement statement = connection.prepareStatement("INSERT INTO NormalThings(int, blob) VALUES (?, ?)");
        
//        // Obtaining ... in order to look through the existing entries
//        ArrayList<String> tableRequest = requestDetails(connection, "SELECT int FROM NormalThings");
//
//        if ((!tableRequest.contains(...)) || (tableRequest.isEmpty()))
//        {                
        	int randomNum = ThreadLocalRandom.current().nextInt(0, dbSize + 1);
        	
        	// Generating Binary Blobs (all zero)
        	byte[] blob = new byte[blobSize];

        	// optionally
        	// new Random().nextBytes(blob);
        	
            statement.setInt(1, randomNum);  // Setting random integer
            statement.setBinaryStream(2, new ByteArrayInputStream(blob), blob.length);  // Setting binary Blob
            statement.executeUpdate();

//        }
        
        // Closing statement if not null
        if (statement != null){
            statement.close();  
        }

    }

    
    
    /**
     * Method calls sub-methods which populate the database table
     * @param conn [Connection] to enable the establishing online connection to database
     * @param movie [JSONObject] the specific movie JSON object to read and parse information from
     * @return [boolean] only true when all databases have been successfully populated
     * @throws SQLException
     * @throws JSONException
     * @throws ParseException
     */
    public static boolean populateDB(Connection conn) throws SQLException, ParseException {

        try {  
            appendNormalThings(conn);
            return true;

        } catch(SQLException e) {
            e.getMessage();
            return false;
        }
    }



    public static Connection conn = null;
    private static int dbSize = 0;
    private static int blobSize = 0;

    /**
     * MAIN METHOD
     */
    public static void main(String[] args) throws SQLException, IOException {
    	
    	// Deal with args exceptions (too little/many arguments than expected)
        if (args.length != 2) {
            System.err.println("Expected 2 arguments, but got: " + args.length);
	    System.err.println("Usage: java PopulateDB.java <Database Size> <Blob Size>");
	    System.exit(-1);
	}

        try {
            // Parse the string argument into an integer value.
            dbSize = Integer.parseInt(args[0]);
            blobSize = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("The arguments must both be an integers.");
            System.exit(1);
        };

        String fileName = "pmg.db";
        String url = "jdbc:sqlite:" + fileName;
        
        try {

            // Establishes online connection
            conn = DriverManager.getConnection(url);

            // Populate Database
            int i=0;
            for (; i < dbSize ; i++) {
                // If the database has been successfully populated for a movie, the method will return a 
                // boolean value, which will be added to a boolean list.   
                populateDB(conn);
            }

            if (i != dbSize+1) {
                System.out.println("Database Populated.");
            } else {System.out.println("Unable to Populate Database.");
		}


        } catch(SQLException e) {
            e.printStackTrace();
        } catch(ParseException e) {
            e.printStackTrace();

        }  finally {
            if (conn != null) {
                conn.close();
            }
        }    
    }
}
