package test.bdb;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.EnvironmentMutableConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import java.io.File;

public class SimpleExample {
	public static void main(String[] argc) {
		// Open the environment. Allow it to be created if it does not
		// already exist.
		Environment myDbEnvironment = null;
		Database myDatabase = null;

		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(new File("G:/Research/Berkeley DB/export/dbEnv"),
					envConfig);
			// Open the database. Create it if it does not already exist.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					"sampleDatabase",
					dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
		}
		EnvironmentMutableConfig mutableConfig = myDbEnvironment.getMutableConfig();
	    System.out.println("je.maxMemoryPercent " +mutableConfig.getConfigParam("je.maxMemoryPercent"));

		String aKey = "FirstKey";
		/*String aData = "myFirstData";
		try {
			DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry(aData.getBytes("UTF-8"));
			myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
			// Exception handling goes here
		}*/


		try {
			// Create a pair of DatabaseEntry objects. theKey
			// is used to perform the search. theData is used
			// to store the data returned by the get() operation.
			DatabaseEntry theKey = new DatabaseEntry(aKey.getBytes("UTF-8"));
			DatabaseEntry theData = new DatabaseEntry();
			// Perform the get.
			if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				// Recreate the data String.
				byte[] retData = theData.getData();
				String foundData = new String(retData, "UTF-8");
				System.out.println("For key: '" + aKey + "' found data: '" +
						foundData + "'.");
			} else {
				System.out.println("No record found for key '" + aKey + "'.");
			}
		} catch (Exception e) {
			// Exception handling goes here
		}

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
}
