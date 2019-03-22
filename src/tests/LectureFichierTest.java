package tests;

import application.*;


import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LectureFichierTest {

	private LectureFichier lecture = new LectureFichier();

	@Mock
	Plat plat;
	@Mock
	Client client;
	@Mock
	Commande commande;

	@Before
	public void setUp() {
		lecture.listeClients = new ArrayList<>();
		lecture.listeCommandes = new ArrayList<>();
		lecture.listePlats = new ArrayList<>();
	}

	@Test
	public void testClientExistePas() {
		lecture.listeClients.add( new Client( "Paul" ) );
		assertFalse( lecture.chercherClient( "Jean" ) );

	}

	@Test
	public void testClientExiste() {
		lecture.listeClients.add( new Client( "Paul" ) );
		assertTrue( lecture.chercherClient( "Paul" ) );

	}

	@Test
	public void testPlatExistePas() {
		lecture.listePlats.add( new Plat( "Poutine", 10 ) );
		assertFalse( lecture.chercherPlat( new Plat( "Pâtes", 8 ) ) );
	}

	@Test
	public void testPlatExiste() {
		lecture.listePlats.add( plat );
		Mockito.when(plat.getNom()).thenReturn("Poutine");
		assertTrue( lecture.chercherPlat( plat ) );
	}

	@Test(expected = NumberFormatException.class)
	public void testFormatPlatPrixIncorrect() throws IOException {
		lecture.lectureListe( "testFormatPlatPrixIncorrect.txt" );
	}

	@Test
	public void testFormatPlatIncorrect() throws IOException {
		lecture.lectureListe( "testFormatPlatIncorrect.txt" );
		assertTrue( lecture.listeErreurs.contains( "\nLe plat Poutine ne respecte pas le bon format" ) );
	}

	@Test
	public void testFormatCommandeIncorrect() throws IOException {
		lecture.lectureListe( "testFormatCommandeIncorrect.txt" );
		assertTrue( lecture.listeErreurs.contains( "\nLa commande du client Roger ne respecte pas le bon format" ) );
	}

	@Test(expected = NumberFormatException.class)
	public void testFormatCommandeQuantiteIncorrect() throws IOException {
		lecture.lectureListe( "testFormatCommandeQuantiteIncorrect.txt" );
	}

	@Test
	public void testAffectationCommandeClient() {
		Mockito.when(client.getNom()).thenReturn("Gabriel");
		Mockito.when(client.getListeCommande()).thenReturn(new ArrayList<Commande>());
		Mockito.when(commande.getNomClient()).thenReturn("Gabriel");
		lecture.listeClients.add( client );
		lecture.listeCommandes.add( commande );
		System.out.println( client.getNom() + commande.getNomClient() );
		lecture.affecterCommandesAClients();
		assertFalse( client.getListeCommande().isEmpty() );
	}

	@Test
	public void testAffectationCommandeClientSansCommande() {
		Client clientAffectation = new Client( "Gabriel" );
		lecture.listeClients.add( clientAffectation );
		lecture.listeCommandes.add( new Commande( "Jean", new Plat( "Poutine", 10.00 ), 1 ) );
		lecture.affecterCommandesAClients();
		assertTrue( clientAffectation.getListeCommande().isEmpty() );
	}

	@Test
	public void testAjouterCommandePlatInexistant() {
		lecture.listeClients.add( new Client("Gabriel") );
		lecture.listePlats.add( new Plat( "Pâtes", 8.00 ) );
		lecture.ajouterCommandes( "Gabriel Poutine 2" );
		assertTrue( lecture.listeCommandes.isEmpty() );
	}
	
	@Test
	public void testAjouterCommandeClientInexistant() {
		lecture.listeClients.add( new Client("Jean") );
		lecture.ajouterCommandes( "Gabriel Poutine 2" );
		assertTrue( lecture.listeCommandes.isEmpty() );
	}
	
	@Test
	public void testAjouterCommandeClientPlatExistant() {
		Mockito.when(plat.getNom()).thenReturn("Pâtes");
		Mockito.when(client.getNom()).thenReturn("Gabriel");
		lecture.listeClients.add( client );
		lecture.listePlats.add( plat );
		assertTrue( lecture.ajouterCommandes( "Gabriel Pâtes 2" ) );
	}
	
	@Test
	public void testFichierSansEnteteClient() throws IOException {
		lecture.lectureListe("testFichierSansEnteteClient.txt");
		assertTrue(lecture.listeErreurs.contains( "\nLe fichier ne respecte pas le bon format." ));
	}
	
	
	

	
}
