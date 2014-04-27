package joinbase.withgrid.test;

import java.io.File;

import joinbase.withgrid.Pattern;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class TestBDBBinding {
	public static void main(String[] args) {
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
		//EnvironmentMutableConfig mutableConfig = myDbEnvironment.getMutableConfig();
		//System.out.println("je.maxMemoryPercent " +mutableConfig.getConfigParam("je.maxMemoryPercent"));
		
		
		
		
		
		TupleBinding<Pattern> keyBinding = new PatternBinding();
		TupleBinding<Instance> dataBinding = new InstanceBinding();
		Pattern aKey = new Pattern(new Integer[]{1,2,3,4});
		Instance aData = new Instance(aKey.length());
		aData.add(new int[]{2,5,10,15});
		aData.add(new int[]{1,2,6,9});
		aData.add(new int[]{1,2,10,15});
		aData.add(new int[]{1,2,3,5});
		
		
		try {
			DatabaseEntry theKey = new DatabaseEntry();
			keyBinding.objectToEntry(aKey, theKey);
			DatabaseEntry theData = new DatabaseEntry();
			dataBinding.objectToEntry(aData, theData);
			myDatabase.put(null, theKey, theData);
		} catch (Exception e) {
			// Exception handling goes here
		}
		
		try {
			// Create a pair of DatabaseEntry objects. theKey
			// is used to perform the search. theData is used
			// to store the data returned by the get() operation.
			DatabaseEntry theKey = new DatabaseEntry();
			keyBinding.objectToEntry(aKey, theKey);
			DatabaseEntry theData = new DatabaseEntry();
			// Perform the get.
			if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				// Recreate the data String.
				Instance foundData = dataBinding.entryToObject(theData);
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
