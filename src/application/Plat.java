package application;

public class Plat {

	private String nom;
	private double prix;

	public Plat( String nom ) {
		this.nom = nom;
	}

	public Plat( String nom, double prix ) {
		this.nom = nom;
		this.prix = prix;

	}

	public String getNom() {
		return nom;
	}

	public double getPrix() {
		return prix;
	}

	@Override
	public boolean equals( Object o ) {

		Plat plat = (Plat) o;
		return plat.getNom().equals( this.nom );

	}
}
