package joinbase.withgrid;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class BDBManager {
	final private String path;
	private Environment myDbEnvironment = null;
	private Database myDatabase = null;
	private PatternBinding keyBinding = new PatternBinding();
	private InstanceBinding valueBinding = new InstanceBinding();

	public BDBManager(String path) {
		this.path = path;
	}

	public boolean createDatabase(String name) {

		System.out.println("Create directory...");
		File theDir = new File(path);

		if (theDir.exists()) {
			System.out.println("Directory " + path + " already exists!");
			return false;
		}
		boolean result = theDir.mkdir();
		if(result) {    
			System.out.println("Directory " + path + " created successfully!");  
		} 
		else {
			System.out.println("Fail to create directory " + path + " !");
			return false;
		}
		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(new File(path), envConfig);
			// Create the database.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					name, dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
			dbe.printStackTrace();
			System.out.println("Exception happens when opening database!");
		}
		return true;
	}

	public boolean openDatabase(String name) {

		File theDir = new File(path);
		if (!theDir.exists()) {
			System.out.println("Directory " + path + " doesn't exists!");
			return false;
		} else {
			System.out.println("Open directory " + path + "...");
		}

		try {
			// Open the environment. Create it if it does not already exist.
			EnvironmentConfig envConfig = new EnvironmentConfig();
			envConfig.setAllowCreate(true);
			myDbEnvironment = new Environment(new File(path), envConfig);
			// Create the database.
			DatabaseConfig dbConfig = new DatabaseConfig();
			dbConfig.setAllowCreate(true);
			myDatabase = myDbEnvironment.openDatabase(null,
					name, dbConfig);
		} catch (DatabaseException dbe) {
			// Exception handling goes here
			dbe.printStackTrace();
			System.out.println("Exception happens when opening database!");
		}
		return true;
	}

	public void readAll() {
		Cursor cursor = null;
		try {
			// Open the cursor.
			cursor = myDatabase.openCursor(null, null);
			// Get the DatabaseEntry objects that the cursor will use.
			DatabaseEntry foundKey = new DatabaseEntry();
			DatabaseEntry foundData = new DatabaseEntry();

			while (cursor.getPrev(foundKey, foundData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				Pattern theKey = keyBinding.entryToObject(foundKey);
				/*Instance theData = valueBinding.entryToObject(foundData);
				System.out.println("Key | Data : " + theKey + " | " +
						theData + "");*/
				System.out.println("Key : " + theKey );
			}
		}catch (DatabaseException de) {
			System.err.println("Error accessing database." + de);
		} finally {
			// Cursors must be closed.
			cursor.close();
		}
	}

	public boolean put(Pattern p, Instance ins) {
		try {
			DatabaseEntry theKey = new DatabaseEntry();
			keyBinding.objectToEntry(p, theKey);
			DatabaseEntry theData = new DatabaseEntry();
			valueBinding.objectToEntry(ins, theData);
			if(myDatabase.put(null, theKey, theData) == 
					OperationStatus.SUCCESS) {
				return true;
			} else {
				System.out.println("Fail to put Key:" + p + "into database");
				return false;
			}

		} catch (Exception e) {
			// Exception handling goes here
			e.printStackTrace();
			System.out.println("Exception happens when putting Key:" + p + "into database");
		}
		return false;
	}

	public Instance get(Pattern p) {
		Instance ins = null;

		try {
			// Create a pair of DatabaseEntry objects. theKey
			// is used to perform the search. theData is used
			// to store the data returned by the get() operation.
			DatabaseEntry theKey = new DatabaseEntry();
			keyBinding.objectToEntry(p, theKey);
			DatabaseEntry theData = new DatabaseEntry();
			// Perform the get.
			if (myDatabase.get(null, theKey, theData, LockMode.DEFAULT) ==
					OperationStatus.SUCCESS) {
				// Recreate the data String.
				ins = valueBinding.entryToObject(theData);
				//System.out.println("For key: '" + p + "' found.");
			} else {
				System.out.println("No record found for key '" + p + "'.");
			}
		} catch (Exception e) {
			// Exception handling goes here
			e.printStackTrace();
			System.out.println("Exception happens when getting the instance for key: " + p);
		}
		return ins;
	}

	public void closeDatabase() {
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
			System.out.println("Fail to close the database!");
		}
	}

	public static void main(String[] args) throws IOException {
		//redirect System.out to file
		File file = new File("out.txt");
		FileOutputStream fos = new FileOutputStream(file);
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		String dbPath = "g:/Research/Graduation Project/Database/real20000";
		BDBManager database = new BDBManager(dbPath);
		if(database.openDatabase("instanceDatabase") != true) {
			System.exit(0);
		}
		database.readAll();
		database.closeDatabase();
	}

}
