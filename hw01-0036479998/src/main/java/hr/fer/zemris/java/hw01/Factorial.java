package hr.fer.zemris.java.hw01;

import java.util.Scanner;

/**
 *Program računa faktorijel zadanog cijelog broja u rasponu od 1 do 20 kojeg korisnik upisuje preko tipkovnice nakon pokretanja programa.
 *
 */
public class Factorial {
	
	/**
	 * Glavna metoda programa traži upis broja te provjerava zadovoljava li broj unaprijed zadane uvjete.
	 * Ako je unesen cijeli broj u željenom rasponu od 1 do 20 metoda ispisuje faktorijel zadanog broja i traži ponovan unos, a u slučaju da korisnk ne zadovolji
	 * neki od uvjeta program ispisuje prikladnu poruku i traži ponovan unos. Za kraj rada potrebno je unijeti 'kraj'. 
	 * U slučaju unosa dva ili više elemenata bit će zatražen ponovan upis.
	 * 
	 * @param args Parametar glavne metode se u ovom slučaju ne koristi.
	 */
	public static void main(String[] args) {
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
			
			if (i > 1) {
				System.out.printf("Mora se unijeti samo jedan element.%n");
			} else {
				
				//Ako je samo jedan element, tada se parsira
				try{
					int broj = Integer.parseInt(unos);
					
					//Provjera uvjeta raspona unešenog cijelog broja
					if (broj >= 1 && broj <= 20) {
						long faktorijel = racunanjeFaktorijela(broj);
						System.out.printf("%d! = %d%n", broj, faktorijel);
					} else {
						System.out.printf("'%d' nije broj u dozvoljenom rasponu.%n", broj);
					}
				} catch(NumberFormatException ex) {
					
					//Prekinuti program ako je zatražen kraj
					if(unos.equals("kraj")) {
						System.out.print("Doviđenja");
						break;
					} else {
						System.out.printf("'%s' nije cijeli broj.%n", unos);
					}
				}
			}
		}
		
		scUnos.close();
	}
	
	/**
	 * Metoda računa faktorijel od zadanog cijelog broja n.
	 * Provjera raspona cijelog broja n vrši se prije poziva metode, sama metoda 
	 * jedino računa faktorijel. Ako je zadan broj kojim slučajem manji od nula metoda će vratiti -1.
	 * 
	 * @param n Broj čiji faktorijel se želi izračunati. Mora biti u rasponu od 1 do 20.
	 * @return Vraća faktorijel broja koji je predan kao argument, ako je manji od 0 tada metoda vraća -1.
	 */
	public static long racunanjeFaktorijela(int n) {
		long rezultat = 1;
		
		if (n < 0) {
			rezultat = -1;
		} else if(n == 0){
			rezultat = 1;
		} else {
			while (n >= 1) {
				rezultat *= n;
				n--;
			}
		}
		return rezultat;
	}

}
