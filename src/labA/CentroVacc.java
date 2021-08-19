// classe ausiliaria che permette la creazione di oggetti che consentono di descrivere
// qualsiasi centro vaccinale memorizzandone le informazioni richieste
// ATTENZIONE: una volta creato l'oggetto che descrive una struttura vaccinale, i suoi dati NON possono
// essere modificati per motivi di sicurezza (quindi non vi sono metodi "setter")

package labA;
import java.util.Scanner;

// classe ausiliaria che permette la creazione di oggetti che consentono di descrivere
// qualsiasi centro vaccinale memorizzandone le informazioni richieste
// ATTENZIONE: una volta creato l'oggetto che descrive una struttura vaccinale, i suoi dati NON possono
// essere modificati per motivi di sicurezza (quindi non vi sono metodi "setter")

public class CentroVacc {
	private final static char separatore = ','; 
	private String name;
	private String tipologia;
	private Indirizzo indirizzo;
	
	// costruzione dell'oggetto solo con tutte le info necessarie
	// inizializzazione variabili di stato
	// costruttore con visibilità di package
	CentroVacc(String name, String tipologia, Indirizzo indirizzo){
		this.name = name;
		this.tipologia = tipologia;
		this.indirizzo = indirizzo;
	}
	
	// metodo per la creazione di un oggetto Centro vaccinale inserendo le info necessarie
	public static CentroVacc InfoCentro() {
		Scanner in = new Scanner(System.in);
		System.out.println("\n***ATTENZIONE: avvio procedura REGISTRAZIONE NUOVO CENTRO***");
		System.out.println("LEGGERE ATTENTAMENTE LE ISTRUZIONI RIPORTATE.");
		System.out.println("Inserire SOLO i dati RICHIESTI.\n");
		
		System.out.print("Nome struttura: "); String qualificatore = in.nextLine();
		
		System.out.print("Tipologia [ospedaliero/hub/aziendale]: "); String tipologia = in.nextLine(); System.out.println("");
		
		// costruzione di un nuovo oggetto tramite costruttore
		return new CentroVacc(qualificatore, tipologia, Indirizzo.inserisciInfo());
	}
	
	// metodi getter
	public String getNome() {return this.name;}
	public String getTipologia() {return this.tipologia;}
	public Indirizzo getIndirizzo() {return this.indirizzo;}
	
	// doppio metodo di conversione in una stringa
	// scelta effettuata per risparmiare codice
	// metodo per stampare le informazioni legate ad un certo centro vaccinale
	public String tostring() {
		return "***INFORMAZIONI STRUTTURA " + getNome().toUpperCase() + "***\n" + 
			   "Tipologia centro: " + getTipologia() + "\n" +
		       "Indirizzo centro: " + indirizzo.toString();
	}
	
	public String toString() {
		return getNome().toLowerCase() + separatore + getTipologia().toLowerCase() + separatore + 
			   getIndirizzo().getComune().toLowerCase() + separatore + getIndirizzo().getQualificatore().toLowerCase() 
				+ separatore + getIndirizzo().getNome().toLowerCase() + separatore + getIndirizzo().getCivico().toLowerCase() 
				+ separatore + getIndirizzo().getProvincia().toUpperCase() + separatore + getIndirizzo().getCAP();
	}
}

	
