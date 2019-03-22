package application;

import java.util.ArrayList;

public class Client {
	private String nom;
	private ArrayList<Commande> listeCommande = new ArrayList<>();

	public Client( String nom ) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public ArrayList<Commande> getListeCommande() {
		return listeCommande;
	}

	@Override
	public boolean equals( Object client ) {

		return this.getNom().equals( ( (Client) client ).getNom() );
	}
}
