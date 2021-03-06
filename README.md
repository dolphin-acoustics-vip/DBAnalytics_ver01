# DBAnalytics
 
Code Runs Tests on Database (DB) Appending speeds for various DB and Binary Blob Sizes.

To RUN:
 Open project on Eclipse.
 Configure arguments for InitializeDB.java to indicate 
  “<FileName.db>”
 , and for PopulateDB.java 
  “<FileName.db> <# of Records> <Binary Blob Size in Bytes> <Binary Blob Type (either 0 (Zeros) or 1 (Random))>”

 Run in order InitializeDB > PopulateDB > DataAnlysis. Each process will print out a message indicating completion (ex: "Database Initialized", Database Populated").
 
Caution: Please do not run PopulateDB twice. You must first initialize the database through InitializeDB then repopulate it through PopulatedDB. Not doing so will  produce an unclean database which will obscure data analysis.

Note: You will find that some databases (large ones ex: 10,000 records with 10 kByte Blobs) take a few minutes (~3 min) to populate, but the message indicates that it took less (ex: 00:01:20.875). The total Write Time (WT) is accurate, but the program goes back to update the table with calculations of specific WTs, which results in twice the time of computing.
