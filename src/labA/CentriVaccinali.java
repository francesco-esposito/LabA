package labA;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;


// classe principale per la gestione dell'applicazione "Centri Vaccinali"
public class CentriVaccinali {
	
	// variabili globali condivise da tutti i metodi della classe che non possono modificarle
	final static Scanner tastiera = new Scanner(System.in);
	final static String errore = "Errore di inserimento. La invitiamo a riprovare.";
	final static String salvataggio = "***Informazioni registrate CORRETTAMENTE nel sistema***";
	final static String salvaErr = "***Si è verificato un ERRORE. Ricontrollare le istruzioni segnalate dal sistema.***";
	final static String separatore = ",";
	
	public void registraCentroVaccinale() throws IOException {
		
		CentroVacc centro = CentroVacc.InfoCentro(); 
		// controllo parametri univocità centro (per evitare duplicati: nome centro, nome via, civico)
		if ((!FileOperations.duplicatesCheckingMultipleLines("Centri_Vaccinali.dati.txt", centro.toString(), false))) {
			// memorizzazione del nuovo Centro una volta inseriti tutti i dati necessari
			if (FileOperations.storeInfoFromObject("Centri_Vaccinali.dati.txt", centro)) System.out.println(salvataggio + '\n');
			else System.out.println(salvaErr + '\n');
		} else System.out.println("Questo centro vaccinale e' già stato registrato!");
	}
	
	// *** PARTE DEDICATA ALLA GESTIONE DELLE STRUTTURE VACCINALI *** //
		
	// metodo per la conversione di una linea del file di testo in un oggetto CentroVacc
	// ogni oggetto rappresenta una riga nel file di testo
	public static CentroVacc parseCentroFromFile(String linea) {
		
		// vettore che contiene le informazioni memorizzate nel file in base alla 
		// posizione dei dati stessi
		final String infoCentro[] = { "nome", "tipologia", "comune", "qualificatore", 
				"nomevia", "civico", "provincia","CAP" }; 	
        
		HashMap<String,String> map = FileOperations.FetchObjectFromFile(linea, separatore, infoCentro);
		
		// creazione oggetto Indirizzo necessario per creare l'oggetto CentroVacc prelevendoli
		// dalla tabella hash
        Indirizzo indirizzo = new Indirizzo(
        	map.get("qualificatore"), map.get("nomevia"), map.get("civico"), 
        	map.get("comune"), map.get("provincia"), Integer.parseInt(map.get("CAP")));
       
        // creazione oggetto CentroVacc
        CentroVacc newCentro = new CentroVacc(map.get("nome"), map.get("tipologia"), indirizzo);
        return newCentro;
	}
	
	//********************************************************************************************************************
	
	// *** PARTE DEDICATA ALLA GESTIONE DEI VACCINATI *** //
	
	// metodo per la registrazione di un nuovo vaccinato
	// metodo statico: non vi è bisogno di creare un'istanza di tale classe per usarlo
	// evitato l'accesso concorrente usando un semaforo
	public synchronized void registraVaccinato() throws InterruptedException, IOException {
		System.out.println("\n***PRIMA di inserire il NOME della struttura, verificare con QUALE NOME \nessa"
				+ " sia stata REGISTRATA nel SISTEMA tramite gli APPOSITI COMANDI***");
		Vaccinato vacc = Vaccinato.InfoVaccinato();
		Random random = new Random(); Long tmp = System.currentTimeMillis();
		vacc.setVID( tmp.toString() + Integer.toString(random.nextInt(900) + 100)); Thread.sleep(100);
		
		if( !FileOperations.duplicatesCheckingSingleLine("Vaccinati_" + vacc.getNomeCentro().toLowerCase() + ".dati.txt", vacc.getCF(), 12, true)) {
			// memorizzazione del nuovo "Vaccinato" una volta inseriti tutti i dati necessari
			if (FileOperations.storeInfoFromObject("Vaccinati_" + vacc.getNomeCentro().toLowerCase() + ".dati.txt", vacc)) 
			{ System.out.println("\nIl suo ID univoco DI VACCINAZIONE e': " + vacc.getVID()); System.out.println('\n' + salvataggio + '\n'); }
			else System.out.println(salvaErr + '\n'); 
		
		} else System.out.println("\nQuesto vaccinato e' già stato registrato!");
	}
		
	public void menu() {
		System.out.println("\n***BENVENUTO NEL SISTEMA DI SEGNALAZIONE DI EVENTI AVVERSI CAUSATI DA VACCINAZIONE COVID-19***\n");
		System.out.println("ATTENZIONE: attenersi SCRUPOLOSAMENTE alle indicazioni riportate nel MANUALE UTENTE.");
		System.out.println("Ogni comando non valido causerà l'ARRESTO FORZATO del sistema.\n");
		System.out.println("Desidera accedere ai servizi offerti dal sistema in quanto...");
		System.out.println("1 - ... Operatore vaccinale"); System.out.println("2 - ... Cittadino"); System.out.print("Scelta : "); 
		try {
				Byte scelta = tastiera.nextByte();
				if (scelta == 1) {
					System.out.println(""); System.out.println("Che operazione intende effettuare?"); 
					System.out.println("1 - Registrazione di un nuovo centro vaccinale"); 
					System.out.println("2 - Registrazione di un nuovo vaccinato "); System.out.print("Scelta : "); 
					scelta = tastiera.nextByte();
						if (scelta == 1) registraCentroVaccinale(); else if (scelta == 2) registraVaccinato();
						else System.out.println(errore); }
				else if (scelta == 2) {
					System.out.println(""); System.out.println("Che operazione intende effettuare?");  
					System.out.println("1 - Cerca e visualizza informazioni centri vaccinali (LOGIN NON NECESSARIO)"); 
					System.out.println("2 - Registrazione presso un centro vaccinale"); 
					System.out.println("3 - Inserimento eventi avversi post vaccinazione (LOGIN NECESSARIO)");
					System.out.println("4 - Cerca centro vaccinale (LOGIN NON NECESSARIO)"); System.out.print("Scelta : ");
					scelta = tastiera.nextByte();
						if (scelta == 1) Cittadini.visualizzaInfoCentroVaccinale();
						else if (scelta == 2) Cittadini.registraCittadino();
						else if (scelta == 3) Cittadini.inserisciEventiAvversi();
						if (scelta == 4) Cittadini.cercaCentroVaccinale();
				}
				else System.out.println(errore);
			}	catch (Exception e) { System.out.println(errore); System.exit(0);};
	}
	
	public static void main(String[] args) throws IOException{
		CentriVaccinali applicazione = new CentriVaccinali(); boolean avanti = false;
		do {
			applicazione.menu();
			System.out.print("\nDesideri svolgere altre operazioni? (Inserire 'si' oppure 'no'): ");
			if (tastiera.next().equals("si")) avanti = true; else avanti = false;
		} while (avanti);
		System.out.println("\n********ARRESTO DEL SISTEMA...********");
	}
}
