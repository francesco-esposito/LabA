package labA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// classe parametrica per gestire la memorizzazione e la lettura di oggetti di vario tipo su file 
public class FileOperations {
	
	// metodo che consente di memorizzare un le informazione già convertite all'interno del file di testo specificato
	// metodo per SINGOLO salvataggio (un solo oggetto)
	// Restituisce TRUE se l'operazione avviene correttamente
	// Restituisce False se avvengono delle eccezioni
	public static <T> boolean storeInfoFromObject(String pathFile , T object) {
		try {
				// apertura del file indicato per la memorizzazione dei nuovi dati senza
				// sovrascrivere quelli già esistenti (aggiunta in coda)
				FileWriter fw = new FileWriter("data/" + pathFile, true);
				// scrittura con buffer (prestazioni migliori)	
				BufferedWriter bw = new BufferedWriter(fw);
				
				// memorizzazione info oggetto 
				bw.write(object.toString() + "\r\n");
				
				// svuotamento forzato del buffer e chiusura degli oggetti utilizzati (deallocamento memoria)
				bw.flush();  fw.close(); bw.close(); return true;
			}
		
		catch(Exception e) {
				e.printStackTrace(); return false;
			}
	}

	// metodo che restituisce le informazioni memorizzate in una linea di un file di testo
	// sotto-forma di tabella hash (l'obiettivo è quello di ricostruire l'oggetto memorizzato) 
	public static HashMap<String, String> FetchObjectFromFile(String linea, String separatore, String[] info) {
		
		// utilizzato per "scannerizzare la riga del file di testo"
		Scanner scanner = new Scanner(linea);   scanner.useDelimiter(separatore);
		
		// tabella hash che ha come chiave il nome del parametro e come dato associato
		// il suo rispettivo valore
		HashMap<String,String> dati = new HashMap<String, String>();
		
			for(byte i = 0; i < info.length; i++) {
				// aggiungili alla tabella hash
				dati.put(info[i], scanner.next()); }		
		return dati;
	}
	
	// metodo utilizzare per verificare se in una determinata linea di file 
	// esistono delle corrispondenze in base a due parametri esterni (MAX DUE PARAMETRI DI CONFRONTO)
	public static boolean CheckInfoInFile(String linea, String separatore, String valore1, String valore2, boolean mod){

			Scanner input = new Scanner(linea);  input.useDelimiter(separatore); String parametro1 = ""; String parametro2 = "";
			
			// se il valore del secondo parametro è una stringa vuota significa che la ricerca è solo in base al
			// al primo parametro
			if(valore2.equals("null")) {  
				
				// lettura del nome della struttura (sappiamo essere meorizzato in prima posizione)
				if (input.hasNext()) parametro1 = input.next();
				
				//System.out.println(parametro1 + "  " +valore1);
				
				// se esiste un match tra i due valori in modalità parziale (mod = false)
				if (!mod) return matchString(valore1,parametro1) ? true : false;
				else return parametro1.equals(valore1) ? true : false;
			}
			
			// in questo caso significa che stiamo cercando il VID, quindi in questo caso non viene passato
			// il valore due ma la posizione del valore 1 sotto-forma di stringa nel valore 2
			else if (valore2.equals("7")) {
				for (int i = 0; i < Integer.parseInt(valore2); i++) parametro1 = input.next();
				return parametro1.equals(valore1) ? true : false;
			}
			
			// ricerca in base a due parametri
			else {
				
				// comune e tipologia si trovano memorizzati nella linea nella seconda e terza posizone
				if (input.hasNext()) { input.next();  parametro1 = input.next(); parametro2 = input.next(); }
				return ((parametro1.equals(valore1) && (parametro2.equals(valore2)) ? true : false)); 
			}
	}

	// metodo che consente di prelevare i dati da una linea del file di testo specificando quali
	// informazioni sono quelle importanti 
	public static ArrayList<String> ricercaFile(String linea, String separatore, int volte) {
		
		// utilizzato per "scannerizzare la riga del file di testo"
		Scanner input = new Scanner(linea);  input.useDelimiter(separatore); ArrayList<String> dati = new ArrayList<String> ();
		for (int i = 0; i < volte; i++) {
			// aggiunta dati alla lista (conterrà comunque un numero limitato di elementi)
			if(input.hasNext())
				dati.add(input.next()); 
			else break;}
		return dati;
	}

	// metodo per il prelevamento degli eventi avversi legati ad un determinato centro dalla prima linea
	public static ArrayList<String> fetchFromTheBeginning (String filePath, int nInfo) throws IOException {
		
		FileReader fr = new FileReader("data/" + filePath);
		BufferedReader br = new BufferedReader(fr);  String linea = br.readLine();
		
		// prelevamento dei dati riguardanti le segnalazioni di eventi avversi per ogni centro
		// in ogni file, la prima linea contiene tutti i dati "generali" legati al centro
		ArrayList<String> dati = FileOperations.ricercaFile(linea, ",", 6);
		
		// in ogni dato presente in una linea, effettua un ulteriore sotto-scansione per determinare il numero di segnalazioni
		// per quel sintomo e la severità media associata
		// salva questi dati in un altro array (numero di dati limitato, facile l'uso di un array)
		ArrayList<String> ndati = new ArrayList<String>();
		
		// ogni volta che l'utente specifica i sintomi e la severità per la sua esperienza vaccinale,
		// tutti i dati si aggiornano automaticamente
		
		// scansione del vettore lista contenente i sei dati
		for (int y = 0; y < nInfo; y++) {
			
			// memorizzazione dei dati per ogni sintomo (nuovo separatore necessario per compiere tale operazione)
			// vettore sempre composto da due elementi --> numero segnalazioni (pos. 0) e severità media (pos. 1)
			ndati = FileOperations.ricercaFile(dati.get(y), "-" , 2);
		}
		
		return ndati;
	}

	// metodo che consente di stabilire se una frase (più stringhe) si trova all'interno di un'altra 
	// str2 --> frase da verificare che sia contenuta nell'altra (o anche solo una sua parte ad esclusione delle
	// parole più comuni che renderebbero tale ricerca insignificante)
	public static boolean matchString(String str1, String str2) {
		
		// non si accettano valori nulli
		if ((str1 == null) || (str2 == null)) {return false;}
		
		// crea un array in cui ogni parola della frase è considerata singolarmente
			String[] str3 = str2.split(" ");
			for (int i = 0; i < str3.length; i++) {
				//scartiamo le parole comuni più utilizzati per rendere più "realistica" la ricerca
				if (!str3[i].equals("centro")&&(!str3[i].equals("vaccinale")&&(!str3[i].equals("di")))) 
					if (str1.contains(str3[i])) return true; 
			}
			return false;
		}

	// controllo se vi sono duplicati all'interno memorizzati all'interno di un determinato file
	// valore su unica linea
	public static boolean duplicatesCheckingSingleLine(String filePath, String info, int pos, boolean skip){
			
		try {
			// apertura in sola lettura del file indicato
			BufferedReader br = new BufferedReader(new FileReader("data/"+ filePath));
			// lettura della prima linea
			String linea = br.readLine();  if (skip) linea = br.readLine();
			// scandisci tutto il file
			while ( linea != null ) {
				ArrayList<String> list = FileOperations.ricercaFile(linea, ",", pos);
				if ( info.equals(list.get(pos-1)) ) return true;
				linea = br.readLine();
			}
			br.close(); return false;}
		catch (Exception e) {
			return false;
		}
	}
	
	// controllo se vi sono duplicati all'interno memorizzati all'interno di un determinato file
	// piu' valori su un'unica linea
	public static boolean duplicatesCheckingMultipleLines(String filePath, String info, boolean skip) {
		
		try {
			// apertura in sola lettura del file indicato
			BufferedReader br = new BufferedReader(new FileReader("data/"+ filePath));
			// lettura della prima linea
			String linea = br.readLine();  if (skip) linea = br.readLine();
			// scandisci tutto il file
			while ( linea != null ) {
				if ( info.equals(linea) ) return true;
				linea = br.readLine();
			}
			br.close(); return false; }
		catch (Exception e) {
			return false;
		}	
	}
	
}
