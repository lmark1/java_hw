package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.FieldValueGetters;
import hr.fer.zemris.java.hw04.db.StudentRecord;

@SuppressWarnings("javadoc")
public class FieldValueGetterTest {

	@Test(expected = IllegalArgumentException.class)
	public void getNullRecordName() {
		FieldValueGetters.FIRST_NAME.get(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getNullRecordLastName() {
		FieldValueGetters.LAST_NAME.get(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void getNullRecordJMBAG() {
		FieldValueGetters.JMBAG.get(null);
	}
	
	@Test
	public void getRecordAllRecordValues() {
		StudentRecord newRecord = new StudentRecord("0004", "Markovic", "Marko", 5);
		
		assertEquals(FieldValueGetters.FIRST_NAME.get(newRecord), "Marko");
		assertEquals(FieldValueGetters.LAST_NAME.get(newRecord), "Markovic");
		assertEquals(FieldValueGetters.JMBAG.get(newRecord), "0004");
		assertNotEquals(FieldValueGetters.JMBAG.get(newRecord), "4");	
	}
	
}
