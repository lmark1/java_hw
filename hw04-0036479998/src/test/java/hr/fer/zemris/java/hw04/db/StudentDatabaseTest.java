package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.StudentDatabase;
import hr.fer.zemris.java.hw04.db.StudentRecord;

@SuppressWarnings("javadoc")
public class StudentDatabaseTest {

	@Test(expected = IllegalArgumentException.class)
	public void nullDatabase() {
		new StudentDatabase(null);
	}

	@Test
	public void testForJMBAG() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4")
		};
		
		StudentDatabase newDB = new StudentDatabase(ocjene);
		
		StudentRecord getRecord = newDB.forJMBAG(new String("2"));
		assertEquals(getRecord, null);
		
		StudentRecord getRecord1 = newDB.forJMBAG(new String("0000000002"));
		assertEquals(getRecord1.getFirstName(), "Petra");
		
		StudentRecord getRecord2 = newDB.forJMBAG(new String("0000000015"));
		assertEquals(getRecord2.getLastName(), "Glavinić Pecotić");
		
		assertEquals(newDB.forJMBAG("00021"), null);
	}

	@Test
	public void testFilterRemoveAll() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		List<StudentRecord> newRecords = newDb.filter( (record) -> false );
		
		assertEquals(newRecords.size(), 0);
	}
	
	@Test
	public void testFilterRemoveNone() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		List<StudentRecord> newRecords = newDb.filter( (record) -> true );
		
		assertEquals(newRecords.size(), 5);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidDatabaseEntry() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2	Kifla"),
		};
		
		new StudentDatabase(ocjene);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidDatabaseEntry2() {
		String[] ocjene = new String[] { 
				new String("0000000001	ovo 	nijeDobro"),
		};
		
		new StudentDatabase(ocjene);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void gradeNotANumber() {
		String[] ocjene = new String[] { 
				new String("0000000001	isto	nijeDobro Keksi"),
		};
		
		new StudentDatabase(ocjene);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void spacesNotTabs() {
		String[] ocjene = new String[] { 
				new String("0000000001 isto nijeDobro 1"),
		};
		
		new StudentDatabase(ocjene);
	}
	
	@Test
	public void multipleNameInput() {
		String[] ocjene = new String[] { 
				new String("Keksi	Ovo bi trebao	biti valjan upis ja mislim	12345"),
		};
		
		StudentDatabase newDb = null;
		try{
			newDb = new StudentDatabase(ocjene);
		
		} catch (Exception e) {
			fail();
		}
		
		StudentRecord onlyRecord = newDb.forJMBAG("Keksi");

		assertEquals(onlyRecord.getLastName(), "Ovo bi trebao");
		assertEquals(onlyRecord.getFirstName(), "biti valjan upis ja mislim");
	}
	
}
