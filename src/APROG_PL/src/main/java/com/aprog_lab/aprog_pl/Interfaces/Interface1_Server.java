package com.aprog_lab.aprog_pl.Interfaces;

import com.aprog_lab.aprog_pl.events.BlackoutEvent;
import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.shared_resources.Logger;
import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.VecnaChecker;
import com.aprog_lab.aprog_pl.threads.Child;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.EventManager;
import com.aprog_lab.aprog_pl.threads.PortalManager;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import javax.swing.DefaultListModel;

/**
 *
 * @author Emanuel Baciu
 */
public class Interface1 extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Interface1.class.getName());
    private Unsafe_Zone Forest, Lab, Mall, Sewer, Hive;
    private Safe_Zone ms, bb, radio;
    private Portal p1, p2, p3, p4;
    private ArrayList<Unsafe_Zone> uz;
    private ArrayList<Safe_Zone> sz;
    private ArrayList<Portal> portals;
    private EventManager em;
    private Logger log;
    
    private ArrayList<DefaultListModel<String>> uz_models_D;
    private ArrayList<DefaultListModel<String>> uz_models_C;
    private ArrayList<DefaultListModel<String>> sz_models_C;
    private ArrayList<DefaultListModel<String>> enter_portal_models;
    private ArrayList<DefaultListModel<String>> exit_portal_models;
    

    /**
     * Creates new form Interface1
     */
    public Interface1()
    {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(this);
        jTextField_BLOODCOUNT.setText("");
        jTextField_HIVE_CAPTURED.setText("");
        jTextField_CURRENT_EVENT.setText("");
        log = new Logger();
        
        // ===================== SETTING DEFAULTLISTMODEL MODEL FOR JLISTS =====================
        DefaultListModel<String> m1 = new DefaultListModel<>();
        jList_MAIN_STREET.setModel(m1);
        DefaultListModel<String> m2 = new DefaultListModel<>();
        jList_BYERS.setModel(m2);
        DefaultListModel<String> m3 = new DefaultListModel<>();
        jList_WSQK.setModel(m3);
        DefaultListModel<String> m4 = new DefaultListModel<>();
        jList_ENTER_PORTAL_FOREST.setModel(m4);
        DefaultListModel<String> m5 = new DefaultListModel<>();
        jList_ENTER_PORTAL_LAB.setModel(m5);
        DefaultListModel<String> m6 = new DefaultListModel<>();
        jList_ENTER_PORTAL_MALL.setModel(m6);
        DefaultListModel<String> m7 = new DefaultListModel<>();
        jList_ENTER_PORTAL_SEWER.setModel(m7);
        DefaultListModel<String> m8 = new DefaultListModel<>();
        jList_EXIT_PORTAL_FOREST.setModel(m8);
        DefaultListModel<String> m9 = new DefaultListModel<>();
        jList_EXIT_PORTAL_LAB.setModel(m9);
        DefaultListModel<String> m10 = new DefaultListModel<>();
        jList_EXIT_PORTAL_MALL.setModel(m10);
        DefaultListModel<String> m11 = new DefaultListModel<>();
        jList_EXIT_PORTAL_SEWER.setModel(m11);
        DefaultListModel<String> m12 = new DefaultListModel<>();
        jList_CHILDREN_FOREST.setModel(m12);
        DefaultListModel<String> m13 = new DefaultListModel<>();
        jList_CHILDREN_LAB.setModel(m13);
        DefaultListModel<String> m14 = new DefaultListModel<>();
        jList_CHILDREN_MALL.setModel(m14);
        DefaultListModel<String> m15 = new DefaultListModel<>();
        jList_CHILDREN_SEWERS.setModel(m15);
        DefaultListModel<String> m16 = new DefaultListModel<>();
        jList_DEMOS_FOREST.setModel(m16);
        DefaultListModel<String> m17 = new DefaultListModel<>();
        jList_DEMOS_LAB.setModel(m17);
        DefaultListModel<String> m18 = new DefaultListModel<>();
        jList_DEMOS_MALL.setModel(m18);
        DefaultListModel<String> m19 = new DefaultListModel<>();
        jList_DEMOS_SEWERS.setModel(m19);
        
        sz_models_C = new  ArrayList<>();
        uz_models_C = new  ArrayList<>();
        uz_models_D = new  ArrayList<>();
        enter_portal_models = new  ArrayList<>();
        exit_portal_models = new  ArrayList<>();
        
        sz_models_C.add(m1); sz_models_C.add(m2); sz_models_C.add(m3);
        
        enter_portal_models.add(m4); enter_portal_models.add(m5); enter_portal_models.add(m6); enter_portal_models.add(m7);
        
        exit_portal_models.add(m8); exit_portal_models.add(m9); exit_portal_models.add(m10); exit_portal_models.add(m11);
        
        uz_models_C.add(m12); uz_models_C.add(m13); uz_models_C.add(m14); uz_models_C.add(m15);
        
        uz_models_D.add(m16); uz_models_D.add(m17); uz_models_D.add(m18); uz_models_D.add(m19);
        
    // ===================== SAFE AND UNSAFE ZONE INITIALIZATION =====================
        Forest = new Unsafe_Zone("Forest", this, log);
        Lab = new Unsafe_Zone("Laboratory", this, log);
        Mall = new Unsafe_Zone("Shopping Mall", this, log);
        Sewer = new Unsafe_Zone("Sewer", this, log);
        Hive = new Unsafe_Zone("HIVE", this, log);

        ms = new Safe_Zone("Hawkin's Main Street", this, log);                       
        bb = new Safe_Zone("Bayer's Basement", this, log);
        radio = new Safe_Zone("WSQK Radio", this, log);
        
        uz = new ArrayList<>();                              // Children and Demogorgons share this ArrayList.
        sz = new ArrayList<>();                                // ONLY children are allowed to use this ArrayList.
        portals = new ArrayList<>();                              // Children and PortalManager use this ArrayList.
        
        // ===================== SAFE AND UNSAFE ZONE ADDITION =====================
        uz.add(Forest); uz.add(Lab); uz.add(Mall); uz.add(Sewer); uz.add(Hive);
        sz.add(ms); sz.add(bb); sz.add(radio);
        
// ===================== PORTAL INITIALIZATION =====================
        p1 = new Portal("ForestPortal", bb, Forest, new CyclicBarrier(2), this, log);
        p2 = new Portal("LabPortal", bb, Lab, new CyclicBarrier(3), this, log);
        p3 = new Portal("MallPortal", bb, Mall, new CyclicBarrier(4), this, log);
        p4 = new Portal("SewerPortal", bb, Sewer, new CyclicBarrier(2), this, log);
        
        // ===================== PORTAL ADDITION =====================
        portals.add(p1); portals.add(p2); portals.add(p3); portals.add(p4);
        
        // ===================== PORTAL MANAGER =====================
        new PortalManager(portals, log).start();
        
// ===================== EVENT-RELATED OBJECT INITIALIZATION =====================
        BlackoutEvent be = new BlackoutEvent(portals);
        StormEvent se = new StormEvent();
        ElevenSavesEvent ese = new ElevenSavesEvent(uz.get(4));
        HiveMindEvent hme = new HiveMindEvent(uz);
        
        em = new EventManager(be, se, ese, hme, log);
        em.start();

// ===================== VECNA CHECKER INITIALIZATION =====================
        VecnaChecker vc = new VecnaChecker(1, uz, se, ese, hme, log);
        
// ===================== CHILDREN & DEMOGORGON INITIALIZATION =====================

        // Initial threads (Alpha Demog and Children) creation.
        try
        {
            int idn = 0;                                                                               // Used for Children ID and Alpha Demogorgon.
            new Demogorgon("D"+String.format("%04d",idn), uz, vc, se, ese, hme, log).start();          // Formatted ID for the Alpha Demogorgon (D0000)
            for(int i=0; i<1500; i++)
            {
                Thread.sleep((int)(Math.random()*1.5+0.5));                                         // SHOULD wait between 0.5 and 2 seconds.
                String child_id = "C"+String.format("%04d", idn);                                   // Formatted ID for children
                new Child(child_id, sz, uz, portals,se, log).start();
                idn++;
            }
        }
        catch(InterruptedException ie)
        {
            System.out.println("Interrupted Exception -> main()");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTextField_BLOODCOUNT = new javax.swing.JTextField();
        jLabel_MAIN_STREET = new javax.swing.JLabel();
        jLabel_BAYERS = new javax.swing.JLabel();
        jLabel_WSQK = new javax.swing.JLabel();
        jLabel_BLOOD = new javax.swing.JLabel();
        jTextField_HIVE_CAPTURED = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel_FOREST = new javax.swing.JLabel();
        jLabel_LABORATORY = new javax.swing.JLabel();
        jLabel_MALL = new javax.swing.JLabel();
        jLabel_SEWERS = new javax.swing.JLabel();
        jLabel_PORTALS = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_MAIN_STREET = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_BYERS = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_WSQK = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_FOREST = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_LAB = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_LAB = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_MALL = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_MALL = new javax.swing.JList<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_SEWER = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_FOREST = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_SEWER = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList_DEMOS_FOREST = new javax.swing.JList<>();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList_DEMOS_LAB = new javax.swing.JList<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        jList_CHILDREN_MALL = new javax.swing.JList<>();
        jScrollPane16 = new javax.swing.JScrollPane();
        jList_DEMOS_MALL = new javax.swing.JList<>();
        jScrollPane17 = new javax.swing.JScrollPane();
        jList_CHILDREN_SEWERS = new javax.swing.JList<>();
        jScrollPane18 = new javax.swing.JScrollPane();
        jList_DEMOS_SEWERS = new javax.swing.JList<>();
        jScrollPane19 = new javax.swing.JScrollPane();
        jList_CHILDREN_FOREST = new javax.swing.JList<>();
        jScrollPane20 = new javax.swing.JScrollPane();
        jList_CHILDREN_LAB = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        jTextField_CURRENT_EVENT = new javax.swing.JTextField();
        jButton_PLAY = new javax.swing.JButton();
        jButton_STOP = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Epsteins vs Children");

        jTextField_BLOODCOUNT.setEditable(false);
        jTextField_BLOODCOUNT.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 48)); // NOI18N
        jTextField_BLOODCOUNT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_BLOODCOUNT.setText("jTextField2");

        jLabel_MAIN_STREET.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_MAIN_STREET.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_MAIN_STREET.setText("MAIN STREET");

        jLabel_BAYERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_BAYERS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_BAYERS.setText("BYERS BASEMENT");

        jLabel_WSQK.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_WSQK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_WSQK.setText("RADIO WSQK");

        jLabel_BLOOD.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_BLOOD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_BLOOD.setText("BLOOD COUNT");

        jTextField_HIVE_CAPTURED.setEditable(false);
        jTextField_HIVE_CAPTURED.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jTextField_HIVE_CAPTURED.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_HIVE_CAPTURED.setText("jTextField2");

        jLabel1.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HIVE CHILDREN");

        jLabel_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_FOREST.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_FOREST.setText("FOREST");

        jLabel_LABORATORY.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_LABORATORY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_LABORATORY.setText("LABORATORY");

        jLabel_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_MALL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_MALL.setText("MALL");

        jLabel_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_SEWERS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_SEWERS.setText("SEWERS");

        jLabel_PORTALS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_PORTALS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_PORTALS.setText("PORTALS");

        jList_MAIN_STREET.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_MAIN_STREET.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList_MAIN_STREET);

        jList_BYERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_BYERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList_BYERS);

        jList_WSQK.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_WSQK.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList_WSQK);

        jList_EXIT_PORTAL_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList_EXIT_PORTAL_FOREST);

        jList_EXIT_PORTAL_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList_EXIT_PORTAL_LAB);

        jList_ENTER_PORTAL_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_LAB.setAutoscrolls(false);
        jScrollPane6.setViewportView(jList_ENTER_PORTAL_LAB);

        jList_ENTER_PORTAL_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_MALL.setAutoscrolls(false);
        jScrollPane7.setViewportView(jList_ENTER_PORTAL_MALL);

        jList_EXIT_PORTAL_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(jList_EXIT_PORTAL_MALL);

        jList_ENTER_PORTAL_SEWER.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_SEWER.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_SEWER.setAutoscrolls(false);
        jScrollPane9.setViewportView(jList_ENTER_PORTAL_SEWER);

        jList_ENTER_PORTAL_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_FOREST.setAutoscrolls(false);
        jScrollPane10.setViewportView(jList_ENTER_PORTAL_FOREST);

        jList_EXIT_PORTAL_SEWER.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_SEWER.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane11.setViewportView(jList_EXIT_PORTAL_SEWER);

        jList_DEMOS_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_FOREST.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane12.setViewportView(jList_DEMOS_FOREST);

        jList_DEMOS_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_LAB.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane13.setViewportView(jList_DEMOS_LAB);

        jList_CHILDREN_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_MALL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane15.setViewportView(jList_CHILDREN_MALL);

        jList_DEMOS_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_MALL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane16.setViewportView(jList_DEMOS_MALL);

        jList_CHILDREN_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_SEWERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_SEWERS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane17.setViewportView(jList_CHILDREN_SEWERS);

        jList_DEMOS_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_SEWERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_SEWERS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane18.setViewportView(jList_DEMOS_SEWERS);

        jList_CHILDREN_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_FOREST.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane19.setViewportView(jList_CHILDREN_FOREST);

        jList_CHILDREN_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_LAB.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane20.setViewportView(jList_CHILDREN_LAB);

        jLabel2.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CURRENT EVENT");

        jTextField_CURRENT_EVENT.setEditable(false);
        jTextField_CURRENT_EVENT.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jTextField_CURRENT_EVENT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_CURRENT_EVENT.setText("jTextField1");
        jTextField_CURRENT_EVENT.addActionListener(this::jTextField_CURRENT_EVENTActionPerformed);

        jButton_PLAY.setText("PLAY");
        jButton_PLAY.addActionListener(this::jButton_PLAYActionPerformed);

        jButton_STOP.setText("STOP");
        jButton_STOP.addActionListener(this::jButton_STOPActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(jLabel_MAIN_STREET, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel_BAYERS, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(138, 138, 138)
                        .addComponent(jLabel_PORTALS, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_WSQK, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel_BLOOD, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField_BLOODCOUNT, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(60, 60, 60)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jButton_PLAY)))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(jButton_STOP)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(1, 1, 1))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel1))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_FOREST)
                                .addGap(39, 39, 39)
                                .addComponent(jTextField_HIVE_CAPTURED, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel_MALL, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(jLabel2))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jTextField_CURRENT_EVENT, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel_LABORATORY))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel_SEWERS, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_MAIN_STREET)
                    .addComponent(jLabel_BAYERS)
                    .addComponent(jLabel_PORTALS))
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel_WSQK)
                        .addGap(6, 6, 6)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(jLabel_BLOOD)
                        .addGap(6, 6, 6)
                        .addComponent(jTextField_BLOODCOUNT, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_FOREST)
                            .addComponent(jTextField_HIVE_CAPTURED, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_MALL)
                            .addComponent(jLabel2))
                        .addGap(4, 4, 4)
                        .addComponent(jTextField_CURRENT_EVENT, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabel_LABORATORY)
                        .addGap(109, 109, 109)
                        .addComponent(jLabel_SEWERS))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jScrollPane19, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(6, 6, 6)
                                        .addComponent(jScrollPane20, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(8, 8, 8)
                                        .addComponent(jScrollPane16, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(3, 3, 3)
                                        .addComponent(jScrollPane13, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane18, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane17, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton_PLAY)
                            .addComponent(jButton_STOP))))
                .addContainerGap(48, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_PLAYActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_PLAYActionPerformed
        java.awt.EventQueue.invokeLater(() -> 
                log.resume()
        );
    }//GEN-LAST:event_jButton_PLAYActionPerformed

    private void jButton_STOPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_STOPActionPerformed
        java.awt.EventQueue.invokeLater(() -> log.stop());
    }//GEN-LAST:event_jButton_STOPActionPerformed

    private void jTextField_CURRENT_EVENTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_CURRENT_EVENTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_CURRENT_EVENTActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Interface1().setVisible(true));
    }
    
    /*
    
    */
    public void refreshPortalStats()
    {
        java.awt.EventQueue.invokeLater(() -> 
            {
// =============================== PORTAL CONTENT REFRESHER ===============================
                for(int i=0;i<portals.size();i++)
                {
                    enter_portal_models.get(i).clear();
                    exit_portal_models.get(i).clear();
                    CopyOnWriteArrayList<Child> enteringChildren = portals.get(i).getEntering();
                    CopyOnWriteArrayList<Child> leavingChildren = portals.get(i).getLeaving();
                    for(int j=0;j<enteringChildren.size();j++)
                    {
                        enter_portal_models.get(i).add(j, enteringChildren.get(j).getID());
                    }
                    for(int k=0;k<leavingChildren.size();k++)
                    {
                        exit_portal_models.get(i).add(k, leavingChildren.get(k).getID());
                    }
                }
            }
        );
    }
    
    public void refreshCounters()
    {
        java.awt.EventQueue.invokeLater( () ->
            {
                jTextField_BLOODCOUNT.setText(String.valueOf(sz.get(2).getBlood()));
                jTextField_HIVE_CAPTURED.setText(String.valueOf(uz.get(4).getCapturedChildren()));
            }
        );
    }
    public void refreshStats()
    {
        java.awt.EventQueue.invokeLater(() ->
            {
                for(int i=0;i<uz_models_C.size();i++)
                {
                    uz_models_C.get(i).clear();
                    CopyOnWriteArrayList<Child> avail_children = uz.get(i).getAvailChildren();
                    for(int j=0;j<avail_children.size();j++)
                    {
                        uz_models_C.get(i).add(j, avail_children.get(j).getID());
                    }

                }
                
                for(int i=0;i<uz_models_D.size();i++)
                {
                    uz_models_D.get(i).clear();
                    CopyOnWriteArrayList<Demogorgon> avail_demos = uz.get(i).getAvailDemos();
                    for(int j=0; j<avail_demos.size();j++)
                    {
                        uz_models_D.get(i).add(j, avail_demos.get(j).getID());
                    }
                }
                
                for(int i=0;i<sz_models_C.size();i++)
                {
                    sz_models_C.get(i).clear();
                    ArrayList<Child> avail_children = sz.get(i).getAvailChildren();
                    for(int j=0; j<avail_children.size();j++)
                    {
                        sz_models_C.get(i).add(j, avail_children.get(j).getID());
                    }
                }
                
                jTextField_CURRENT_EVENT.setText(em.getStatus());
            }
        );
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton_PLAY;
    private javax.swing.JButton jButton_STOP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel_BAYERS;
    private javax.swing.JLabel jLabel_BLOOD;
    private javax.swing.JLabel jLabel_FOREST;
    private javax.swing.JLabel jLabel_LABORATORY;
    private javax.swing.JLabel jLabel_MAIN_STREET;
    private javax.swing.JLabel jLabel_MALL;
    private javax.swing.JLabel jLabel_PORTALS;
    private javax.swing.JLabel jLabel_SEWERS;
    private javax.swing.JLabel jLabel_WSQK;
    private javax.swing.JList<String> jList_BYERS;
    private javax.swing.JList<String> jList_CHILDREN_FOREST;
    private javax.swing.JList<String> jList_CHILDREN_LAB;
    private javax.swing.JList<String> jList_CHILDREN_MALL;
    private javax.swing.JList<String> jList_CHILDREN_SEWERS;
    private javax.swing.JList<String> jList_DEMOS_FOREST;
    private javax.swing.JList<String> jList_DEMOS_LAB;
    private javax.swing.JList<String> jList_DEMOS_MALL;
    private javax.swing.JList<String> jList_DEMOS_SEWERS;
    private javax.swing.JList<String> jList_ENTER_PORTAL_FOREST;
    private javax.swing.JList<String> jList_ENTER_PORTAL_LAB;
    private javax.swing.JList<String> jList_ENTER_PORTAL_MALL;
    private javax.swing.JList<String> jList_ENTER_PORTAL_SEWER;
    private javax.swing.JList<String> jList_EXIT_PORTAL_FOREST;
    private javax.swing.JList<String> jList_EXIT_PORTAL_LAB;
    private javax.swing.JList<String> jList_EXIT_PORTAL_MALL;
    private javax.swing.JList<String> jList_EXIT_PORTAL_SEWER;
    private javax.swing.JList<String> jList_MAIN_STREET;
    private javax.swing.JList<String> jList_WSQK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane18;
    private javax.swing.JScrollPane jScrollPane19;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane20;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTextField jTextField_BLOODCOUNT;
    private javax.swing.JTextField jTextField_CURRENT_EVENT;
    private javax.swing.JTextField jTextField_HIVE_CAPTURED;
    // End of variables declaration//GEN-END:variables
}
