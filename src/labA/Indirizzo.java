package labA;
import java.util.Scanner;

// classe ausiliaria creata per gestire gli indirizzi dei centri vaccinali (oppure in un futuro ampliamento
// del progetto anche quello di residenza dei vaccinati/registrati)
// ATTENZIONE: una volta creato l'oggetto che descrive un dato indirizzo, i suoi dati NON possono
// essere modificati per motivi di sicurezza (quindi non vi sono metodi "setter")

public class Indirizzo{
	private String qualificatore;
	private String nome;
	private String civico;
	private String comune;
	private String provincia;
	// SHORT = 16 BIT => VALORE POSITIVO MAX 32767 (NON SUFFICIENTE)
	private int cap;
	
	
	// costruzione dell'oggetto solo con tutte le info necessarie
	// inizializzazione variabili di stato
	Indirizzo(String qualificatore, String nome, String civico, 
			String comune, String provincia, int cap){
		this.qualificatore = qualificatore;
		this.nome = nome;
		this.civico = civico;
		this.comune = comune;
		this.provincia = provincia;
		this.cap = cap;
	}
	
	// metodo per la creazione di un oggetto indirizzo inserendo le info necessarie
	public static Indirizzo inserisciInfo() {
		Scanner in = new Scanner(System.in);
		System.out.println("***Inserire ora i dati legati all'INDIRIZZO della struttura***");
		
		System.out.print("Qualificatore [via/v.le/p.zza]: "); String qualificatore = in.nextLine();
		
		System.out.print("Nome " + qualificatore + ": "); String nome = in.nextLine();
		
		System.out.print("Civico: "); String civico = in.nextLine();
		
		System.out.print("Comune: "); String comune = in.nextLine();
		
		System.out.print("Provincia (sigla): "); String provincia = in.nextLine();
		
		System.out.print("CAP: "); int cap = in.nextInt(); System.out.println("");
		
		// costruzione di un nuovo oggetto tramite costruttore 
		return new Indirizzo(qualificatore, nome, civico, comune, provincia, cap);	
	}
	
	// metodi getter utilizzati per ottenere il valore delle variabili private
	public String getQualificatore() {return this.qualificatore;}
	public String getNome() {return this.nome;}
	public String getCivico() {return this.civico;}
	public String getComune() {return this.comune;}
	public String getProvincia() {return this.provincia;}
	public int getCAP() {return this.cap;}
	
	// stampa informazioni sul centro --> override metodo della superclasse
	public String toString() {
		return getQualificatore() + " " + getNome() + " " + getCivico() + ", " + getComune() + " (" + 
				getProvincia().toUpperCase() + "), " + getCAP();
	}
	
}
