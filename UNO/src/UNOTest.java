import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

//Teszt osztaly
public class UNOTest {

    //Kartya osztaly tesztelese
    @Test
    public void testKartya(){
        Szin sz = Szin.Blue;
        Ertek e = Ertek.Het;
        Kartya k = new Kartya(sz,e);
        Assert.assertEquals("kartya szin", Szin.Blue, k.getSzin());
        Assert.assertEquals("kartya ertek", Ertek.Het, k.getErtek());
    }

    //Jatekos osztaly tesztelese
    @Test
    public void testJatekos(){
        Jatekos j = new Jatekos("Sanya");
        Assert.assertEquals("jatekos nev", "Sanya", j.getNev());
        Kartya k = new Kartya(Szin.Red, Ertek.Ketto);
        j.huz(k);
        j.huz(new Kartya(Szin.Yellow, Ertek.Ketto));
        Assert.assertEquals("jatekos lapjainak szama", 2, j.getLapokSzama());
        Iterator<Kartya> it = j.getSajatLapokIterator();
        Assert.assertEquals("lap lerakasa", k, j.lerak(it.next()));
    }

    // Asztal osztaly tesztelese elotti inicializacio
    Asztal a;
    Jatekos j1, j2, j3;
    GUI gui;
    @Before
    public void setUp(){
        a = new Asztal(3);
        a.addJatekos(j1 = new Jatekos("A"));
        a.addJatekos(j2 = new Jatekos("B"));
        a.addJatekos(j3 = new Jatekos("C"));
        a.oszt();
    }

    // Kezdolap tesztelese, nem lehet specialis
    @Test
    public void testKezdolap(){
        Assert.assertNotEquals("kezdolap", Szin.Spec, a.getAktLap().getSzin());
    }

    // Jatekosok szamanak tesztelese
    @Test
    public void testJatekosokSzama(){
        Assert.assertEquals("jatekosok szama", 3, a.getJatekosokSzama());
    }

    // Lapok osztasanak tesztelese
    @Test
    public void testOsztas(){
        for(int i = 0; i < 3; ++i) {
            Assert.assertEquals("lapok osztasa", 5, a.getCurrentPlayer().getLapokSzama());
            a.kovJatekos();
        }
    }

    // Jatekosok leptetesenek tesztelese
    @Test
    public void testLeptetes(){
        a.kovJatekos();
        Assert.assertEquals("kovetkezo jatekos, normal sorrend", "B", a.getCurrentPlayer().getNev());
    }

    // Lap lerakasa eseten szabalyok betartasanak tesztelese
    @Test
    public void testLerakas(){
        Kartya k = a.getCurrentPlayer().getSajatLapokIterator().next();
        Jatekos j = a.getCurrentPlayer();
        boolean siker = a.lerak(j, k);
        boolean szabalyos = (a.getAktLap().getErtek() == k.getErtek() || a.getAktLap().getSzin() == k.getSzin() ||        // Szabalyok
                k.getSzin() == Szin.Spec || a.getAktLap().getSzin() == Szin.Spec);
        Assert.assertEquals("lerakasi szabalyok", szabalyos, siker);
    }

    // Sorrend megforditasanak tesztelese
    @Test
    public void testForditottSorrend(){
        a.forditottSorrend();
        a.kovJatekos();
        Assert.assertEquals("kovetkezo jatekos, forditott sorrend", "A", a.getCurrentPlayer().getNev());
    }

    // Kovetkezo jatekos huz ket lapot tesztelese
    @Test
    public void testPluszKetto(){
        try {
            gui = new GUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gui.setAsztal(a);
        Kartya k = new Kartya(Szin.Spec, Ertek.PluszKetto);
        j1.huz(k);
        a.lerak(j1,k);
        a.lepes(gui);
        Assert.assertEquals("kovetkezo jatekos huzott 2-t", 7, j2.getLapokSzama());
    }

    // Kovetkezo jatekos kimarad tesztelese
    @Test
    public void testKimarad(){
        try {
            gui = new GUI();
        } catch (IOException e) {
            e.printStackTrace();
        }
        gui.setAsztal(a);
        Kartya k = new Kartya(Szin.Spec, Ertek.Kimarad);
        j1.huz(k);
        a.lerak(j1,k);
        a.lepes(gui);
        Assert.assertEquals("kovetkezo jatekos kimarad", "C", a.getCurrentPlayer().getNev());
    }

}