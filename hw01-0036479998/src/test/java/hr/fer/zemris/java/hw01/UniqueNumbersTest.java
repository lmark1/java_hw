package hr.fer.zemris.java.hw01;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Program sadrži testne primjere za provjeravanje funkcionalnosti slijedećih funkcija: 
 * <ul>
 * 	<li>UniqueNumbers.TreeNode UniqueNumebers.addNode(UniqueNumbers.TreeNode glava, int element);</li>
 * 	<li>int UniqueNumbers.treeSize(UniqueNumbers.TreeNode glava);</li>
 *  <li>boolean UniqueNumbers.containsValue(UniqueNumbers.TreeNode glava, int element);</li>
 * </ul>
 *
 */
public class UniqueNumbersTest {

	/**
	 * Provjerava vrijednost korijena binarnog stabla nakon višestrukog pokušaja dodavanja elemenata.
	 */
	@Test
	public void testProvjeraVrijednostiKorijena() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		assertEquals(glava.value, 42);
	}
	
	/**
	 * Provjerava vrijednost lijevog elementa od korijena nakon višestrukog pokušaja dodavanja elemenata.
	 */
	@Test
	public void testProvjeraVrijednostiLijevo() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		assertEquals(glava.left.value, 21);
	}
	
	/**
	 * Provjerava vrijednost lijevog elementa od korijena nakon višestrukog pokušaja dodavanja elemenata.
	 */
	@Test 
	public void testProvjeraVrijednostiDesno() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		assertEquals(glava.right.value, 76);
	}
	
	/**
	 * Provjerava vrijednost elementa glava.left.right nakon višestrukog pokušaja dodavanja elemenata.
	 */
	@Test
	public void testProvjeraVrijednostiLijevoDesno() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		assertEquals(glava.left.right.value, 35);
	}
	
	/**
	 * Provjerava brojanje čvorova u stablu.
	 */
	@Test
	public void testBrojanjeElemenata() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		
		int broj = UniqueNumbers.treeSize(glava);
		assertEquals(broj, 4);
	}
	
	/**
	 * Provjerava brojanje čvorova u praznom stablu.
	 */
	@Test
	public void testBrojanjeElemenataPraznoStablo() {
		UniqueNumbers.TreeNode glava = null;
		
		int broj = UniqueNumbers.treeSize(glava);
		assertEquals(broj, 0);
	}
	
	/**
	 * Provjerava postoji li element u stablu (kojeg stablo zaista sadrži).
	 */
	@Test
	public void testSadrziBroj() {
		UniqueNumbers.TreeNode glava = null;
		
		glava = UniqueNumbers.addNode(glava, 42);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 21);
		glava = UniqueNumbers.addNode(glava, 76);
		glava = UniqueNumbers.addNode(glava, 35);
		glava = UniqueNumbers.addNode(glava, 21);
		
		boolean check = UniqueNumbers.containsValue(glava, 35);
		assertEquals(check, true);
	}
	
	/**
	 * Provjerava postoji li element u stablu (kojeg stablo ne sadrži). 
	 */
	@Test
	public void testSadrziBrojPrazno() {
		UniqueNumbers.TreeNode glava = null;
			
		boolean check = UniqueNumbers.containsValue(glava, 35);
		assertEquals(check, false);
	}
}
