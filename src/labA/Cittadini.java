package labA;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

//classe principale per la gestione dell'applicazione "Cittadini"
public class Cittadini {
	//variabili globali condivise da tutti i metodi della classe che non possono modificarle
	final static Scanner tastiera = new Scanner(System.in);
	final static String errore = "Errore di inserimento. La invitiamo a riprovare.";
	final static String salvataggio = "***Informazioni registrate CORRETTAMENTE nel sistema***";
	final static String salvaErr = "***Si è verificato un ERRORE. Ricontrollare le istruzioni segnalate dal sistema.***";
	final static String separatore = ",";
	
	// metodo che permette la ricerca di una struttura vaccinale dato il suo nome (o una sua parte) oppure la tipologia e il comune
	// metodo per la lettura di tutti i centri vaccinali memorizzati nel file di testo indicato
	// il risultato restituito è una tabella hash in cui viene riportata una chiave numerica intera e il corrispondente
	// centro vaccinale sotto-forma di apposito oggetto
	// se "scelta" = TRUE, la tipologia di ricerca è in base al NOME di una struttura (una sua parte)
	// se "scelta" = FALSE, la tipologia di ricerca è in base al COMUNE e alla TIPOLOGIA di una struttura
	public static HashMap<Integer, CentroVacc> cercaCentroVaccinale() {	
			
		// tabella hash che contiene tutti i centri che sono compatibili con la ricerca effettuata dall'utente
		HashMap<Integer, CentroVacc> centriTrovati = new HashMap<>();
	
		String tipologia = "null"; String nome = "null";  String comune = "null"; int key = 0;

		try {
			
			System.out.println("\nChe tipologia di ricerca intendi effettuare? ");
			System.out.println("1 - Ricerca per nome "); System.out.println("2 - Ricerca per comune e tipologia ");
			System.out.print("Scelta: "); String scelta = tastiera.nextLine();
			
			// prelevamento dati in base alla tipologia di ricerca da effettuare
			if (scelta.equals("1")) { System.out.print("\nInserisci nome struttura da ricercare: "); nome = tastiera.nextLine(); }
			else if (scelta.equals("2"))
			{ System.out.print("\nInserisci comune struttura da ricercare: "); comune = tastiera.nextLine();
				System.out.print("Inserisci tipologia [ospedaliero/hub/aziendale]: "); tipologia = tastiera.nextLine(); }
			else System.out.print('\n' + errore);
			
			// apertura in sola lettura del file indicato
			BufferedReader br = new BufferedReader(new FileReader("data/CentriVaccinali.dati.txt"));
			
			// lettura della prima linea
			String linea = br.readLine();
			
			// scandisci tutto il file
			while ( linea != null ) {
				
				// ricerca per nome
				if (scelta.equals("1")) { 
					// se viene trovata una corrispondenza nel file in base ai dati inseriti dall'utente
					if (FileOperations.CheckInfoInFile(linea, separatore, nome.toLowerCase(), tipologia.toLowerCase(), false)) {
					// crea un apposito oggetto
					CentroVacc centro = CentriVaccinali.parseCentroFromFile(linea);
					// inseriscilo nella hash map per memorizzarlo
					centriTrovati.put(key++, centro); 
						}
					} 
				
				// ricerca per comune + tipologia
				else {
					// se viene trovata una corrispondenza nel file in base ai dati inseriti dall'utente
					if(FileOperations.CheckInfoInFile(linea, separatore, tipologia.toLowerCase(), comune.toLowerCase(), false)) {
						// crea un apposito oggetto
						CentroVacc centro = CentriVaccinali.parseCentroFromFile(linea);
						// inseriscilo nella hash map per memorizzarlo
						centriTrovati.put(key++, centro); }
					}	
				// lettura della prossima linea
				linea = br.readLine();
		}
		
			br.close();
			
			// conversione degli oggetti "CentroVacc" in un array per facilitare le operazioni
			// su di essi (vettore elementare ma conterrà sicuramente un numero limitato di elementi)
			Object[] cen = centriTrovati.values().toArray();  System.out.println("");
			
			// vettore non vuoto
			if (cen.length != 0) System.out.println("***Risultati della ricerca, strutture registrate nel sistema***");
			else                 { System.out.println("\n***La ricerca non ha prodotto risultati***"); return null; }
			
			// stampa risultati ricerca strutture per nome
			for (int i = 0; i < cen.length; i++) {
				System.out.println(i+1 + " - Struttura '" + ((CentroVacc) 
						cen[i]).getNome().toUpperCase() + "'");
			}
			// restituzione tabella hash con tutti i centri
			return centriTrovati;	
		} catch (Exception e){
			e.printStackTrace();
			System.out.print(salvaErr); return null; }
	}
	
	// metodo per la selezione di una struttura vaccinale (una volta ricercata e aver ottenuto
	// dei risultati validi)
	public static CentroVacc visualizzaInfoCentroVaccinale() {
		// riferimento hash map contenente i risultati della ricerca
		// effettutata per nome o comune + tipologia
		HashMap<Integer, CentroVacc> risultatoRicerca = cercaCentroVaccinale();
		
		if (risultatoRicerca != null) {
			// in base al valore inserito dall'utente ( che corrisponde alla chiave nella
			// hash map aumentata di uno ) prelevo l'oggetto che mi interessa
			System.out.println("\n***Inserisci il numero corrispondente al centro di interesse***");
			System.out.print("Scelgo il centro numero "); String nCentro = tastiera.nextLine(); System.out.println("");
			
			int c = Integer.parseInt(nCentro);
			
			// ottenimento del centro selezionato in base alla chiave nella tabella hash
			CentroVacc centro = (CentroVacc)(risultatoRicerca.get(--c)); System.out.println(centro.tostring());
			
			System.out.println("\n***PROSPETTO RIASSUNTIVO EVENTI AVVERSI " + centro.getNome().toUpperCase() + "***\n");
			System.out.println("Sintomo \t \t \t \t Numero segnalazioni \t \t   Severita' media");
			ArrayList<String> dati; float newSevMedia; int newNumSegn; String newReady = "";
			
			// vettore di stringhe costanti (gli spazi servono per dare un corretto "effetto grafico" tabellare)
			final String[] sintomi = {"Mal di testa               ", "Febbre                     ", 
					"Dolori muscolari/articolari", "Linfoadenopatia            ",
					                  "Tachicardia                ", "Crisi ipertensiva          "};
			
			try {
				// scansione del vettore lista contenente i sei dati
				for (int y = 1; y < sintomi.length + 1; y++) {
					// memorizzazione dei dati per ogni sintomo (nuovo separatore necessario per compiere tale operazione)
					// vettore sempre composto da due elementi --> numero segnalazioni (pos. 0) e severità media (pos. 1)
					// permette di prelevare in au array dotato sempre di due elementi, per ogni sintomo, occorrenza e severità
					dati = FileOperations.fetchFromTheBeginning("Vaccinati_"+ centro.getNome().toLowerCase() +".dati.txt", y);
					newNumSegn = (Integer.parseInt(dati.get(0))); newSevMedia = (Float.parseFloat(dati.get(1))); System.out.println();
					System.out.println(sintomi[y-1] + "\t \t \t  " + newNumSegn + "\t \t \t \t " + newSevMedia);
			}} catch (Exception e) {
				System.out.println("\n N.D." + "\t \t \t \t \t \t " + " 0" + "\t \t \t \t" + "  N.D.");
				System.out.println("\n***NON SONO ANCORA STATI SEGNALATI EVENTI AVVERSI PER QUESTO CENTRO***\n");
				return null;
			}
			
			// restituzione oggetto che rappresenta il centro cercato
			return (centro);
		} else return null;
	}
	
	// metodo che consente di memorizzare un nuovo cittadino all'interno di un file di testo
	// metodo per SINGOLO salvataggio (un solo utente)
	// Restituisce TRUE se l'operazione avviene correttamente
	// Restituisce False se avvengono delle eccezioni
	public synchronized static void registraCittadino() throws IOException {
		
		Cittadino citt = Cittadino.InfoCittadino();
				
		// controllo cittadini duplicati tramite user ID
		if( !FileOperations.duplicatesCheckingSingleLine("Cittadini_Registrati.dati.txt", citt.getUID(), 2, false)) {
			// memorizzazione del nuovo Centro una volta inseriti tutti i dati necessari
			if (FileOperations.storeInfoFromObject("Cittadini_Registrati.dati.txt", citt)) System.out.println('\n' + salvataggio + '\n');
			else System.out.println('\n' + salvaErr + '\n');
		} else System.out.println("\nQuesto nome utente non è disponibile. Riprova.");
	}
	
	// metodo che consente il login dell'utente una volta registrato
	// Se restituisce il VID --> login effettuato con successo
	// Se restituisce una stringa vuota --> login effettuato senza successo
	public static String loginCittadino() throws IOException {
		System.out.println("\n***Per poter accedere a questa funzionalità deve prima effettuare il login***");
		System.out.print("Inserisci il tuo nome utente: "); String myName = tastiera.nextLine();
		System.out.print("Inserisci la tua password: ");    String myPassword = tastiera.nextLine();
					
		// apertura in sola lettura del file indicato
		BufferedReader br = new BufferedReader(new FileReader("data/Cittadini_Registrati.dati.txt"));  String VIDTrovato = "";
		
		// lettura prima linea
		String linea = br.readLine();
		
		while ( linea != null ) {
			
			// cerca una corrispondenza di UID e password nella linea
			if (FileOperations.CheckInfoInFile(linea, separatore, myName, myPassword, true)) {
				// l'array sarà costituito da un solo elemento, il VID
				VIDTrovato = FileOperations.ricercaFile(linea, separatore, 1).get(0); break; }
			
			// lettura della prossima linea
			linea = br.readLine();
		}
		
		br.close(); return VIDTrovato;
	}
	
	// metodo che permette all'utente di inserire eventuali sintomi post-vaccinazione dopo
	// aver effettuato il login e dopo aver selezionato il centro in cui ha effettuato la vaccinazione
	public static void inserisciEventiAvversi() throws IOException {
		// vettore di stringhe costanti
		final String[] sintomi = {"Mal di testa", "Febbre", "Dolori muscolari e articolari", "Linfoadenopatia",
		                          "Tachicardia", "Crisi ipertensiva"};
		// 1 - Login utente
		String VIDCittadino = loginCittadino();
		
			if (VIDCittadino != "" ) {
				System.out.println("\n***Login effettuato CORRETTAMENTE***");
				
				// 2 - Selezione del centro (per nome o per comune + tipologia)
				// importante è l'oggetto che restituisce tale metodo poichè ci consente di posizionarci
				// nel file giusto per il prelevamento dei dati (per questo non è una procedura)
				CentroVacc centro = visualizzaInfoCentroVaccinale();
					
				// 3 - Inserisci info eventi avversi
				System.out.println("\n***Inserimento degli eventi avversi post-vaccinazione Covid-19***");
				System.out.println("Verranno proposti una serie di sintomi comuni.");
				System.out.println("Il sistema permette di indicare la severità degli stessi tramite un valore da 1 a 5.");
				System.out.println("Sarà inoltre possibile specificare eventuali dettagli aggiuntivi");
				System.out.println("\n***ATTENZIONE: una volta compilato il prospetto degli eventi avversi NON sara' "
						+ "possibile fare modifiche***"); System.out.println("[Premi 'invio' per confermare]\n"); tastiera.nextLine();
				
				String severitaMix = ""; String noteMix = ""; boolean[] opzionale = {false, false, false, false, false, false}; 
				String aus = "";
				
				// inserimento di tutti i sintomi
				for (int i = 0; i < sintomi.length; i++)
				{
						// leggi la severità riscontrata per ogni sintomo e aggiungila ad una stringa in cui
						// compariranno tutte le severità separate da una virgola
						System.out.print(sintomi[i] + " (se questo sintomo non si e' presentato digiti 'no'): "); 
						aus = tastiera.nextLine();
						if (!aus.equals("no")) opzionale[i] = true; else opzionale[i] = false;
						severitaMix += aus + separatore; 
						
						// leggi le note opzionali per ogni sintomo (se specificate) e aggiungile ad una stringa in cui
						// compariranno tutte le note opzionali separate da una virgola
						if(opzionale[i] == true) { 
						System.out.print("Note aggiuntive (opzionale, se non desidera specificare altro digiti 'no'): "); 
						noteMix += "," + tastiera.nextLine();} else { noteMix += ",no";}
				}
				
				StringBuffer inputBuffer = new StringBuffer(); ArrayList<String> dati; float newSevMedia; int newNumSegn; 
				String newReady = "";
		
				try {
				for (int i = 1; i < sintomi.length + 1; i++) {
					// permette di prelevare in au array dotato sempre di due elementi, per ogni sintomo, occorrenza e severità
					dati = FileOperations.fetchFromTheBeginning("Vaccinati_"+ centro.getNome().toLowerCase() +".dati.txt", i);
					// lettura della severità memorizzata nel file
					ArrayList<String> n = FileOperations.ricercaFile(severitaMix, separatore, i);
					
					// se è stato inserito un giudizio - incremento segnalazioni per quel sintomo
					if (opzionale[i-1]) newReady += Integer.toString(Integer.parseInt(dati.get(0)) + 1) + '-';
					else newReady += dati.get(0) + '-'; 
					
					// modifica severita' media per quel sintomo
					if (opzionale[i-1]) { 
						newSevMedia = (Float.parseFloat(dati.get(1)) + Float.parseFloat(n.get(i-1))) / 2;
						// solo due decimali
						String value = new BigDecimal(newSevMedia).setScale(1, RoundingMode.DOWN).toString(); newReady += value + ',';}
					else { newReady += dati.get(1) + ','; }
				}} catch(Exception e) {
					
				}
				
				// memorizziamo la nuova prima linea del file
				inputBuffer.append(newReady + "\r\n"); 
				
				// adesso occorre memorizzare i dati inseriti dall'utente nell'apposita linea (quella che acorrisponde appunto
				// all'utente stesso)
				FileReader fr = new FileReader("data/Vaccinati_"+ centro.getNome().toLowerCase() +".dati.txt");
				BufferedReader br = new BufferedReader(fr); boolean trovato = false; String linea = br.readLine(); linea = br.readLine();
				
				// fino a quando sono presenti delle linee nel file (e quindi ci sono oggetti da estrarre) 
				while (linea != null) {
					
					// se la linea è quella che corrisponde al cittadino
					if ((!trovato) && (FileOperations.CheckInfoInFile( linea, separatore, VIDCittadino, "7" ,true))) {
						// memorizzazione dei nuovi dati
						inputBuffer.append(severitaMix + linea.substring(12) + noteMix + "\r\n"); trovato = true;}
					else {
						// memorizza la linea senza cambiamenti
						inputBuffer.append(linea + "\r\n"); }
					
					// procedi alla prossima linea
					linea = br.readLine();
				}
					
				fr.close(); br.close();
				
				// scrittura su file di testo
				FileOutputStream fileOut = new FileOutputStream("data/Vaccinati_"+ centro.getNome().toLowerCase() +".dati.txt");
				fileOut.write(inputBuffer.toString().getBytes()); fileOut.close();   
				System.out.println("\n***Segnalazioni registrate CORRETTAMENTE nel sistema***");
			}
		else System.out.println("\n***Le credenziali inserite NON sono CORRETTE***"); System.out.println("");	
	}
}