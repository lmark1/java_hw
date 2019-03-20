package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Program sadrži testne primjere za provjeravanje funkcije Factorial.racunanjeFaktorijela(int n).
 * Testiranje se vrši jedino provjerom računanja samih faktorijela jer metoda će jedino biti pozvana 
 * sa argumentom koji zadovolji postavljene uvjete. 
 * 
 *
 */
public class FactorialTest {

	/**
	 * Test provjerava izlaz metode double Factorial.racunanjeFaktorijela(int n)
	 * za n = 1;
	 */
	@Test
	public void testRacunanjeFaktorijela1() {
		long fakt1 = Factorial.racunanjeFaktorijela(1);
		
		assertEquals(fakt1, 1L);
	}
	
	/**
	 * Test provjerava izlaz metode double Factorial.racunanjeFaktorijela(int n)
	 * za n = 20;
	 */
	@Test
	public void testRacunanjeFaktorijela20() {
		long fakt20 = Factorial.racunanjeFaktorijela(20);
		long fakt20Check = 2432902008176640000L;
		
		assertEquals(fakt20, fakt20Check);
	}
	
	/**
	 * Test provjerava izlaz metode double Factorial.racunanjeFaktorijela(int n)
	 * za n = 10;
	 */
	@Test
	public void testRacunanjeFaktorijela10() {
		long fakt10 = Factorial.racunanjeFaktorijela(10);
		long fakt10Check = 3628800L;
		
		assertEquals(fakt10, fakt10Check);
	}
	

	/**
	 * Test provjerava izlaz metode double Factorial.racunanjeFaktorijela(int n)
	 * za n = 0; 
	 */
	@Test
	public void testRacunanjeFaktorijela0() {
		long fakt0 = Factorial.racunanjeFaktorijela(0);
		long fakt0Check = 1L;
		
		assertEquals(fakt0, fakt0Check);
	}
	
	/**
	 * Test provjerava izlaz metode double Factorial.racunanjeFaktorijela(int n)
	 * za n = -5; 
	 * 
	 */
	@Test
	public void testRacunanjeFaktorijelaNegativnogBroja() {
		long faktNeg = Factorial.racunanjeFaktorijela(-5);
		long faktNegCheck = -1L;
		
		assertEquals(faktNeg, faktNegCheck);
	}

}
