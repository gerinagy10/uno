import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Iterator;

/**
 * GUI osztaly a grafikus felulet megjelenitesere
 */
public class GUI extends JFrame implements Serializable {
    private final HatterJPanel namesJP;
    private final JTextField nameJTF;
    private final JComboBox box;
    private final CardLayout cl;
    private final Container gui;
    private Asztal asztal;
    private final JLabel currPlayer, lapJf;
    private final JPanel also;

    /**
     * Konstruktor. Menu, nevbekero es jatekfelulet letrehozasa, mindegyik egy Cardlayout egy-egy oldala.
     */
    public GUI() throws IOException {
        //Alap beallitasok
        super("UNO");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(750, 440);
        cl = new CardLayout();
        gui = getContentPane();
        gui.setLayout(cl);
        Image icon = ImageIO.read(getClass().getResource("/img/icon.png"));
        this.setIconImage(icon);;

        //Menu felulete

        JPanel menu = new JPanel(new BorderLayout());
        HatterJPanel menuHatter = new HatterJPanel();
        menu.add(menuHatter, BorderLayout.CENTER);
        menuHatter.setLayout(new BorderLayout());

        //Jatekosok szamanak kivalasztasa, jatek inditasa gomb
        JPanel p2 = new JPanel(new FlowLayout());
        p2.setOpaque(false);
        menuHatter.add(p2, BorderLayout.SOUTH);
        JLabel jatekosok = new JLabel("Játékosok száma");
        jatekosok.setFont(new Font("Arial", Font.PLAIN, 30));
        jatekosok.setForeground(Color.WHITE);
        p2.add(jatekosok);
        box = new JComboBox();
        for (int i = 2; i < 6; ++i)
            box.addItem(i);
        box.setFont(new Font("Arial", Font.PLAIN, 25));
        p2.add(box);
        JButton start = new JButton("Kezdés");
        start.setFont(new Font("Arial", Font.PLAIN, 25));
        p2.add(start);
        start.addActionListener(new StartButtonActionListener());

        //jatek folytatasa gomb
        JPanel p3 = new JPanel();
        p3.setOpaque(false);
        menuHatter.add(p3, BorderLayout.EAST);
        JButton load = new JButton("Játék folytatása");
        load.setFont(new Font("Arial", Font.PLAIN, 25));
        load.addActionListener(new LoadActionListener());
        p3.add(load);

        gui.add("Menu", menu);

        //Nevek megadasanak felulete, uj oldal

        namesJP = new HatterJPanel();
        namesJP.setLayout(new BorderLayout());

        //Nevek bekerese, illetve kiirasa a kepernyore
        JPanel jpn = new JPanel();
        jpn.setOpaque(false);
        JLabel addName = new JLabel("Add meg a neveket!");
        addName.setFont(new Font("Arial", Font.PLAIN, 20));
        addName.setForeground(Color.WHITE);
        jpn.add(addName);
        nameJTF = new JTextField(30);
        nameJTF.addActionListener(new NameFieldActionListener());
        jpn.add(nameJTF);
        namesJP.add(jpn, BorderLayout.NORTH);

        gui.add("Names", namesJP);

        //Jatekter

        JPanel game = new JPanel(new BorderLayout());
        JatekHatterJPanel hatter = new JatekHatterJPanel();
        game.add(hatter, BorderLayout.CENTER);
        hatter.setLayout(new BorderLayout());

        //Felso panel
        JPanel felso = new JPanel();
        hatter.add(felso, BorderLayout.NORTH);
        felso.setOpaque(false);
        lapJf = new JLabel();
        felso.add(lapJf);
        Image img = ImageIO.read(getClass().getResource("/img/uno.png"));
        Image newimg = img.getScaledInstance( 129, 187,  java.awt.Image.SCALE_SMOOTH ) ;
        JButton huz = new JButton();
        huz.setIcon(new ImageIcon(newimg));
        huz.setFont(new Font("Arial", Font.PLAIN, 20));
        huz.setBorder(null);
        huz.setContentAreaFilled(false);
        huz.addActionListener(new HuzActionListener());
        felso.add(huz);

        //Jobb panel
        JPanel jobb = new JPanel();
        hatter.add(jobb, BorderLayout.EAST);
        JButton save = new JButton("Mentés");
        save.setFont(new Font("Arial", Font.PLAIN, 20));
        save.addActionListener(new SaveActionListener());
        jobb.add(save);
        jobb.setOpaque(false);

        //Also panel
        also = new JPanel();
        hatter.add(also, BorderLayout.SOUTH);
        currPlayer = new JLabel();
        currPlayer.setOpaque(false);
        currPlayer.setFont(new Font("Arial", Font.PLAIN, 20));
        currPlayer.setForeground(Color.ORANGE);
        also.add(currPlayer);
        also.setOpaque(false);

        gui.add("Game", game);

        setVisible(true);
    }

    /**
     * A Kezdes gombhoz rendelt ActionListener
     */
    private class StartButtonActionListener implements ActionListener {

        /**
         * Letrehozza az asztalt a kivalasztott szamu jatekossal,
         * es atvalt a nevbekero nezetre.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            asztal = new Asztal((Integer) box.getSelectedItem());
            addNames();
        }
    }

    /**
     * A nevek bekeresehez rendelt ActionListener
     */
    private class NameFieldActionListener implements ActionListener {
        private int currentPlayers = 0;
        private JPanel p = new JPanel();

        /**
         * A mezobe beirt jatekost hozzaadja az asztalhoz es
         * kiirja a kepernyore. Ha minden jatekos megadta a nevet,
         * atvalt a jatek feluletre.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (currentPlayers < asztal.getJatekosokSzama()) {
                asztal.addJatekos(new Jatekos(nameJTF.getText()));
                currentPlayers++;
                JLabel n = new JLabel(nameJTF.getText());
                p.setOpaque(false);
                n.setFont(new Font("Arial", Font.PLAIN, 30));
                n.setForeground(Color.DARK_GRAY);
                p.add(n);
                namesJP.add(p, BorderLayout.WEST);
                nameJTF.setText("");
                setVisible(true);
                if (currentPlayers == asztal.getJatekosokSzama()){
                    startGame();
                    currentPlayers = 0;
                    p.removeAll();
                    p.revalidate();
                    p.repaint();
                }
            }
        }
    }

    /**
     * Megjeleniti a nevek bekeresere szolgalo feluletet.
     */
    public void addNames() {
        cl.show(gui, "Names");
    }

    /**
     * Megjeleniti a menu feluletet.
     */
    public void newGame(){
        cl.show(gui, "Menu");
    }

    /**
     * Megjeleniti a jatekter feluletet es meghivja az asztal
     * osztas fuggvenyet.
     */
    public void startGame() {
        cl.show(gui, "Game");
        asztal.oszt();
        showPlayer();
    }

    /**
     * Megjeleniti az aktualis jatekost, kiirja a nevet es megjeleniti a lapjait,
     * illetve megjeleniti az aktualis lapot.
     */
    public void showPlayer() {
        also.removeAll();
        also.revalidate();
        also.repaint();
        currPlayer.setText(asztal.getCurrentPlayer().getNev());
        also.add(currPlayer);
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/img/" + asztal.getAktLap().getSzin().toString() + "_" + asztal.getAktLap().getErtek().getErtek() + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image newimg = img.getScaledInstance( 129, 187,  java.awt.Image.SCALE_SMOOTH ) ;
        lapJf.setIcon(new ImageIcon(newimg));
        Iterator<Kartya> it = asztal.getCurrentPlayer().getSajatLapokIterator();
        while (it.hasNext()) {
            Kartya k = it.next();
            Image imgs = null;
            try {
                imgs = ImageIO.read(getClass().getResource("/img/" + k.getSzin().toString() + "_" + k.getErtek().getErtek() + ".png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image newimgs = imgs.getScaledInstance( 65, 94,  java.awt.Image.SCALE_SMOOTH );
            JButton jb = new JButton();
            jb.setBorder(null);
            jb.setContentAreaFilled(false);
            jb.setIcon(new ImageIcon(newimgs));
            jb.addActionListener(new RakActionListener(k));
            also.add(jb);
        }
    }

    /**
     * A huzashoz kotott ActionListener
     */
    private class HuzActionListener implements ActionListener {
        /**
         * A jatekos, aki megnyomta, huz egy lapot, majd valt a kovetkezo jatekosra
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            asztal.huzas(asztal.getCurrentPlayer());
            asztal.kovJatekos();
            showPlayer();
        }
    }

    /**
     * Lap lerakasahoz kotott ActionListener
     */
    private class RakActionListener implements ActionListener {
        private Kartya k;
        RakActionListener(Kartya kartya){
            k = kartya;
        }

        /**
         * A jatekos, aki megnyomta, lerakja a kivalasztott lapot, ha szabalyos lepes.
         * Ellenkezo esetben felugro hibauzenet.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(asztal.lerak(asztal.getCurrentPlayer(), k))
                asztal.lepes(GUI.this);
            else
                JOptionPane.showMessageDialog(null, "Ervenytelen lap!");
        }
    }

    /**
     * Specialis hatteru JPanel a jatek felulethez.
     */
    private class JatekHatterJPanel extends JPanel{

        /**
         * Hatter festese a megadott keppe.
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Image img = null;
            try {
                img = ImageIO.read(getClass().getResource("/img/Table_0.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(img, 0,0,getWidth(),getHeight(), this);
            g2.dispose();
        }
    }

    /**
     * Specialis hatteru JPanel a menu felulethez.
     */
    private class HatterJPanel extends JPanel{

        /**
         * Hatter festese a megadott keppe.
         */
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Image img = null;
            try {
                img = ImageIO.read(getClass().getResource("/img/main.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.drawImage(img, 0,0,getWidth(),getHeight(), this);
            g2.dispose();
        }
    }

    /**
     * A mentes gombhoz rendelt ActionListener.
     */
    private class SaveActionListener implements ActionListener{

        /**
         * Asztal mentese szerializalassal a
         * save.txt fajlba.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            ObjectOutputStream out;
            try {
                FileOutputStream f = new FileOutputStream("save.txt");
                out = new ObjectOutputStream(f);
                out.writeObject(asztal);
                out.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Asztal beallitasa a parameterkent kapottnak.
     */
    public void setAsztal(Asztal asz){
        asztal = asz;
    }

    /**
     * A jatek folytatasa gombhoz rendelt ActionListener.
     */
    private class LoadActionListener implements ActionListener{

        /**
         * Asztal betoltese szerializalassal a
         * save.txt fajlbol.
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                FileInputStream f = new FileInputStream("save.txt");
                ObjectInputStream in = new ObjectInputStream(f);
                asztal = (Asztal)in.readObject();
                asztal.setCurrIterator();
                cl.show(gui, "Game");
                showPlayer();
                asztal.kovJatekos();
                in.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}
