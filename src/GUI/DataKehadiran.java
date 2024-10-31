/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import GUI.KoneksiDtbase;
import com.mysql.jdbc.PreparedStatement;
import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author REZKY
 */
public class DataKehadiran extends javax.swing.JFrame {
private DefaultTableModel model;
KoneksiDtbase dbsetting;
AutoNumber autonum;
String driver, database, user, pass, userlogin;
    /**
     * Creates new form DataKehadiran
     */
    public DataKehadiran() {
        initComponents();
        dbsetting = new KoneksiDtbase();
        driver = dbsetting.SettingPanel("DBDriver");
        database = dbsetting.SettingPanel("DBDatabase");
        user = dbsetting.SettingPanel("DBUsername");
        pass = dbsetting.SettingPanel("DBPassword");

        Object[] Baris = {"ID.Absen", "Tanggal", "NIDN", "Kelas", "NIM", "Ket"};
        model = new DefaultTableModel(null, Baris);
        tabel.setModel(model);
        loadcmbdata();
        loadData();
    }

      public void loadcmbdata() {
           try {
            cbnmdos.removeAllItems();
            Connection c = DriverManager.getConnection(database, user, pass);
            Statement s = c.createStatement();
            String sql = "Select nama from dosen";
            ResultSet r = s.executeQuery(sql);while (r.next()) {
            cbnmdos.addItem(r.getString("nama"));
        }
        r.close();
        s.close();
        c.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        try {
            cbmatkul.removeAllItems();
            Connection c = DriverManager.getConnection(database, user, pass);
            Statement s = c.createStatement();
            String sql = "Select nama_matkul from matkul";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                cbmatkul.addItem(r.getString("nama_matkul"));
            }
            r.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            System.out.println(e);
        }

        try {
            cbkelas.removeAllItems();
            Connection c = DriverManager.getConnection(database, user, pass);
            Statement s = c.createStatement();
            String sql = "Select nmkelas from kelas";
            ResultSet r = s.executeQuery(sql);

            while (r.next()) {
                cbkelas.addItem(r.getString("nmkelas"));
            }
            r.close();
            s.close();
            c.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        
        try{
            cbnmmhs.removeAllItems();
            Connection c = DriverManager.getConnection(database, user, pass);
            Statement s=c.createStatement();
            String sql = "select nama from mahasiswa";
            ResultSet r=s.executeQuery(sql);
            
            while(r.next()){
                cbnmmhs.addItem(r.getString("nama"));
            }
            r.close();
            s.close();
        }catch(SQLException e){
            System.out.println(e);
        }
    
      }
      
      public void loadData() {
    model.getDataVector().removeAllElements();
    model.fireTableDataChanged();
    t_tgl.setDate(new java.util.Date());
    try {
        Connection c = DriverManager.getConnection(database, user, pass);
        Statement s = c.createStatement();
        String sql = "Select * From absen";
        ResultSet r = s.executeQuery(sql);

        while (r.next()) {
            Object[] o = new Object[7];
            o[0] = r.getString("id");
            o[1] = r.getString("tglabsen");
            o[2] = r.getString("nidn");
            o[3] = r.getString("nim");
            o[4] = r.getString("kdmatkul");
            o[5] = r.getString("kelas");
            o[6] = r.getString("ket");

            model.addRow(o);
        }

        r.close();
        s.close();
    } catch (SQLException e) {
        System.out.println("Terjadi kesalahan");
    }
}
      public void TambahData() {
        String IDabsen = this.tID.getText();
        java.util.Date tgl = (java.util.Date) this.t_tgl.getDate();
        String Nidn = this.tnidn.getText();
        String Nim = this.tnim.getText();
        String Kdmatkul = this.tkdmatkul.getText();
        String Kdkelas = this.tkdkelas.getText();
        String Ket = this.cbket.getSelectedItem().toString();
        
        try {
            String sql2 = "Select * From absen Where id='" + IDabsen
                          + "' AND nidn='" + Nidn + "' AND nim='" + Nim
                          + "' AND kdmatkul='" + Kdmatkul + "' AND kelas='"
                          + Kdkelas + "'";
            Connection c = DriverManager.getConnection(database, user, pass);
            PreparedStatement st = (PreparedStatement) c.prepareStatement(sql2);
            ResultSet rs = st.executeQuery(sql2);

            if (rs.next()) {
                String sql = "UPDATE absen Set ket=ket + ? Where idabsen='"
                             + IDabsen + "' AND nidn='" + Nidn + "' AND nim='"
                             + Nim + "' AND kdmatkul='" + Kdmatkul + "' AND kdkelas='"
                             + Kdkelas + "'";
                PreparedStatement p2 = (PreparedStatement) c.prepareStatement(sql);
                p2.setString(1, Ket);
                p2.executeUpdate();
                p2.close();
            } else {
                String sql = "Insert Into absen values(?,?,?,?,?,?,?)";
                PreparedStatement p = (PreparedStatement) c.prepareStatement(sql);
                p.setString(1, IDabsen);
                p.setString(2, Nidn);
                p.setString(3, Nim);
                p.setDate(4, new java.sql.Date(tgl.getTime()));
                p.setString(5, Kdmatkul);
                p.setString(6, Kdkelas);
                p.setString(7, Ket);
                p.executeUpdate();
                p.close();
            }
            c.close();
        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            loadData();
    }
}
     public void HapusText(){
        this.tID.setText(autonum.getAutoNumberInt());
        t_tgl.setDate(new java.util.Date());
    }
    
    public void autonumber() throws SQLException{
        autonum = new AutoNumber();
        Connection connection = KoneksiDtbase.getKoneksi();
        if (connection != null) {
            autonum.setConnection(connection);
            autonum.setTableName("absen");
            tID.setText(autonum.getAutoNumberInt());
        } else {
            System.out.println("Failed to establish connection.");
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
        jLabel1 = new javax.swing.JLabel();
        bBack = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        tID = new javax.swing.JTextField();
        cbnmdos = new javax.swing.JComboBox<>();
        cbkelas = new javax.swing.JComboBox<>();
        cbnmmhs = new javax.swing.JComboBox<>();
        cbket = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        t_tgl = new com.toedter.calendar.JDateChooser();
        tnidn = new javax.swing.JTextField();
        tkdkelas = new javax.swing.JTextField();
        tnim = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        tkdmatkul = new javax.swing.JTextField();
        cbmatkul = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        bProses = new javax.swing.JButton();
        bRefresh = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabel = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Data Kehadiran Mahasiswa");

        bBack.setBackground(new java.awt.Color(255, 255, 255));
        bBack.setText("<=");
        bBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bBack)
                .addGap(189, 189, 189)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(bBack)))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        jLabel2.setText("ID Absen");

        jLabel3.setText("Nama Dosen ");

        jLabel4.setText("Kelas");

        jLabel5.setText("Nama Mahasiswa");

        jLabel6.setText("Keterangan");

        tID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tIDActionPerformed(evt);
            }
        });

        cbnmdos.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbnmdos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbnmdosActionPerformed(evt);
            }
        });

        cbkelas.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbkelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbkelasActionPerformed(evt);
            }
        });

        cbnmmhs.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbnmmhs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbnmmhsActionPerformed(evt);
            }
        });

        cbket.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hadir", "Izin", "Sakit", "Alfa" }));
        cbket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbketActionPerformed(evt);
            }
        });

        jLabel7.setText("Tgl.Absen");

        jLabel8.setText("NIDN");

        jLabel9.setText("Kode");

        jLabel10.setText("NIM");

        tnidn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tnidnActionPerformed(evt);
            }
        });

        tkdkelas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tkdkelasActionPerformed(evt);
            }
        });

        tnim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tnimActionPerformed(evt);
            }
        });

        jLabel11.setText("Mata Kuliah");

        jLabel12.setText("Kode");

        tkdmatkul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tkdmatkulActionPerformed(evt);
            }
        });

        cbmatkul.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbmatkul.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbmatkulActionPerformed(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(0, 153, 153));

        bProses.setText("Prosess");
        bProses.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bProsesActionPerformed(evt);
            }
        });

        bRefresh.setText("Refresh");
        bRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bProses)
                .addGap(38, 38, 38)
                .addComponent(bRefresh)
                .addGap(90, 90, 90))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bProses)
                    .addComponent(bRefresh))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tgl Absen", "NIDN", "Kode Matkul", "Kelas", "NIM", "Keterangan"
            }
        ));
        jScrollPane1.setViewportView(tabel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbnmdos, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbkelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbnmmhs, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbket, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(85, 85, 85)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(t_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tnim, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tnidn)
                                    .addComponent(tkdkelas, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbmatkul, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel12)))
                        .addGap(6, 6, 6)
                        .addComponent(tkdmatkul, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(111, 111, 111)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 602, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel7)))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel3)
                                .addComponent(cbnmdos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(tnidn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel11)
                                .addComponent(cbmatkul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel12)
                                .addComponent(tkdmatkul, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cbkelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)
                            .addComponent(tkdkelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(t_tgl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(cbnmmhs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cbket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(tnim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bBackActionPerformed
        // TODO add your handling code here:
            Menu x = new Menu(); 
            this.setVisible(false);
            x.setVisible(true);
    }//GEN-LAST:event_bBackActionPerformed

    private void cbnmdosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbnmdosActionPerformed
        // TODO add your handling code here:
        int i = cbnmdos.getSelectedIndex();
        if(i == -1){
            return;
        }
        try{
            String kd = (String) cbnmdos.getSelectedItem();
            Connection c=DriverManager.getConnection(database, user, pass);
            Statement s=c.createStatement();
            String sql="Select * from dosen where nama=?";
            PreparedStatement p=(PreparedStatement) c.prepareStatement(sql);
            p.setString(1, kd);
            ResultSet result=p.executeQuery();
            result.next();
            this.tnidn.setText(result.getString("nidn"));
        }catch(SQLException e){
            System.out.println(e);
        }
        
        
    }//GEN-LAST:event_cbnmdosActionPerformed

    private void tIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tIDActionPerformed

    private void tkdmatkulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkdmatkulActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tkdmatkulActionPerformed

    private void cbkelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbkelasActionPerformed
        // TODO add your handling code here:
        int i = cbkelas.getSelectedIndex();
        if(i == -1){
            return;
        }
        try{
            String kd = (String) cbkelas.getSelectedItem();
            Connection c=DriverManager.getConnection(database, user, pass);
            Statement s=c.createStatement();
            String sql="Select * from kelas where nmkelas=?";
            PreparedStatement p=(PreparedStatement) c.prepareStatement(sql);
            p.setString(1, kd);
            ResultSet result=p.executeQuery();
            result.next();
            this.tkdkelas.setText(result.getString("kdkelas"));
        }catch(SQLException e){
            System.out.println(e);
        }
    }//GEN-LAST:event_cbkelasActionPerformed

    private void cbnmmhsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbnmmhsActionPerformed
        // TODO add your handling code here:
        int i = cbnmmhs.getSelectedIndex();
        if(i == -1){
            return;
        }
        try{
            String kd = (String) cbnmmhs.getSelectedItem();
            Connection c=DriverManager.getConnection(database, user, pass);
            Statement s=c.createStatement();
            String sql="Select * from mahasiswa where nama=?";
            PreparedStatement p=(PreparedStatement) c.prepareStatement(sql);
            p.setString(1, kd);
            ResultSet result=p.executeQuery();
            result.next();
            this.tnim.setText(result.getString("nim"));
        }catch(SQLException e){
            System.out.println(e);
        }
    }//GEN-LAST:event_cbnmmhsActionPerformed

    private void cbketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbketActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbketActionPerformed

    private void tnidnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnidnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnidnActionPerformed

    private void tkdkelasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tkdkelasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tkdkelasActionPerformed

    private void tnimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tnimActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tnimActionPerformed

    private void cbmatkulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbmatkulActionPerformed
        // TODO add your handling code here:
        int i = cbmatkul.getSelectedIndex();
        if(i == -1){
            return;
        }
        try{
            String kd = (String) cbmatkul.getSelectedItem();
            Connection c=DriverManager.getConnection(database, user, pass);
            Statement s=c.createStatement();
            String sql="Select * from matkul where nama_matkul=?";
            PreparedStatement p=(PreparedStatement) c.prepareStatement(sql);
            p.setString(1, kd);
            ResultSet result=p.executeQuery();
            result.next();
            this.tnidn.setText(result.getString("kdmatkul"));
        }catch(SQLException e){
            System.out.println(e);
        }
        
    }//GEN-LAST:event_cbmatkulActionPerformed

    private void bRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRefreshActionPerformed
        // TODO add your handling code here:
        HapusText();
        loadData();
    }//GEN-LAST:event_bRefreshActionPerformed

    private void bProsesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bProsesActionPerformed
        // TODO add your handling code here:
        TambahData();
        loadData();
    }//GEN-LAST:event_bProsesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DataKehadiran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DataKehadiran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DataKehadiran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DataKehadiran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DataKehadiran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bBack;
    private javax.swing.JButton bProses;
    private javax.swing.JButton bRefresh;
    private javax.swing.JComboBox<String> cbkelas;
    private javax.swing.JComboBox<String> cbket;
    private javax.swing.JComboBox<String> cbmatkul;
    private javax.swing.JComboBox<String> cbnmdos;
    private javax.swing.JComboBox<String> cbnmmhs;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField tID;
    private com.toedter.calendar.JDateChooser t_tgl;
    private javax.swing.JTable tabel;
    private javax.swing.JTextField tkdkelas;
    private javax.swing.JTextField tkdmatkul;
    private javax.swing.JTextField tnidn;
    private javax.swing.JTextField tnim;
    // End of variables declaration//GEN-END:variables
}
