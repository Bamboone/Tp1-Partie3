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

	public LectureFichier() {
			try {
	            lectureListe( "Liste.txt" );
	        } catch ( IOException e ) {
	            e.printStackTrace();
	        }
		

	}
	public void lectureListe(String chemin) throws IOException {
		Path path = Paths.get(chemin);
		BufferedReader ficLecture = Files.newBufferedReader( path, Charset.forName( "UTF-8" ) );
		
		String ligne = "";

		if ( ( ligne = ficLecture.readLine() ).equals( "Clients :" ) ) {
			try {
				// Lecture des clients.
				while ( !( ligne = ficLecture.readLine() ).equals( "Plats :" ) ) {

					listeClients.add( new Client( ligne ) );

				}

				// Lecture des plats.
				while ( !( ligne = ficLecture.readLine() ).equals( "Commandes :" ) ) {

					String[] infoPlat = ligne.split( " " );

					listePlats.add( new Plat( infoPlat[0], Double.parseDouble( infoPlat[1] ) ) );

				}

				// Lecture des commandes.
				while ( !( ligne = ficLecture.readLine() ).equals( "Fin" ) ) {

					Boolean platTrouve = ajouterCommandes( ligne );

					if ( !platTrouve ) {

						System.out.println( "Erreur, le fichier ne respecte pas le bon format." );
					}

				}

				affecterCommandesAClients();

				ecrireFacture();
				
			} catch ( Exception ex ) {

				System.out.println( "Erreur, le fichier ne respecte pas le bon format." );
			}

		} else {

			System.out.println( "Erreur, le fichier ne respecte pas le bon format." );
		}

		ficLecture.close();
		
	}

	public void ecrireFacture() throws IOException {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern( "yyyy-MM-dd_HH'h'mm" );
        LocalDateTime date = LocalDateTime.now();
        Path cheminEcriture = Paths.get( "Facture-du-" + dtf.format( date ) + ".txt" );
        BufferedWriter ficEcriture = Files.newBufferedWriter( cheminEcriture, Charset.forName( "UTF-8" ) );
		
		double totalFacture;
		ficEcriture.write( "Bienvenue chez Barette!" );
		ficEcriture.newLine();
		ficEcriture.write( "\nFactures:" );
		ficEcriture.newLine();

		NumberFormat formatArgent = NumberFormat.getCurrencyInstance();

		for ( Client client : listeClients ) {

			totalFacture = 0;

			for ( Commande commande : client.getListeCommande() ) {

				totalFacture += commande.calculerPrix();
			}

			ficEcriture.write( client.getNom() + " " + formatArgent.format( totalFacture ) );
			ficEcriture.newLine();
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

	public Boolean ajouterCommandes( String ligne ) {
		
		String[] infoCommande = ligne.split( " " );
		Boolean platTrouve = false;

		for ( Plat plat : listePlats ) {

			Client client = new Client( infoCommande[0] );

			if ( infoCommande[1].equals( plat.getNom() ) ) {

				if ( listeClients.contains( client ) ) {

					listeCommandes.add(
							new Commande( infoCommande[0], plat, Integer.parseInt( infoCommande[2] ) ) );
					platTrouve = true;
					break;

				} else {

					System.out.println( "Erreur, le fichier ne respecte pas le bon format." );
					System.exit( 0 );
				}

			}
		}
		return platTrouve;
	}
}
