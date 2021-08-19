package labA;
import java.util.Scanner;

//classe ausiliaria che permette la creazione di oggetti che consentono di descrivere
//un cittadino memorizzandone le informazioni richieste
//ATTENZIONE: una volta creato l'oggetto che descrive un vaccinato, i suoi dati NON possono
//essere modificati per motivi di sicurezza (quindi non vi sono metodi "setter")

public class Cittadino {
	private String nome;
	private String cognome;
	private String cf;
	private String mail;
	private String UID;
	private String password;
	private String VID;
	
	// costruzione dell'oggetto solo con tutte le info necessarie
	// inizializzazione variabili di stato
	// costruttore con visibilità di package
	Cittadino(String nome, String cognome, String cf,
			String UID, String password, String VID, String mail){
		this.nome = nome;
		this.cognome = cognome;
		this.cf = cf;
		this.mail = mail;
		this.UID = UID;
		this.password = password;
		this.VID = VID;
	}
	
	// metodo per la creazione di un oggetto "Cittadino" inserendo le info necessarie
	public static Cittadino InfoCittadino() {
		Scanner in = new Scanner(System.in);
		
		System.out.println("\n***ATTENZIONE: avvio procedura REGISTRAZIONE NUOVO CITTADINO***");
		System.out.println("LEGGERE ATTENTAMENTE LE ISTRUZIONI RIPORTATE");
		System.out.println("Inserire SOLO i dati RICHIESTI");
		
		System.out.print("Nome: "); String nome = in.nextLine();
		
		System.out.print("Cognome: "); String cognome = in.nextLine();
		
		System.out.print("Codice fiscale: "); String cf = in.nextLine();
		
		System.out.print("E-mail personale: "); String mail = in.nextLine();
		
		System.out.print("Inserisci il tuo nome utente: "); String UID = in.nextLine();
		
		System.out.print("Inserisci la tua password: "); String password = in.nextLine();
		
		System.out.print("Inserisci identificativo vaccinazione (fornito al momento della vaccinazione): ");
		String VID = in.nextLine();
		
		// costruzione di un nuovo oggetto tramite costruttore
		return new Cittadino(nome, cognome, cf, UID, password, VID, mail);
	}
	
	// metodi getter
	public String getNome() {return this.nome;}
	public String getCognome() {return this.cognome;}
	public String getCF() {return this.cf.toUpperCase();}
	public String getUID() {return this.UID;}
	public String getPassword() {return this.password;}
	public String getVID() {return this.VID;}
	public String getMail() {return this.mail;}
	
	public String toString() {
		char separatore = ',';
		// ogni oggetto viene scomposto nelle variabili di stato che lo compongono
		return getVID() + separatore + getUID() + separatore + getPassword() + separatore + getCF().toUpperCase() + separatore
			+ getNome().toLowerCase() + separatore + getCognome().toLowerCase() + separatore + getMail().toLowerCase();
	}
	
}