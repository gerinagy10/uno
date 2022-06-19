import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jatekos osztaly, tartalmazza a nevet es a lapjait
 */
public class Jatekos implements Serializable {
    private String nev;
    private List<Kartya> sajatLapok;

    /**
     * Konstruktor, parametere a jatekos neve, letrehozza a sajat paklit (uresen).
     */
    Jatekos(String name){
        nev = name;
        sajatLapok = new ArrayList<>();
    }

    /**
     * Kartya lerakasa. A parameterkent kapott kartyat eltavolitja
     * a sajat lapjai kozul es ezt adja vissza.
     */
    public Kartya lerak(Kartya k){
        sajatLapok.remove(k);
        return k;
    }

    /**
     * Kartya huzasa. A parameterkent kapott kartyat beteszi
     * a sajat lapjai koze.
     */
    public void huz(Kartya k){
        sajatLapok.add(k);
    }

    /**
     * Visszaadja a jatekos nevet
     */
    public String getNev(){
        return nev;
    }

    /**
     * Visszaadja a lapjaira mutato iteratort
     */
    public Iterator<Kartya> getSajatLapokIterator(){
        return sajatLapok.iterator();
    }

    /**
     * Visszaadja a jatekos lapjainak a szamat
     */
    public int getLapokSzama(){
        return sajatLapok.size();
    }
}
