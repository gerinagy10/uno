/**
 * Enum ertekek tarolasara
 */
public enum Ertek {
    Egy(1), Ketto(2), Harom(3), Negy(4), Ot(5),
    Hat(6), Het(7), Nyolc(8), Kilenc(9), Kimarad(10),
    Sorrend(11), PluszKetto(12), PluszNegy(13);

    private int ertek;

    /**
     * Konstruktor
     */
    Ertek(int i){
        ertek = i;
    }

    /**
     * Ertek lekerdezese
     */
    public int getErtek(){
        return ertek;
    }
}
