package com.aprog_lab.aprog_pl.GUI;

import com.aprog_lab.aprog_pl.Network_Connection.RemoteObjectImplementation;
import com.aprog_lab.aprog_pl.events.BlackoutEvent;
import com.aprog_lab.aprog_pl.events.ElevenSavesEvent;
import com.aprog_lab.aprog_pl.events.HiveMindEvent;
import com.aprog_lab.aprog_pl.events.StormEvent;
import com.aprog_lab.aprog_pl.shared_resources.logManager;
import com.aprog_lab.aprog_pl.shared_resources.Portal;
import com.aprog_lab.aprog_pl.shared_resources.Safe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.Unsafe_Zone;
import com.aprog_lab.aprog_pl.shared_resources.VecnaChecker;
import com.aprog_lab.aprog_pl.threads.Child;
import com.aprog_lab.aprog_pl.threads.Demogorgon;
import com.aprog_lab.aprog_pl.threads.EventManager;
import com.aprog_lab.aprog_pl.threads.PortalManager;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import javax.swing.DefaultListModel;

/**
 *
 * @author Emanuel Baciu
 */
public class GUI1_Server extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GUI1_Server.class.getName());
    private Unsafe_Zone Forest, Lab, Mall, Sewer, Hive;
    private Safe_Zone ms, bb, radio;
    private Portal p1, p2, p3, p4;
    private ArrayList<Unsafe_Zone> uz;
    private ArrayList<Safe_Zone> sz;
    private ArrayList<Portal> portals;
    private EventManager em;
    private logManager log;
    
    private ArrayList<DefaultListModel<String>> uz_models_D;
    private ArrayList<DefaultListModel<String>> uz_models_C;
    private ArrayList<DefaultListModel<String>> sz_models_C;
    private ArrayList<DefaultListModel<String>> enter_portal_models;
    private ArrayList<DefaultListModel<String>> exit_portal_models;
    

    /**
     * Creates new form Interface1
     */
    public GUI1_Server()
    {
        initComponents();
        this.setResizable(false);
        this.setLocationRelativeTo(this);
        jTextField_BLOODCOUNT.setText("0");
        jTextField_HIVE_CAPTURED.setText("0");
        jTextField_CURRENT_EVENT.setText("None");
        log = new logManager();
        
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
        p1 = new Portal("ForestPortal", new CyclicBarrier(2), this, log);
        p2 = new Portal("LabPortal", new CyclicBarrier(3), this, log);
        p3 = new Portal("MallPortal", new CyclicBarrier(4), this, log);
        p4 = new Portal("SewerPortal", new CyclicBarrier(2), this, log);
        
        // ===================== PORTAL ADDITION =====================
        portals.add(p1); portals.add(p2); portals.add(p3); portals.add(p4);
        
// ===================== EVENT-RELATED OBJECT INITIALIZATION =====================
        BlackoutEvent be = new BlackoutEvent(portals, uz);
        StormEvent se = new StormEvent();
        ElevenSavesEvent ese = new ElevenSavesEvent(uz.get(4));
        HiveMindEvent hme = new HiveMindEvent(uz);
        
        em = new EventManager(be, se, ese, hme, log);
        em.start();
        
        // ===================== PORTAL MANAGER =====================
        new PortalManager(portals, log).start();

// ===================== VECNA CHECKER INITIALIZATION =====================
        VecnaChecker vc = new VecnaChecker(1, uz, se, ese, hme, log);
        initRMI();
        
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
            System.out.println("Interrupted Exception -> GUI1 constructor");
        }
    }
    
// =============================== PORTAL CONTENT REFRESHER ===============================
    /*
    
    */
    public void refreshPortalStats()
    {
        java.awt.EventQueue.invokeLater(() -> 
            {
                for(int i=0;i<portals.size();i++)
                {
                    enter_portal_models.get(i).clear();
                    exit_portal_models.get(i).clear();
                    Child[] enteringChildrenSnapshot = portals.get(i).getEntering().toArray(new Child[0]); // Changed from CopyOnWriteArrayList to an Array due to chances of updating whilst printing each element
                    Child[] leavingChildrenSnapshot = portals.get(i).getLeaving().toArray(new Child[0]);  // Changed from CopyOnWriteArrayList to an Array due to chances of updating whilst printing each element
                    
                        for(int j=0;j<enteringChildrenSnapshot.length;j++)
                        {
                            enter_portal_models.get(i).add(j, enteringChildrenSnapshot[j].getID());
                        }
                    
                        for(int k=0;k<leavingChildrenSnapshot.length;k++)
                        {
                            exit_portal_models.get(i).add(k, leavingChildrenSnapshot[k].getID());
                        }
                }
            }
        );
    }
// =============================== COUNTER REFRESHER ===============================
    public void refreshCounters()
    {
        java.awt.EventQueue.invokeLater( () ->
            {
                jTextField_BLOODCOUNT.setText(String.valueOf(sz.get(2).getBlood()));
                jTextField_HIVE_CAPTURED.setText(String.valueOf(uz.get(4).getCapturedChildren()));
            }
        );
    }
    
// =============================== PORTAL CONTENT REFRESHER ===============================
    /*
    
    */
    public void refreshZoneStats()
    {
        java.awt.EventQueue.invokeLater(() ->
            {
                for(int i=0;i<uz_models_C.size();i++)
                {
                    uz_models_C.get(i).clear();
                    Child[] avail_childrenSnapshot = uz.get(i).getAvailChildren().toArray(new Child[0]);     // Changed from CopyOnWriteArrayList to an Array due to chances of updating whilst printing each element
                    for(int j=0;j<avail_childrenSnapshot.length;j++)
                    {
                        uz_models_C.get(i).add(j, avail_childrenSnapshot[j].getID());
                    }   
                }
                
                for(int i=0;i<uz_models_D.size();i++)
                {
                    uz_models_D.get(i).clear();
                    Demogorgon[] avail_demosSnapshot = uz.get(i).getAvailDemos().toArray(new Demogorgon[0]); // Changed from CopyOnWriteArrayList to an Array due to chances of updating whilst printing each element
                        for(int j=0; j<avail_demosSnapshot.length;j++)
                        {
                            uz_models_D.get(i).add(j, avail_demosSnapshot[j].getID());
                        }   
                }
                
                for(int i=0;i<sz_models_C.size();i++)
                {
                    sz_models_C.get(i).clear();
                    Child[] avail_childrenSnapshot = sz.get(i).getAvailChildren().toArray(new Child[0]);     // Changed from CopyOnWriteArrayList to an Array due to chances of updating whilst printing each element
                    for(int j=0; j<avail_childrenSnapshot.length;j++)
                    {
                        sz_models_C.get(i).add(j, avail_childrenSnapshot[j].getID());
                    }
                }
                jTextField_CURRENT_EVENT.setText(em.getStatus());
            }
        );
    }
    
    public void initRMI()
    {
        try
        {
            RemoteObjectImplementation obj = new RemoteObjectImplementation(log, uz, em, sz.get(0));
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("//localhost/RemoteObjectCreatedForDemonstration", obj);
            System.out.println("Remote object registered");
        }
        catch(Exception e)
        {
            System.out.println("Error: "+e.getMessage());
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

        jPanel1 = new javax.swing.JPanel();
        jLabel_PORTALS = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_SEWER = new javax.swing.JList<>();
        jScrollPane11 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_SEWER = new javax.swing.JList<>();
        jScrollPane8 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_MALL = new javax.swing.JList<>();
        jScrollPane7 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_MALL = new javax.swing.JList<>();
        jScrollPane6 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_LAB = new javax.swing.JList<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_LAB = new javax.swing.JList<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList_EXIT_PORTAL_FOREST = new javax.swing.JList<>();
        jScrollPane10 = new javax.swing.JScrollPane();
        jList_ENTER_PORTAL_FOREST = new javax.swing.JList<>();
        jScrollPane18 = new javax.swing.JScrollPane();
        jList_DEMOS_SEWERS = new javax.swing.JList<>();
        jScrollPane17 = new javax.swing.JScrollPane();
        jList_CHILDREN_SEWERS = new javax.swing.JList<>();
        jScrollPane20 = new javax.swing.JScrollPane();
        jList_CHILDREN_LAB = new javax.swing.JList<>();
        jScrollPane16 = new javax.swing.JScrollPane();
        jList_DEMOS_MALL = new javax.swing.JList<>();
        jScrollPane13 = new javax.swing.JScrollPane();
        jList_DEMOS_LAB = new javax.swing.JList<>();
        jScrollPane15 = new javax.swing.JScrollPane();
        jList_CHILDREN_MALL = new javax.swing.JList<>();
        jScrollPane19 = new javax.swing.JScrollPane();
        jList_CHILDREN_FOREST = new javax.swing.JList<>();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList_DEMOS_FOREST = new javax.swing.JList<>();
        jLabel_FOREST = new javax.swing.JLabel();
        jLabel_MALL = new javax.swing.JLabel();
        jLabel_LABORATORY = new javax.swing.JLabel();
        jLabel_SEWERS = new javax.swing.JLabel();
        jTextField_CURRENT_EVENT = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField_HIVE_CAPTURED = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel_MAIN_STREET = new javax.swing.JLabel();
        jLabel_BAYERS = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_BYERS = new javax.swing.JList<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_MAIN_STREET = new javax.swing.JList<>();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList_WSQK = new javax.swing.JList<>();
        jTextField_BLOODCOUNT = new javax.swing.JTextField();
        jLabel_BLOOD = new javax.swing.JLabel();
        jLabel_WSQK = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Stranger Things Sim SERVER");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(31, 31, 31));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel_PORTALS.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_PORTALS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_PORTALS.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_PORTALS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_PORTALS.setText("PORTALS");
        jPanel1.add(jLabel_PORTALS, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 70, 71, -1));

        jLabel3.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("UNSAFE ZONES");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 60, 150, -1));

        jLabel4.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("CHILDREN");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 80, 70, 20));

        jLabel5.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("DEMOGORGONS");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 80, 80, 20));

        jList_ENTER_PORTAL_SEWER.setBackground(new java.awt.Color(31, 31, 31));
        jList_ENTER_PORTAL_SEWER.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_SEWER.setForeground(new java.awt.Color(255, 255, 255));
        jList_ENTER_PORTAL_SEWER.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_SEWER.setAutoscrolls(false);
        jScrollPane9.setViewportView(jList_ENTER_PORTAL_SEWER);

        jPanel1.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 430, 76, 100));

        jList_EXIT_PORTAL_SEWER.setBackground(new java.awt.Color(31, 31, 31));
        jList_EXIT_PORTAL_SEWER.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_SEWER.setForeground(new java.awt.Color(255, 255, 255));
        jList_EXIT_PORTAL_SEWER.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane11.setViewportView(jList_EXIT_PORTAL_SEWER);

        jPanel1.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 430, 76, 100));

        jList_EXIT_PORTAL_MALL.setBackground(new java.awt.Color(31, 31, 31));
        jList_EXIT_PORTAL_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_MALL.setForeground(new java.awt.Color(255, 255, 255));
        jList_EXIT_PORTAL_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane8.setViewportView(jList_EXIT_PORTAL_MALL);

        jPanel1.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 320, 76, 100));

        jList_ENTER_PORTAL_MALL.setBackground(new java.awt.Color(31, 31, 31));
        jList_ENTER_PORTAL_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_MALL.setForeground(new java.awt.Color(255, 255, 255));
        jList_ENTER_PORTAL_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_MALL.setAutoscrolls(false);
        jScrollPane7.setViewportView(jList_ENTER_PORTAL_MALL);

        jPanel1.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 320, 76, 100));

        jList_ENTER_PORTAL_LAB.setBackground(new java.awt.Color(31, 31, 31));
        jList_ENTER_PORTAL_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_LAB.setForeground(new java.awt.Color(255, 255, 255));
        jList_ENTER_PORTAL_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_LAB.setAutoscrolls(false);
        jScrollPane6.setViewportView(jList_ENTER_PORTAL_LAB);

        jPanel1.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 210, 76, 100));

        jList_EXIT_PORTAL_LAB.setBackground(new java.awt.Color(31, 31, 31));
        jList_EXIT_PORTAL_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_LAB.setForeground(new java.awt.Color(255, 255, 255));
        jList_EXIT_PORTAL_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane5.setViewportView(jList_EXIT_PORTAL_LAB);

        jPanel1.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 210, 76, 100));

        jList_EXIT_PORTAL_FOREST.setBackground(new java.awt.Color(31, 31, 31));
        jList_EXIT_PORTAL_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_EXIT_PORTAL_FOREST.setForeground(new java.awt.Color(255, 255, 255));
        jList_EXIT_PORTAL_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane4.setViewportView(jList_EXIT_PORTAL_FOREST);

        jPanel1.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 100, 80, 100));

        jList_ENTER_PORTAL_FOREST.setBackground(new java.awt.Color(31, 31, 31));
        jList_ENTER_PORTAL_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_ENTER_PORTAL_FOREST.setForeground(new java.awt.Color(255, 255, 255));
        jList_ENTER_PORTAL_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_ENTER_PORTAL_FOREST.setAutoscrolls(false);
        jScrollPane10.setViewportView(jList_ENTER_PORTAL_FOREST);

        jPanel1.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 100, 80, 100));

        jList_DEMOS_SEWERS.setBackground(new java.awt.Color(31, 31, 31));
        jList_DEMOS_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_SEWERS.setForeground(new java.awt.Color(255, 255, 255));
        jList_DEMOS_SEWERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_SEWERS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane18.setViewportView(jList_DEMOS_SEWERS);

        jPanel1.add(jScrollPane18, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 430, 77, 100));

        jList_CHILDREN_SEWERS.setBackground(new java.awt.Color(31, 31, 31));
        jList_CHILDREN_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_SEWERS.setForeground(new java.awt.Color(255, 255, 255));
        jList_CHILDREN_SEWERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_SEWERS.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane17.setViewportView(jList_CHILDREN_SEWERS);

        jPanel1.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 77, 100));

        jList_CHILDREN_LAB.setBackground(new java.awt.Color(31, 31, 31));
        jList_CHILDREN_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_LAB.setForeground(new java.awt.Color(255, 255, 255));
        jList_CHILDREN_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_LAB.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane20.setViewportView(jList_CHILDREN_LAB);

        jPanel1.add(jScrollPane20, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 320, 76, 101));

        jList_DEMOS_MALL.setBackground(new java.awt.Color(31, 31, 31));
        jList_DEMOS_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_MALL.setForeground(new java.awt.Color(255, 255, 255));
        jList_DEMOS_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_MALL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane16.setViewportView(jList_DEMOS_MALL);

        jPanel1.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 210, 78, 100));

        jList_DEMOS_LAB.setBackground(new java.awt.Color(31, 31, 31));
        jList_DEMOS_LAB.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_LAB.setForeground(new java.awt.Color(255, 255, 255));
        jList_DEMOS_LAB.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_LAB.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane13.setViewportView(jList_DEMOS_LAB);

        jPanel1.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 320, 78, 101));

        jList_CHILDREN_MALL.setBackground(new java.awt.Color(31, 31, 31));
        jList_CHILDREN_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_MALL.setForeground(new java.awt.Color(255, 255, 255));
        jList_CHILDREN_MALL.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_MALL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane15.setViewportView(jList_CHILDREN_MALL);

        jPanel1.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 210, 76, 99));

        jList_CHILDREN_FOREST.setBackground(new java.awt.Color(31, 31, 31));
        jList_CHILDREN_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_CHILDREN_FOREST.setForeground(new java.awt.Color(255, 255, 255));
        jList_CHILDREN_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_CHILDREN_FOREST.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane19.setViewportView(jList_CHILDREN_FOREST);

        jPanel1.add(jScrollPane19, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 100, 76, 98));

        jList_DEMOS_FOREST.setBackground(new java.awt.Color(31, 31, 31));
        jList_DEMOS_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_DEMOS_FOREST.setForeground(new java.awt.Color(255, 255, 255));
        jList_DEMOS_FOREST.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jList_DEMOS_FOREST.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane12.setViewportView(jList_DEMOS_FOREST);

        jPanel1.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 100, 78, 98));

        jLabel_FOREST.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_FOREST.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_FOREST.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_FOREST.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_FOREST.setText("FOREST");
        jPanel1.add(jLabel_FOREST, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 140, -1, -1));

        jLabel_MALL.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_MALL.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_MALL.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_MALL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_MALL.setText("MALL");
        jPanel1.add(jLabel_MALL, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 250, 39, -1));

        jLabel_LABORATORY.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_LABORATORY.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_LABORATORY.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_LABORATORY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_LABORATORY.setText("LAB");
        jPanel1.add(jLabel_LABORATORY, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 360, -1, -1));

        jLabel_SEWERS.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_SEWERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_SEWERS.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_SEWERS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_SEWERS.setText("SEWERS");
        jPanel1.add(jLabel_SEWERS, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 470, 53, -1));

        jTextField_CURRENT_EVENT.setEditable(false);
        jTextField_CURRENT_EVENT.setBackground(new java.awt.Color(31, 31, 31));
        jTextField_CURRENT_EVENT.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jTextField_CURRENT_EVENT.setForeground(new java.awt.Color(255, 255, 255));
        jTextField_CURRENT_EVENT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_CURRENT_EVENT.addActionListener(this::jTextField_CURRENT_EVENTActionPerformed);
        jPanel1.add(jTextField_CURRENT_EVENT, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 330, 120, 80));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CURRENT EVENT");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 310, 120, -1));

        jTextField_HIVE_CAPTURED.setEditable(false);
        jTextField_HIVE_CAPTURED.setBackground(new java.awt.Color(31, 31, 31));
        jTextField_HIVE_CAPTURED.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jTextField_HIVE_CAPTURED.setForeground(new java.awt.Color(255, 255, 255));
        jTextField_HIVE_CAPTURED.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField_HIVE_CAPTURED.addActionListener(this::jTextField_HIVE_CAPTUREDActionPerformed);
        jPanel1.add(jTextField_HIVE_CAPTURED, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 170, 120, 81));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("HIVE CHILDREN");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(850, 150, 120, 20));

        jLabel_MAIN_STREET.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_MAIN_STREET.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_MAIN_STREET.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_MAIN_STREET.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_MAIN_STREET.setText("MAIN STREET");
        jPanel1.add(jLabel_MAIN_STREET, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 135, -1));

        jLabel_BAYERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_BAYERS.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_BAYERS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_BAYERS.setText("BYERS BASEMENT");
        jPanel1.add(jLabel_BAYERS, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 70, 131, -1));

        jList_BYERS.setBackground(new java.awt.Color(31, 31, 31));
        jList_BYERS.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_BYERS.setForeground(new java.awt.Color(255, 255, 255));
        jList_BYERS.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList_BYERS);

        jPanel1.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, 80, 251));

        jList_MAIN_STREET.setBackground(new java.awt.Color(31, 31, 31));
        jList_MAIN_STREET.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_MAIN_STREET.setForeground(new java.awt.Color(255, 255, 255));
        jList_MAIN_STREET.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList_MAIN_STREET);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 80, 251));

        jList_WSQK.setBackground(new java.awt.Color(31, 31, 31));
        jList_WSQK.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 0, 18)); // NOI18N
        jList_WSQK.setForeground(new java.awt.Color(255, 255, 255));
        jList_WSQK.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(jList_WSQK);

        jPanel1.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 390, 81, 155));

        jTextField_BLOODCOUNT.setEditable(false);
        jTextField_BLOODCOUNT.setBackground(new java.awt.Color(31, 31, 31));
        jTextField_BLOODCOUNT.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 48)); // NOI18N
        jTextField_BLOODCOUNT.setForeground(new java.awt.Color(255, 255, 255));
        jTextField_BLOODCOUNT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(jTextField_BLOODCOUNT, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 410, 117, 113));

        jLabel_BLOOD.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_BLOOD.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_BLOOD.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_BLOOD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_BLOOD.setText("BLOOD COUNT");
        jPanel1.add(jLabel_BLOOD, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 390, 117, -1));

        jLabel_WSQK.setBackground(new java.awt.Color(255, 255, 255));
        jLabel_WSQK.setFont(new java.awt.Font("ProggyClean CE Nerd Font", 1, 18)); // NOI18N
        jLabel_WSQK.setForeground(new java.awt.Color(255, 255, 255));
        jLabel_WSQK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_WSQK.setText("RADIO WSQK");
        jPanel1.add(jLabel_WSQK, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, 86, -1));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1020, 590));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField_CURRENT_EVENTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_CURRENT_EVENTActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_CURRENT_EVENTActionPerformed

    private void jTextField_HIVE_CAPTUREDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_HIVE_CAPTUREDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_HIVE_CAPTUREDActionPerformed

    
// ============= MAIN METHOD =============
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
        java.awt.EventQueue.invokeLater(() -> new GUI1_Server().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel1;
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
