package hr.fer.zemris.java.hw01;

import java.util.Scanner;


/**
 * Program realizira binarno stablo. Posjeduje funkcionalnosti dodavanja elemenata, provjeravanje velicine stabla te provjeravanje
 * postoji li određeni elementi u stablu. Prilikom završetka rada biti će ispisani elementi stabla koje je korisnik dodao 
 * najprije od najmanjeg do najveceg, a zatim obrnuto.
 *  
 */

public class UniqueNumbers {
	
	/**
	 * Struktura kojom se definiraju čvorovi u binarnom stablu. Sastoji se od vrijednosti čvora tipa int te 
	 * reference na lijevi i desni list čvora tipa TreeNode.
	 *
	 */
	static class TreeNode{
		/**
		 * TreeNode left - označava referencu na lijevi čvor
		 */
		TreeNode left;
		/**
		 * TreeNode right - označava referencu na desni čvor
		 */
		TreeNode right;
		/**
		 * int value - označava vrijednost spremljenu u čvoru
		 */
		int value;
	}
	
	/**
	 * Metoda kojom se dodaje element u binarno stablo. Ako element već postoji, tada se neće ponovno dodavati te će se 
	 * ispisati pripadna poruka.Elementi koji ne postoje u stablu dodaju se na principu lijevo manji, a desno veći.
	 * 
	 * @param glava Referenca na korijen stabla, tipa TreeNode.
	 * @param element Element tipa int koji korisnik želi dodati u stablo.
	 * @return Referenca na korijen stabla u kojem je dodan željeni element.
	 */
	public static TreeNode addNode(TreeNode glava, int element) {
		
		if (glava == null) {
			TreeNode novi = new TreeNode();
			novi.value = element;
			novi.left = null;
			novi.right = null;
			glava = novi;
		} else if (element < glava.value) {
			glava.left = addNode(glava.left, element);
		} else if (element > glava.value) {
			glava.right = addNode(glava.right, element);
		}
		//Ako element već postoji tada se ne dodaje u stablo
		
		return glava;
	}
	
	/**
	 * Metoda koja broji koliko postoji čvorova u stablu.
	 * 
	 * @param glava Referenca na korijen stabla tipa TreeNode.
	 * @return Broj čvorova u stablu tipa int.
	 */
	public static int treeSize(TreeNode glava) {
		
		if (glava == null) {
			return 0;
		} else {
			return 1 + treeSize(glava.left) + treeSize(glava.right);
		}
	}
	
	/**
	 * Metoda koja provjerava postoji li zadani element u binarnom stablu.
	 * 
	 * @param glava Referenca na korijen stabla tipa TreeNode.
	 * @param element Element koji želimo provjeriti postoji li u stablu.
	 * @return Metoda vraća true ako element postoji u stablu, a false ako ne postoji.
	 */
	public static boolean containsValue(TreeNode glava, int element) {
		
		if (glava == null) {
			return false;
		} else if (element < glava.value) {
			return containsValue(glava.left, element);
		} else if (element > glava.value) {
			return containsValue(glava.right, element);
		} else {
			return true;
		}
	}
	
	/**
	 * Metoda ispisuje elemente stabla po redu, od najmanjeg do najveceg. Prvo ispisuje lijevi, zatim srednji(korijen), pa desni.
	 *
	 * @param glava Referenca na binarno stablo, tipa TreeNode.
	 */
	private static void ispisiOdNajmanjeg(TreeNode glava) {
		if(glava == null) {
			return;
		} else {
			ispisiOdNajmanjeg(glava.left);
			System.out.printf("%d ", glava.value);
			ispisiOdNajmanjeg(glava.right);
		}
	}
	
	/**
	 * Metoda ispisuje elemente stabla od najveceg do najmanjeg. Prvo ispisuje desni, zatim srednji(korijen), pa lijevi.
	 *
	 * @param glava Referenca na binarno stablo, tipa TreeNode.
	 */
	private static void ispisiOdNajveceg(TreeNode glava) {
		if(glava == null) {
			return;
		} else {
			ispisiOdNajveceg(glava.right);
			System.out.printf("%d ", glava.value);
			ispisiOdNajveceg(glava.left);
		}
	}
	
	/**
	 * Glavna metoda zahtijeva od korisnika upis željenih elemenata liste. Rad se prekida upisom riječi 'kraj'. 
	 * Nakon prekida rada ispisuje se sadržaj binarnog stabla najprije od najmanjeg do najvećeg, a zatim od najvećeg do najmanjeg.
	 * 
	 * @param args Argumenti glavne funkciju u ovom se slučaju ne koriste.
	 */
	public static void main(String[] args) {
		TreeNode glava = null;
		Scanner scUnos = new Scanner(System.in);
		int i = 0;
		
		while(true) {
			System.out.print("Unesite broj > ");			
			
			//Čitaj linije sve dok se ne učita jedan ili više elemenata
			String unos = new String();
			int brojRazmaka = 0;
			do{
				unos = scUnos.nextLine();
				brojRazmaka = (unos.length() - unos.replaceAll(" ", "").length());
			} while (brojRazmaka == unos.length());
			
			//Provjera ima li više elemenata u unosu
			Scanner scString = new Scanner(unos);
			i = 0;
			while(scString.hasNext()) {
				unos = scString.next();
				i++;
			}
			scString.close();
			
			if(i > 1) {
				System.out.printf("Mora se unijeti samo jedan element.%n");
			} else {
				
				//Ako je samo jedan element, pokuša se pretvoriti u int
				try {
					int element = Integer.parseInt(unos);
					
					if(!containsValue(glava, element)) {
						glava = addNode(glava, element);
						System.out.printf("Dodano.%n");
					} else {
						System.out.printf("Broj već postoji. Preskačem.%n");
					}
				} catch (NumberFormatException ex) {
					
					// Ako je zatraženo, potrebno je prekinuti program
					if (unos.equals("kraj")){
						break;
					} else {
						System.out.printf("'%s' nije cijeli broj.%n", unos);
					}
				}
			}
		}
		
		System.out.print("Ispis od najmanjeg: ");
		ispisiOdNajmanjeg(glava);
		System.out.printf("%nIspis od najveceg: ");
		ispisiOdNajveceg(glava);
		
		scUnos.close();	
	}
}
