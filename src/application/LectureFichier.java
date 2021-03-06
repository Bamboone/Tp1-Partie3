package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.text.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LectureFichier {

	public ArrayList<Client> listeClients = new ArrayList<>();
	public ArrayList<Plat> listePlats = new ArrayList<>();
	public ArrayList<Commande> listeCommandes = new ArrayList<>();
	public ArrayList<String> listeErreurs = new ArrayList<>();

	NumberFormat formatArgent = NumberFormat.getCurrencyInstance();

	public LectureFichier() {
		try {
			lectureListe( "Liste.txt" );
		} catch ( IOException e ) {
			e.printStackTrace();
		}

	}

	public void lectureListe( String chemin ) throws IOException {

		Path path = Paths.get( chemin );
		BufferedReader ficLecture = Files.newBufferedReader( path, Charset.forName( "UTF-8" ) );

		String ligne = "";

		if ( ( ligne = ficLecture.readLine() ).equals( "Clients :" ) ) {

			try {
				// Lecture des clients.
				while ( !( ligne = ficLecture.readLine() ).equals( "Plats :" ) ) {

					ajouterClient( ligne );

				}

				// Lecture des plats.
				while ( !( ligne = ficLecture.readLine() ).equals( "Commandes :" ) ) {

					ajouterPlat( ligne );

				}

				// Lecture des commandes.
				while ( !( ligne = ficLecture.readLine() ).equals( "Fin" ) ) {

					boolean commandesValide = ajouterCommandes( ligne );

					if ( !commandesValide ) {

						listeErreurs.add( "\nCertaines commande n'ont pas pu �tre ajout� en raison d'erreurs" );
					}

				}

				affecterCommandesAClients();
			} catch ( NullPointerException ex ) {
				listeErreurs.add( "\nLe fichier ne respecte pas le bon format." );
			}

		} else {

			listeErreurs.add( "\nLe fichier ne respecte pas le bon format." );
		}

		ecrireFacture();
		afficherFacture();
		ficLecture.close();

	}

	public void ajouterClient( String ligne ) {
		if ( ligne.split( " " ).length == 1 ) {
			listeClients.add( new Client( ligne ) );
		} else {
			listeErreurs.add( "\nLe client " + ligne + " ne respecte pas le bon format" );
		}

	}

	public void ajouterPlat( String ligne ) {
		String[] infoPlat = ligne.split( " " );
		if ( infoPlat.length == 2 ) {
			try {
				listePlats.add( new Plat( infoPlat[0], Double.parseDouble( infoPlat[1] ) ) );
			} catch ( NumberFormatException ex ) {
				listeErreurs.add( "\nLe prix du plat " + infoPlat[0] + " ne respecte pas le bon format" );
				throw new NumberFormatException();
			}

		} else {
			listeErreurs.add( "\nLe plat " + infoPlat[0] + " ne respecte pas le bon format" );
		}
	}

	public void afficherFacture() {
		double totalFacture = 0;

		if ( !listeErreurs.isEmpty() ) {
			System.out.println( "Erreurs:" );
			for ( String erreur : listeErreurs ) {
				System.out.println( erreur );
			}
		}
		if ( !listeClients.isEmpty() && !listePlats.isEmpty() && !listeCommandes.isEmpty() ) {
			System.out.println( "\nBienvenue chez Barette!" );
			System.out.println( "\nFactures:" );

			for ( Client client : listeClients ) {

				totalFacture = 0;

				for ( Commande commande : client.getListeCommande() ) {

					totalFacture += commande.calculerPrix();
				}
				if ( totalFacture != 0 ) {
					System.out.println( client.getNom() + " " + formatArgent.format( totalFacture ) );
				}
			}
			System.out.println();
		}

	}

	public void ecrireFacture() throws IOException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern( "yyyy-MM-dd_HH'h'mm" );
		LocalDateTime date = LocalDateTime.now();
		Path cheminEcriture = Paths.get( "Facture-du-" + dtf.format( date ) + ".txt" );
		BufferedWriter ficEcriture = Files.newBufferedWriter( cheminEcriture, Charset.forName( "UTF-8" ) );

		double totalFacture;

		if ( !listeErreurs.isEmpty() ) {
			ficEcriture.write( "Erreurs:" );
			ficEcriture.newLine();
			for ( String erreur : listeErreurs ) {
				ficEcriture.write( erreur );
				ficEcriture.newLine();
			}
		}

		if ( !listeClients.isEmpty() && !listePlats.isEmpty() && !listeCommandes.isEmpty() ) {
			ficEcriture.write( "\nBienvenue chez Barette!" );
			ficEcriture.newLine();
			ficEcriture.write( "\nFactures:" );
			ficEcriture.newLine();

			for ( Client client : listeClients ) {

				totalFacture = 0;

				for ( Commande commande : client.getListeCommande() ) {

					totalFacture += commande.calculerPrix();
				}
				if ( totalFacture != 0 ) {
					ficEcriture.write( client.getNom() + " " + formatArgent.format( totalFacture ) );
					ficEcriture.newLine();
				}
			}
		}

		ficEcriture.close();
	}

	public void affecterCommandesAClients() {

		for ( Client client : listeClients ) {

			for ( Commande commande : listeCommandes ) {

				if ( client.getNom().equals( commande.getNomClient() ) ) {

					client.getListeCommande().add( commande );
				}
			}
		}
	}

	public boolean ajouterCommandes( String ligne ) {

		String[] infoCommande = ligne.split( " " );
		boolean commandesValide = false;

		for ( Plat plat : listePlats ) {

			if ( chercherClient( infoCommande[0] ) ) {

				if ( chercherPlat( new Plat( infoCommande[1] ) ) ) {
					if ( infoCommande[1].equals( plat.getNom() ) ) {
						if ( infoCommande.length == 3 ) {
							try {
								listeCommandes.add(
										new Commande( infoCommande[0], plat, Integer.parseInt( infoCommande[2] ) ) );
								commandesValide = true;
							} catch ( NumberFormatException ex ) {
								listeErreurs.add( "\nLa quantit� de la commande du client " + infoCommande[0]
										+ " ne respecte pas le bon format" );
								throw new NumberFormatException();
							}
						} else {
							listeErreurs.add(
									"\nLa commande du client " + infoCommande[0] + " ne respecte pas le bon format" );
							break;
						}
					}

				} else {

					listeErreurs.add( "\nLe plat " + infoCommande[1] + " dans la commande n'existe pas." );
					break;
				}

			} else {
				listeErreurs.add( "\nLe nom du client " + infoCommande[0] + " dans la commande n'existe pas." );
				break;
			}
		}

		return commandesValide;
	}

	public boolean chercherClient( String nomClient ) {
		return listeClients.contains( new Client( nomClient ) );
	}

	public boolean chercherPlat( Plat plat ) {
		return listePlats.contains( plat );
	}
}
