package tests;

import application.*;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.After;
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
	Client client;
	Plat plat;
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
	public void TestPlatExistePas() {
		lecture.listePlats.add( new Plat( "Poutine", 10 ) );
		assertFalse( lecture.chercherPlat( new Plat( "Pâtes", 8 ) ) );
	}

	@Test
	public void TestPlatExiste() {
		lecture.listePlats.add( new Plat( "Poutine", 10 ) );
		assertTrue( lecture.chercherPlat( lecture.listePlats.get( 0 ) ) );
	}

	@Test(expected = NumberFormatException.class)
	public void testFormatPlatPrixIncorrect() throws IOException {
		lecture.lectureListe( "testFormatPlatPrixIncorrect.txt" );
	}

	@Test
	public void testFormatPlatIncorrect() throws IOException {
		lecture.lectureListe( "testFormatPlatIncorrect.txt" );
		assertTrue( lecture.listeErreurs.contains( "Le plat ne respecte pas le bon format" ) );
	}

	@Test
	public void testFormatCommandeIncorrect() throws IOException {
		lecture.lectureListe( "testFormatCommandeIncorrect.txt" );
		assertTrue( lecture.listeErreurs.contains( "La commande ne respecte pas le bon format" ) );
	}

	@Test(expected = NumberFormatException.class)
	public void testFormatCommandeQuantiteIncorrect() throws IOException {
		lecture.lectureListe( "testFormatCommandeQuantiteIncorrect.txt" );
	}

	@Test
	public void testAffectationCommandeClient() {
		Client clientAffectation = new Client( "Gabriel" );
		lecture.listeClients.add( clientAffectation );
		lecture.listeCommandes.add( new Commande( "Gabriel", new Plat( "Poutine", 10.00 ), 1 ) );
		lecture.affecterCommandesAClients();
		assertFalse( clientAffectation.getListeCommande().isEmpty() );
	}

	@Test
	public void testAffectationCommandeClientSansCommande() {
		Client clientAffectation = new Client( "Gabriel" );
		lecture.listeClients.add( clientAffectation );
		lecture.listeCommandes.add( new Commande( "Jean", new Plat( "Poutine", 10.00 ), 1 ) );
		lecture.affecterCommandesAClients();
		assertTrue( clientAffectation.getListeCommande().isEmpty() );
	}

	
	
	

	
}
