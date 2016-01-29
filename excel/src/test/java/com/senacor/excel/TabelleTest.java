package com.senacor.excel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import java.text.ParseException;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Tabelle.class)
public class TabelleTest {
	
	@Before
	public void setUp() {
		PowerMockito.spy(Tabelle.class);
	}
	
	//PowerMockito.mockStatic(App.class);
		//	when(App.getInput()).thenReturn("Hello!");
	
	
	@Test
	public void monat_eins_ergibt_januar() {	
		String ergebnis = Tabelle.getMonat("1");
		PowerMockito.verifyStatic();
		assertEquals("Januar",ergebnis);
	}
	@Test
	public void monat_dreizehn_ergibt_null() {	
		String ergebnis = Tabelle.getMonat("13");
		PowerMockito.verifyStatic();
		assertEquals(null,ergebnis);
	}
	@Test
	public void pfad_existiert() {
		boolean ergebnis = Tabelle.existiert_File("A:\\02_Vorlage_Stundennachweis_Werkstudenten.xlsx");
		PowerMockito.verifyStatic();
		assertEquals(true,ergebnis);
	}
	@Test
	public void pfad_existiert_nicht() {
		boolean ergebnis = Tabelle.existiert_File("A:\\Werkstudenten.xlsx");
		PowerMockito.verifyStatic();
		assertEquals(false,ergebnis);
	}
	@Test
	public void woche_2_ist_woche() {
		boolean ergebnis = Tabelle.pruefe_Kwoche("2");
		PowerMockito.verifyStatic();
		assertEquals(true,ergebnis);
	}
	@Test
	public void woche_66_ist_keine_woche() {
		boolean ergebnis = Tabelle.pruefe_Kwoche("66");
		PowerMockito.verifyStatic();
		assertEquals(false,ergebnis);
	}
	@Test
	public void j_ergibt_x() {
		String ergebnis = Tabelle.getKreuz("j");
		PowerMockito.verifyStatic();
		assertEquals("X",ergebnis);
	}
	@Test
	public void n_ergibt_leeren_string() {
		String ergebnis = Tabelle.getKreuz("n");
		PowerMockito.verifyStatic();
		assertEquals("",ergebnis);
	}
	@Test
	public void jn_ergibt_null() {
		String ergebnis = Tabelle.getKreuz("jn");
		PowerMockito.verifyStatic();
		assertEquals(null,ergebnis);
	}
	@Test
	public void wasauchimmer_ist_beschreibung() {
		boolean ergebnis = Tabelle.pruefe_Taetigkeit("was auch immer");
		PowerMockito.verifyStatic();
		assertEquals(true,ergebnis);
	}
	@Test
	public void keine_beschreibung_ergibt_false() {
		boolean ergebnis = Tabelle.pruefe_Taetigkeit("");
		PowerMockito.verifyStatic();
		assertEquals(false,ergebnis);
	}
	@Test
	public void zu_lange_beschreibung_ergibt_false() {
		boolean ergebnis = Tabelle.pruefe_Taetigkeit("ndfjdlfkjd dfkdjfkdlfj kdjfkdljf dlsdjkle  jdkfjdle e");
		PowerMockito.verifyStatic();
		assertEquals(false,ergebnis);
	}
	@Test
	public void zeile_mo_ist_14() {
		int ergebnis = Tabelle.get_Zeile_Wochentag("mo");
		PowerMockito.verifyStatic();
		assertEquals(14,ergebnis);
	}
	@Test
	public void zeile_ma_ist_0() {
		int ergebnis = Tabelle.get_Zeile_Wochentag("ma");
		PowerMockito.verifyStatic();
		assertEquals(0,ergebnis);
	}
	@Test
	public void falsches_datumsformat_throws_exception() {
		
		try {
			Tabelle.pruefe_Datum("23.11h.2015");
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
		}
		PowerMockito.verifyStatic();
	}
	@Test
	public void richtiges_datum_ist_korrekt() {
		try {
			Tabelle.pruefe_Datum("23.11.2015");
		} catch (ParseException e) {
			fail("Sollte nicht passieren");
		}
		PowerMockito.verifyStatic();
	}
	@Test
	public void falsches_uhrzeitformat_throws_exception() {
		
		try {
			Tabelle.pruefe_Uhrzeit("23:334");
			fail("Should have thrown ParseException");
		} catch (ParseException e) {
		}
		PowerMockito.verifyStatic();
	}
	@Test
	public void richtige_uhrzeit_ist_korrekt() {
		try {
			Tabelle.pruefe_Uhrzeit("23:22");
		} catch (ParseException e) {
			fail("Sollte nicht passieren");
		}
		PowerMockito.verifyStatic();
	}

}
