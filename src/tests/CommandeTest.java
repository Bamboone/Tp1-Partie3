package tests;

import application.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommandeTest {
	
	private Commande commande;
	
	@Mock
	private Plat plat;
	
	@Before
	public void setUp() {
		commande = new Commande("Gabriel", plat, 1);
	}
	
	@Test
	public void calculerPrixCommandeAvecTaxes() {
		Mockito.when(plat.getPrix()).thenReturn(10.00);
		assertEquals(11.50, commande.calculerPrix(), 0);
	}
	
	
	

}
