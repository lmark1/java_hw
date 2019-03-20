package hr.fer.zemris.java.hw04.db;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import hr.fer.zemris.java.hw04.db.QueryFilter;
import hr.fer.zemris.java.hw04.db.QueryParser;
import hr.fer.zemris.java.hw04.db.StudentDatabase;
import hr.fer.zemris.java.hw04.db.StudentRecord;

@SuppressWarnings("javadoc")
public class QueryFilterTest {

	@Test
	public void testQueryFilterOneMatch() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		QueryParser parser = new QueryParser(" jmbag >= \"0000000002\" AND firstName LIKE \"P*\"");
		List<StudentRecord> filteredRecords = newDb.filter(new QueryFilter(parser.getQuery()));
		
		assertEquals(filteredRecords.size(), 1);
		assertEquals(filteredRecords.get(0).getFirstName(), "Petra");
	}

	@Test
	public void testQueryFilterFewMatches() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		QueryParser parser = new QueryParser(" jmbag < \"0000000050\" AND lastName LIKE \"*ić\"");
		List<StudentRecord> filteredRecords = newDb.filter(new QueryFilter(parser.getQuery()));
		
		assertEquals(filteredRecords.size(), 4);
		assertEquals(filteredRecords.get(0).getFirstName(), "Marin");
		assertEquals(filteredRecords.get(3).getFirstName(), "Nenad");
	}
	
	@Test
	public void testQueryFilterNoMatches() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		QueryParser parser = new QueryParser(" jmbag < \"0000000050\" AND lastName LIKE \"*ić\" and jmbag = \"1000000\" ");
		List<StudentRecord> filteredRecords = newDb.filter(new QueryFilter(parser.getQuery()));
		
		assertEquals(filteredRecords.size(), 0);
	}
	
	@Test
	public void testDirectQuery() {
		String[] ocjene = new String[] { 
				new String("0000000001	Akšamović	Marin	2"),
				new String("0000000002	Bakamović	Petra	3"),
				new String("0000000015	Glavinić Pecotić	Kristijan	4"),
				new String("0000000028	Kosanović	Nenad	5"),
				new String("0000000039	Martinec	Jelena	4")
		};
		
		StudentDatabase newDb = new StudentDatabase(ocjene);
		QueryParser parser = new QueryParser("jmbag = \"0000000015\"");
		
		if(parser.isDirectQuery()) {
			StudentRecord r = newDb.forJMBAG(parser.getQueriedJMBAG());
			assertEquals(r.getFirstName(), "Kristijan");
			assertEquals(r.getLastName(), "Glavinić Pecotić");
			return;
		}
		fail();
	}
	
}
