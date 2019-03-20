package hr.fer.zemris.java.hw01;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Program računa površinu i opseg pravokutnika na temelju duljina stranica koje unosi korisnik. 
 * Duljine stranica mogu se unijeti kao argumenti prilikom pokretanja programa ili nakon pokretanja programa nakon zahtjeva za unos.
 * 
 *
 */

public class Rectangle {
	
	/**
	 * Glavna metoda izračunava opseg i površinu pravokutnika čiju duljinu stranica zadaje korisnik ili prilikom pokretanja programa ili 
 	 * nakon pokretanja preko naredbenog retka. U slučaju pokretanja programa s nedovoljno ili peviše argumenata program prekida s radom.
 	 * 
	 * @param args Argumenti koji predstavljaju duljine stranica pravokutnika, odvojeni razmakom
	 */
	public static void main(String[] args) {
			int argLength = args.length;
			double visina = 0, sirina = 0;
			
			//Ako je premalo / previse argumenata, prekinuti s programom
			if (argLength != 0 && argLength != 2) {
				System.out.print("Program je potrebno pozvati s 0 ili 2 argumenta!");
				System.exit(1);
			} else if(argLength == 2) {
				
				//Ako su 2 argumenta pokusaj dohvatiti duljinu i sirinu
				try{
					
					//Formatiranje radi univezalnosti unosa decimalnih brojeva sa točkom ili zarezom	
					sirina = Double.parseDouble(args[0].replaceAll(",", "."));
					visina = Double.parseDouble(args[1].replaceAll(",", "."));
					
					//Prekid ako su negativni
					if (sirina < 0 || visina < 0) {
						System.out.printf("Unijeli ste negativnu vrijednost.");
						System.exit(1);
					}
				} catch (NumberFormatException e) {
					System.out.print("Argumenti moraju biti brojevi!");
					System.exit(1);
				}
			} else {
				Scanner sc = new Scanner(System.in);
				
				//Unošenje duljine i širine
				sirina = ucitajStranicu(1, sc);
				visina = ucitajStranicu(2, sc);
				sc.close();
			}
			
			// Formatiranje na jedno decimalno mjesto
			DecimalFormat decimal = new DecimalFormat("0.0");
			
			System.out.printf("Pravokutnik širine %s i visine %s ima površinu %s te opseg %s.", decimal.format(sirina), decimal.format(visina), decimal.format(visina*sirina), decimal.format(visina+sirina));
	}
	
	/**
	 * Metoda koja služi za učitavanje stranice pravokutnika preko naredbenog retka. Stranica može biti cijeli ili decimalni broj te mora
	 * biti pozitivna. 
	 * Ako se pri unosu pojavila greška(unos sadrži više elemenata, unos nije broj, unos je negativan broj) tada program vraća pripadnu
	 * poruku te ponavlja zahtjev za unos.
	 * 
	 * @param n n = 1 ako želimo učitati širinu, n = 2 ako želimo učitati visinu
	 * @param scUnos Objekt tipa Scanner koji čita tokene iz System.in
	 * @return Metoda vraća broj tipa double koji predstavlja ili duljinu ili širinu stranice pravokutnika
	 */
	private static double ucitajStranicu(int n, Scanner scUnos) {
		String stranica = new String();
		
		// Odabir širine ili visine
		if (n == 1) {
			stranica = "širinu";
		} else if (n == 2) {
			stranica = "visinu";
		}
		
		int i = 0;
		
		while(true) {
			System.out.printf("Unesite %s > ", stranica);

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
				System.out.printf("Mora se unijeti samo jedan elemnt.%n");
			} else {
				//Pokušaj parsirati broj u tip double
				try{
					double broj = Double.parseDouble(unos.replaceAll(",", "."));
					if (broj >= 0) {
						return broj;
					} else {
						System.out.printf("Unijeli ste negativnu vrijednost.%n");
					}
				} catch(NumberFormatException ex) {
					System.out.printf("'%s' se ne može protumačiti kao broj.%n", unos);
				}
			}
		}
		
	}
}
