import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * Asztal osztaly a jatekosok, lerakott lapok es a pakli tarolasara,
 * illetve a jatek iranyitasara
 */
public class Asztal implements Serializable {
    private List<Kartya> pakli, lerakottLapok;
    private int jatekosokSzama;
    private List<Jatekos> jatekosok;
    private transient ListIterator<Jatekos> currIterator;
    private Jatekos currentPlayer;
    private boolean eredetiSorrend;
    private static final long serialVersionUID = -4033805607579183884L;     // szerializalashoz

    /**
     * Konstruktor. Beallitja a jatekosok szamat a parameterben kapott szamra,
     * letrehozza a jatekosok listajat (uresen), inicializalja a paklit,
     * beallitja a kezdolapot (nem lehet specialis), beallitja a sorrendet normalra.
     */
    public Asztal(int jszam){
        jatekosokSzama = jszam;
        jatekosok = new ArrayList<>();
        pakliInit();
        setKezdolap();
        eredetiSorrend = true;
    }

    /**
     * Jelenlegi iterator beallitasa a jelenlegi jatekos alapjan.
     * Szerializalas miatt kell, mert a ListIterator nem szerializalhato.
     */
    public void setCurrIterator() {
        currIterator = jatekosok.listIterator(jatekosok.indexOf(currentPlayer));
    }

    /**
     * Pakli inicializalasa. Feltolti a paklit 1-9ig az osszes szam osszes szinnel
     * valo kombinaciojaval es minden specialis lapbol 4-gyel. Ezutan megkeveri.
     */
    private void pakliInit(){
        pakli = new ArrayList<>();
        for(int i = 0; i < 9; ++i){
            Ertek ertek = Ertek.values()[i];
            for(int j = 0; j < 4; ++j){
                Kartya kartya = new Kartya(Szin.values()[j], ertek);
                pakli.add(kartya);
            }
        }
        for(int i = 0; i < 4; ++i){
            pakli.add(new Kartya(Szin.Spec, Ertek.Kimarad));
            pakli.add(new Kartya(Szin.Spec, Ertek.Sorrend));
            pakli.add(new Kartya(Szin.Spec, Ertek.PluszKetto));
            pakli.add(new Kartya(Szin.Spec, Ertek.PluszNegy));
        }
        for(int i = 0; i < 10; ++i)
            Collections.shuffle(pakli);
    }

    /**
     * Kezdolap beallitasa, nem lehet specialis lap.
     * Amint megvan a megfelelo, atteszi a lerakott lapokba
     * es eltavolitja a paklibol.
     */
    private void setKezdolap(){
        lerakottLapok = new ArrayList<>();
        Iterator<Kartya> it = pakli.iterator();
        Kartya k = it.next();
        while(k.getSzin().equals(Szin.Spec))
            k = it.next();
        lerakottLapok.add(k);
        pakli.remove(k);
    }

    /**
     * Jatekos hozzaadasa a jatekhoz.
     */
    public void addJatekos(Jatekos j){
        jatekosok.add(j);
    }

    /**
     * Visszaadja a jatekosok szamat.
     */
    public int getJatekosokSzama(){
        return jatekosokSzama;
    }

    /**
     * Lapok osztasa, fejenkent 5 db. Vegigmegy a jatekosokon,
     * 5 lapot ad nekik, ezeket eltavolitja a paklibol.
     */
    public void oszt(){
        currIterator = jatekosok.listIterator();
        currentPlayer = currIterator.next();
        Iterator<Jatekos> it = jatekosok.iterator();
        while(it.hasNext()){
            Jatekos j = it.next();
            for (int i = 0; i < 5; ++i){
                j.huz(pakli.get(0));
                pakli.remove(pakli.get(0));
            }
        }
    }

    /**
     * Visszaadja a lerakott lapok kozul az utoljara lerakottat, a legfelsot.
     */
    public Kartya getAktLap(){
        Iterator<Kartya> it = lerakottLapok.iterator();
        Kartya k = it.next();
        while(it.hasNext())
            k = it.next();
        return k;
    }

    /**
     * A parameterben kapott jatekos felhuzza a pakli felso lapjat, amely
     * ezutan eltavolitasra kerul belole.
     */
    public void huzas(Jatekos j){
        j.huz(pakli.get(0));
        pakli.remove(pakli.get(0));
    }

    /**
     * A jatekosok lepteteset valositja meg, figyelembeveve, hogy eppen
     * normal vagy forditott sorrendben kovetkeznek.
     */
    public void kovJatekos(){
        if(eredetiSorrend) {            // normal sorrend
            if (!currIterator.hasNext())                    // Ha nincs kovetkezo jatekos, akkor
                currIterator = jatekosok.listIterator();    // az elso kovetkezik.
            currentPlayer = currIterator.next();
        }
        else {                          // forditott sorrend
            if (!currIterator.hasPrevious())                                    // Ha nincs elozo jatekos, akkor
                currIterator = jatekosok.listIterator(getJatekosokSzama());     // az utolso kovetkezik.
            currentPlayer = currIterator.previous();
        }
    }

    /**
     * Visszaadja az aktualis jatekost.
     */
    public Jatekos getCurrentPlayer(){
        return currentPlayer;
    }

    /**
     * A parameterkent kapott jatekos a szinten parameterkent kapott kartyat szeretne lerakni.
     * Igazzal ter vissza, ha ez a szabalyoknak megfelelo es le is rakja. Ha szabalyellenes lepes,
     * hamissal ter vissza.
     */
    public boolean lerak(Jatekos j, Kartya k){
        if(getAktLap().getErtek() == k.getErtek() || getAktLap().getSzin() == k.getSzin() ||        // Szabalyok
                k.getSzin() == Szin.Spec || getAktLap().getSzin() == Szin.Spec){                    // ellenorzese.
            lerakottLapok.add(j.lerak(k));
            return true;
        }
        else
            return false;
    }

    /**
     * Atallitja a sorrendet jelzo booleant az ellentettjere.
     */
    public void forditottSorrend(){
        eredetiSorrend = !eredetiSorrend;
    }

    /**
     * A jatekot iranyitja. Ellenorzi, hogy van-e gyoztes, ha van, akkor ezt
     * egy felugro ablakban kozli es megjeleniti a menut a parameterkent kapott GUI segitsegevel.
     * Tovabba ellenorzi, hogy a lerakott lap specialis-e, ha igen, akkor aszerint cselekszik, amilyen lap
     * van lent es hivja a parameterkent kapott GUI-n a kovetkezo jatekos megjeleniteset. Ha nem specialis
     * a lap, akkor csak a kovetkezo jatekost jeleniti meg.
     */
    public void lepes(GUI gui) {
        if(getCurrentPlayer().getLapokSzama() == 0) {       // Ha elfogytak a lapjai, nyert.
            JOptionPane.showMessageDialog(null, "Jo akkor a " + getCurrentPlayer().getNev() + " nyert, jo ejszakat, szevasztok!");
            gui.newGame();                                  // Megjeleniti a menut.
        }
        else if(getAktLap().getSzin() == Szin.Spec){        // Ha specialis lap.
            kovJatekos();
            switch (getAktLap().getErtek()){                // Specialis lap erteke szerint.
                case PluszKetto:                            // +2
                    for(int i = 0; i < 2; ++i)              // Kovetkezo jatekos
                        huzas(getCurrentPlayer());          // huz 2-t.
                    JOptionPane.showMessageDialog(null, getCurrentPlayer().getNev() + " huzott 2-t!");
                    break;
                case PluszNegy:                             // +4
                    for(int i = 0; i < 4; ++i)              // Kovetkezo jatekos
                        huzas(getCurrentPlayer());          // huz 4-et.
                    JOptionPane.showMessageDialog(null, getCurrentPlayer().getNev() + " huzott 4-et!");
                    break;
                case Kimarad:                               // Jatekos kimarad, nem tortenik semmi.
                    JOptionPane.showMessageDialog(null, getCurrentPlayer().getNev() + " kimarad!");
                    break;
                case Sorrend:                               // Sorrend megfordul
                    for(int i = 0; i < getJatekosokSzama() - 2; ++i)        // Mivel az elejen volt leptetes
                        kovJatekos();                                   // es nekunk az eredeti jatekos elotti kell,
                    forditottSorrend();                                     // ezert visszaterunk az eredeti jatekosra, es utana forditjuk meg a sorrendet.
                    JOptionPane.showMessageDialog(null, "A sorrend megfordult!");
                    break;
                }
            }
            kovJatekos();
            gui.showPlayer();         // Leptetunk a kovetkezo jatekosra
    }
}
