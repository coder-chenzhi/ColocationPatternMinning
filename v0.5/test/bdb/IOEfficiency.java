package test.bdb;

import java.io.File;
import java.util.HashMap;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class IOEfficiency {
	
	static int MEGABYTES = 1024 * 1024;
	
	static String PREFIX = "G:/Research/Berkeley DB/IOEfficiency/";

	public static void testBDBWrite(int testSize) {
		//create directory
		System.out.println("Create directory...");
		String path = PREFIX + testSize;
		File theDir = new File(path);
		int dup = 0;

		while(theDir.exists()) {
			dup++;
			path = PREFIX + testSize + "_" + dup;
			theDir = new File(path);
		}
		boolean result = theDir.mkdir();
		if(result) {    
			System.out.println("Directory " + path + " created successfully!");  
		} 
		else {
			System.out.println("Fail to create directory " + path + " !");
			System.exit(1);
		}

		// Open the environment. Allow it to be created if it does not
		// already exist.
		Environment myDbEnvironment = null;
		Database myDatabase = null;

		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(theDir, envConfig);
			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					"sampleDatabase",
					dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
		//iterativelly write data
		long start = System.currentTimeMillis();
		for(int i = 0; i < testSize; i++) {
			String aKey = "" + i;
			String aData = "" + i;
			try {
				DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
				DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
				myDatabase.put(null, theKey, theData);
			} catch (Exception e) {
				// Exception handling goes here
			}
		}
		long freeMemory = Runtime.getRuntime().freeMemory() / MEGABYTES;
		long totalMemory = Runtime.getRuntime().totalMemory() / MEGABYTES;
		
		System.out.println("Write " + testSize + " String/String pair with " + 
					(System.currentTimeMillis() - start) / (double)1000 + "sec and " +
								(totalMemory - freeMemory) + "MB.");		
		
		try {
			if (myDatabase != null) {
				myDatabase.close();
			}

			if (myDbEnvironment != null) {
				myDbEnvironment.cleanLog(); // Clean the log before closing
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
	}
	
	public static void testBDBRead(int testSize) {
		//create directory
		System.out.println("Create directory...");
		String path = PREFIX + testSize;
		File theDir = new File(path);
	
		if(theDir.exists()) {    
			System.out.println("Directory " + path + " found!");  
		} 
		else {
			System.out.println("Can not found directory " + path + " !");
			System.exit(1);
		}

		// Open the environment. Allow it to be created if it does not
		// already exist.
		Environment myDbEnvironment = null;
		Database myDatabase = null;

		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(theDir, envConfig);
			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					"sampleDatabase",
					dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
		//iterativelly read data
		long start = System.currentTimeMillis();
		for(int i = 0; i < testSize; i++) {
			String aKey = "" + i;
			try {
				// Create a pair of DatabaseEntry objects. theKey
				// is used to perform the search. theData is used
				// to store the data returned by the get() operation.
				DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
				DatabaseEntry theData = new DatabaseEntry();
				// Perform the get.
				myDatabase.get(null, theKey, theData, LockMode.DEFAULT);
				/*if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
						OperationStatus.SUCCESS) {
					// Recreate the data String.
					byte[] retData = theData.getData();
					String foundData = new String(retData, "UTF-8");
					System.out.println("For key: '" + aKey + "' found data: '" +
							foundData + "'.");
				} else {
					System.out.println("No record found for key '" + aKey + "'.");
				}*/
			} catch (Exception e) {
				// Exception handling goes here
			}
		}
		long freeMemory = Runtime.getRuntime().freeMemory() / MEGABYTES;
		long totalMemory = Runtime.getRuntime().totalMemory() / MEGABYTES;
		
		System.out.println("Write " + testSize + " String/String pair with " + 
					(System.currentTimeMillis() - start) / (double)1000 + "sec and " +
								(totalMemory - freeMemory) + "MB.");		
		
		try {
			if (myDatabase != null) {
				myDatabase.close();
			}

			if (myDbEnvironment != null) {
				myDbEnvironment.cleanLog(); // Clean the log before closing
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
	}
	
	public static void testBDBWriteAndRead(int testSize) {
		//create directory
		System.out.println("Create directory...");
		String path = PREFIX + testSize;
		File theDir = new File(path);
		int dup = 0;

		while(theDir.exists()) {
			dup++;
			path = PREFIX + testSize + "_" + dup;
			theDir = new File(path);
		}
		boolean result = theDir.mkdir();
		if(result) {    
			System.out.println("Directory created successfully!");  
		}

		// Open the environment. Allow it to be created if it does not
		// already exist.
		Environment myDbEnvironment = null;
		Database myDatabase = null;

		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(theDir, envConfig);
			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					"sampleDatabase",
					dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
		//iterativelly write data
		long start = System.currentTimeMillis();
		for(int i = 0; i < testSize; i++) {
			String aKey = "" + i;
			String aData = "" + i;
			try {
				DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
				DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
				myDatabase.put(null, theKey, theData);
			} catch (Exception e) {
				// Exception handling goes here
			}
		}
		System.out.println("Write " + testSize + " String/String pair with " + 
					(System.currentTimeMillis() - start) / (double)1000 + "sec");
		
		try {
			if (myDatabase != null) {
				myDatabase.close();
			}

			if (myDbEnvironment != null) {
				myDbEnvironment.cleanLog(); // Clean the log before closing
				myDbEnvironment.close();
			}
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		
	}

	public static void testHashMapPut(int testSize) {
		HashMap<String, String> map = new HashMap<String, String>();
		long start = System.currentTimeMillis();
		for(int i = 0; i < testSize; i++) {
			String aKey = "" + i;
			String aData = "" + i;
			map.put(aKey, aData);
		}
		long freeMemory = Runtime.getRuntime().freeMemory() / MEGABYTES;
		long totalMemory = Runtime.getRuntime().totalMemory() / MEGABYTES;
		
		System.out.println("Write " + testSize + " String/String pair with " + 
					(System.currentTimeMillis() - start) / (double)1000 + "sec and " +
								(totalMemory - freeMemory) + "MB.");
	}
	
	public static void main(String[] args) {
		int testSize = 10000000;
		testBDBRead(testSize);
	}
}
