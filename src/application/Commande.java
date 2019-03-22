package application;

public class Commande {

	private Plat plat;
	private int quantite;
	private String nomClient;

	public Commande( String nomClient, Plat plat, int quantite ) {
		this.plat = plat;
		this.quantite = quantite;
		this.nomClient = nomClient;
	}

	public Plat getPlat() {
		return plat;
	}

	public int getQuantite() {
		return quantite;
	}

	public String getNomClient() {
		return nomClient;
	}

	public double calculerPrix() {
		return ( plat.getPrix() * quantite ) * 1.15;
	}

}
