/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.swing.JOptionPane;
/**
 *
 * @author REZKY
 */
public class KoneksiDtbase {
    public Properties mypanel, myLanguage;
    private String strNamaPanel;
    
    public String SettingPanel(String nmPanel){
        try{
            mypanel=new Properties();
            mypanel.load(new FileInputStream("src/lib/Dtbase.ini"));
            strNamaPanel=mypanel.getProperty(nmPanel);
        }catch(IOException e){
            JOptionPane.showMessageDialog(null,"Tidak Ada Koneksi","Error",JOptionPane.INFORMATION_MESSAGE);
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return strNamaPanel;
    }

    
     public static final String PathReport = "C:/Users/LENOVO/Documents/NetBeansProjects/Akademik/src/GUI/";
    public static Connection getKoneksi() {
        Connection connection = null;
        try {
            String url = "jdbc:mysql://localhost:3306/akademik";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
