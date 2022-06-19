import java.io.Serializable;

/**
 * Kartya osztaly, van szine, erteke
 */
public class Kartya implements Serializable {
    private Szin szin;
    private Ertek ertek;

    /**
     * Konstruktor
     */
    Kartya(Szin sz, Ertek e){
        szin = sz;
        ertek = e;
    }

    /**
     * Visszaadja a kartya szinet
     */
    public Szin getSzin(){
        return szin;
    }

    /**
     * Visszaadja a kartya erteket
     */
    public Ertek getErtek(){
        return ertek;
    }

    /**
     * Visszaadja a kartya szinet es erteket Stringkent
     */
    public String toString(){
        return getSzin().toString() + " " + getErtek().toString();
    }

}
