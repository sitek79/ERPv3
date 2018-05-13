package erpv3;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

//import calendar.CalendarTest;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class AppWindow extends javax.swing.JFrame {

    private final String URL = "jdbc:mysql://192.168.0.169:3306/erpdb";
    private final String USERNAME = "erpdbusr";
    final String PASSWORD = "U><er!!!123";
    
    String SQLcreateDB = "CREATE DATABASE IF NOT EXISTS erpdb";
    //String URL;
    //String USERNAME;
    //String PASSWORD;   
    
    
    public AppWindow() {
        initComponents();
        
        //Show_Products_In_JTable();
        Show_Data_In_JTable_mfprinters();
        Show_Data_In_JTable_mf_spare_parts();        
        Show_Data_In_JTable_mfprinters_log();
    }
    
    //String ImgPath = null;
    
    // переменные указывают начальное положение указателя списка в таблицах
    int posPrint = 0;
    int posSpare = 0;
    int posLog = 0;
    
    //соединение с базой MySQL
    /*
    public Connection getConnection() {
        Connection con = null;
        try {
            // 172.24.225.222/erpdb","erpuser", "linAdmin79!!!
            //con = DriverManager.getConnection("jdbc:mysql://172.24.225.222/erpdb","erpuser","linAdmin79!!!");
            con = DriverManager.getConnection("jdbc:mysql://192.168.0.169/erpdb","erpdbusr", "U><er!!!123");
            //Убрал всплывающее окно Connected          
            //JOptionPane.showMessageDialog(null,"Connected");
            
            
            return con;
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
            //Убрал всплывающее окно Not Connected            
            //JOptionPane.showMessageDialog(null,"Not Connected");
                        
            return null;
        }
    }
*/
    // временно закомментирован
    /*
    public void DBMySQL() {
    DBMySQL worker = new DBMySQL();

        //String query = "SELECT * FROM mfprinters";
        try {
            Statement statement = worker.getConnection().createStatement();
            //statement.executeQuery(query);
            JOptionPane.showMessageDialog(null,"Connected");
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,"Not Connected");
        }
    }
   */     
    
    // Проверка полей ввода
    //Printers
    public boolean checkInputsPrinters() {
        if (txt_Printers_device_name.getText() == null
                || txt_Printers_dealer.getText() == null
                || txt_Printers_location.getText() == null
                || txt_Printers_date.getDate() == null) {
            System.out.println("Есть незаполненные обязательные поля");
            return false;}
        else{return true;}
    }
    
    //Parts запчасти
    public boolean checkInputsParts() {
        if (txt_Parts_device_name.getText() == null
                || txt_Parts_dealer.getText() == null
                || txt_Parts_location.getText() == null
                || txt_Parts_date.getDate() == null) {
            System.out.println("Есть незаполненные обязательные поля");
            return false;}
        else{return true;}
    }
    
    //Log протоколирование
    public boolean checkInputsLog() {
        if (txt_Log_action.getText() == null
                || txt_Log_date.getDate() == null
                || txt_Log_count.getText() == null) {
            System.out.println("Есть незаполненные обязательные поля");
            return false;}
        else{return true;}
    }
    //--------------------------------------------------------------------------------------------------------------------------------------
    // Отображение данных из базы в таблице MFP JTable App_mfprinters
    // 1 - Заполнить ArrayList данными
    public ArrayList<App_mfprinters> getPrintersList()
    {
        ArrayList<App_mfprinters> dataPrintersList = new ArrayList<App_mfprinters>();
        DBMySQL worker = new DBMySQL();
        String query = "SELECT * FROM mfprinters";
        try {
            Statement statement = worker.getConnection().createStatement();
            // Поместим результат в переменную
            ResultSet resultSetPrinters = statement.executeQuery(query);            
            // и выведем данные этой переменной
            
            //st = con.createStatement();
            //rs = st.executeQuery(query);
            App_mfprinters printers;
            
            while (resultSetPrinters.next()) {
                printers = new App_mfprinters(resultSetPrinters.getInt("device_id"),resultSetPrinters.getString("device_name"),                        
                        resultSetPrinters.getString("dealer"),resultSetPrinters.getString("location"),resultSetPrinters.getString("date"),
                        resultSetPrinters.getString("state"),resultSetPrinters.getString("toner_cartridge"),resultSetPrinters.getString("drum_cartridge"),
                        resultSetPrinters.getString("roller"),resultSetPrinters.getString("waste_toner_container"),resultSetPrinters.getString("notice"));
                dataPrintersList.add(printers);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dataPrintersList;
    }
    
    // 2 - Заполним JTable
    // Show_Data_In_JTable_mfprinters();    
    public void Show_Data_In_JTable_mfprinters()
    {
        ArrayList<App_mfprinters> list = getPrintersList();
        DefaultTableModel model = (DefaultTableModel)jTable_mfprinters.getModel();
        
        // очистка содержимого таблицы
        model.setRowCount(0);
        Object[] row = new Object[11];
        for(int i = 0; i < list.size(); i++)
        {
            row[0] = list.get(i).getPrintersDevice_id();
            row[1] = list.get(i).getPrintersDevice_name();
            row[2] = list.get(i).getPrintersDealer();
            row[3] = list.get(i).getPrintersLocation();
            row[4] = list.get(i).getPrintersDate();
            row[5] = list.get(i).getPrintersState();
            row[6] = list.get(i).getPrintersCartridge();
            row[7] = list.get(i).getPrintersDrum();
            row[8] = list.get(i).getPrintersRoller();
            row[9] = list.get(i).getPrintersWasteToner();
            row[10] = list.get(i).getPrintersNotice();           
            
            model.addRow(row);
        }        
    }    
    //Показать данные в полях для ввода текста при перемещении курсора спомощью кнопок. Таблица MFP (Printers)
    public void ShowItemPrinters(int index) {
            txt_Printers_device_id.setText(Integer.toString(getPrintersList().get(index).getPrintersDevice_id()));
            txt_Printers_device_name.setText(getPrintersList().get(index).getPrintersDevice_name());
            txt_Printers_dealer.setText(getPrintersList().get(index).getPrintersDealer());
            txt_Printers_location.setText(getPrintersList().get(index).getPrintersLocation());            
            txt_Printers_state.setText(getPrintersList().get(index).getPrintersState());
            txt_Printers_toner_cartridge.setText(getPrintersList().get(index).getPrintersCartridge());
            txt_Printers_drum_cartridge.setText(getPrintersList().get(index).getPrintersDrum());
            txt_Printers_roller.setText(getPrintersList().get(index).getPrintersRoller());
            txt_Printers_waste_toner.setText(getPrintersList().get(index).getPrintersWasteToner());
            txt_Printers_notice.setText(getPrintersList().get(index).getPrintersNotice());
        try {            
            Date addDate = null;
            addDate = new SimpleDateFormat("dd-MM-yyyy").parse((String)getPrintersList().get(index).getPrintersDate());
            txt_Printers_date.setDate(addDate);
        } catch (ParseException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }               
    }
    //----------------------------------------------------------------------------------------------------------------------------------
    // Отображение данных из базы в таблице MFP JTable App_mf_spare_parts
    // 1 - Заполнить ArrayList данными
    
    public ArrayList<App_mf_spare_parts> getPartsList() {
        ArrayList<App_mf_spare_parts> dataPartsList = new ArrayList<App_mf_spare_parts>();
        DBMySQL worker = new DBMySQL();
        String query = "SELECT * FROM mf_spare_parts";        

        try {
            Statement statement = worker.getConnection().createStatement();
            // Поместим результат в переменную
            ResultSet resultSetParts = statement.executeQuery(query);            
            // и выведем данные этой переменной
            App_mf_spare_parts parts;
            
            while(resultSetParts.next())
            {
                parts = new App_mf_spare_parts(resultSetParts.getInt("device_id"),resultSetParts.getString("device_name"),                        
                        resultSetParts.getString("dealer"),resultSetParts.getString("location"),resultSetParts.getString("date"),
                        resultSetParts.getString("state"),resultSetParts.getString("type"),resultSetParts.getString("notice"));                        
                dataPartsList.add(parts);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dataPartsList;
    }
    
    // 2 - Заполним JTable
    //Show_Data_In_JTable_mf_spare_parts();    
    public void Show_Data_In_JTable_mf_spare_parts() {
        ArrayList<App_mf_spare_parts> list = getPartsList();
        DefaultTableModel model = (DefaultTableModel)jTable_mf_spare_parts.getModel();
        
        // очистка содержимого таблицы
        model.setRowCount(0);
        Object[] row = new Object[8];
        for(int i = 0; i < list.size(); i++)
        {
            row[0] = list.get(i).getPartDevice_id();
            row[1] = list.get(i).getPartDevice_name();
            row[2] = list.get(i).getPartDealer();
            row[3] = list.get(i).getPartLocation();
            row[4] = list.get(i).getPartDate();
            row[5] = list.get(i).getPartState();
            row[6] = list.get(i).getPartType();
            row[7] = list.get(i).getPartNotice();                      
            
            model.addRow(row);
        }        
    }    
    //Показать данные в полях для ввода текста при перемещении курсора спомощью кнопок. Таблица MFP (Printers)
    //txt_Parts_device_id, txt_Parts_device_name, txt_Parts_dealer, txt_Parts_location, txt_Parts_date, txt_Parts_state, txt_Parts_type, txt_Parts_notice
    public void ShowItemParts(int index) {
            txt_Parts_device_id.setText(Integer.toString(getPartsList().get(index).getPartDevice_id()));
            txt_Parts_device_name.setText(getPartsList().get(index).getPartDevice_name());
            txt_Parts_dealer.setText(getPartsList().get(index).getPartDealer());
            txt_Parts_location.setText(getPartsList().get(index).getPartLocation());            
            txt_Parts_state.setText(getPartsList().get(index).getPartState());
            txt_Parts_type.setText(getPartsList().get(index).getPartType());
            txt_Parts_notice.setText(getPartsList().get(index).getPartNotice());            
        try {            
            Date addDate = null;
            addDate = new SimpleDateFormat("dd-MM-yyyy").parse((String)getPartsList().get(index).getPartDate());
            txt_Parts_date.setDate(addDate);
        } catch (ParseException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }               
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------
    // Отображение данных из базы в таблице MFP JTable App_mfprinters_log
    // 1 - Заполнить ArrayList данными
    
    public ArrayList<App_mfprinters_log> getPrintersLogList()
    {
        ArrayList<App_mfprinters_log> dataPrintersLogList = new ArrayList<App_mfprinters_log>();
        DBMySQL worker = new DBMySQL();
        String query = "SELECT * FROM mfprinters_log";
                    
        try {
            Statement statement = worker.getConnection().createStatement();
            // Поместим результат в переменную
            ResultSet resultSetLog = statement.executeQuery(query);            
            // и выведем данные этой переменной
            App_mfprinters_log printersLogList;
            
            while(resultSetLog.next())
            {
                printersLogList = new App_mfprinters_log(resultSetLog.getInt("action_id"),resultSetLog.getString("action"),                        
                        resultSetLog.getString("dealer"),resultSetLog.getString("date"),resultSetLog.getString("toner_cartridge"),resultSetLog.getString("drum_cartridge"),
                        resultSetLog.getString("roller"),resultSetLog.getString("waste_toner_container"),resultSetLog.getInt("count"),
                        resultSetLog.getString("notice"));
                dataPrintersLogList.add(printersLogList);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return dataPrintersLogList;
    }
    // 2 - Populate The JTable
    //Show_Data_In_JTable_mfprinters_log();    
    public void Show_Data_In_JTable_mfprinters_log() {
        ArrayList<App_mfprinters_log> list = getPrintersLogList();
        DefaultTableModel model = (DefaultTableModel)jTable_mfprinters_log.getModel();
        
        // очистка содержимого таблицы
        model.setRowCount(0);
        Object[] row = new Object[11];
        for(int i = 0; i < list.size(); i++)
        {
            row[0] = list.get(i).getLogActionId();
            row[1] = list.get(i).getLogAction();
            row[2] = list.get(i).getLogDealer();
            row[3] = list.get(i).getLogDate();
            row[4] = list.get(i).getLogToner();
            row[5] = list.get(i).getLogDrum();
            row[6] = list.get(i).getLogRoller();
            row[7] = list.get(i).getLogWaste_toner();
            row[8] = list.get(i).getLogCount();
            row[9] = list.get(i).getLogNotice();                       
            
            model.addRow(row);
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
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        buttonOptions = new java.awt.Button();
        buttonCreateDatabase = new java.awt.Button();
        buttonCreateTable = new java.awt.Button();
        buttonLoadSQLDump = new java.awt.Button();
        jPanel3 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable_mfprinters = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        txt_Printers_device_id = new javax.swing.JTextField();
        txt_Printers_device_name = new javax.swing.JTextField();
        txt_Printers_dealer = new javax.swing.JTextField();
        txt_Printers_location = new javax.swing.JTextField();
        txt_Printers_state = new javax.swing.JTextField();
        txt_Printers_drum_cartridge = new javax.swing.JTextField();
        txt_Printers_roller = new javax.swing.JTextField();
        txt_Printers_toner_cartridge = new javax.swing.JTextField();
        txt_Printers_notice = new javax.swing.JTextField();
        txt_Printers_waste_toner = new javax.swing.JTextField();
        txt_Printers_date = new com.toedter.calendar.JDateChooser();
        btn_Printers_insert = new javax.swing.JButton();
        btn_Printers_refresh = new javax.swing.JButton();
        btn_Printers_delete = new javax.swing.JButton();
        btn_Printers_next = new javax.swing.JButton();
        btn_Printers_first = new javax.swing.JButton();
        btn_Printers_back = new javax.swing.JButton();
        btn_Printers_last = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_mf_spare_parts = new javax.swing.JTable();
        jLabel30 = new javax.swing.JLabel();
        txt_Parts_device_id = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txt_Parts_device_name = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_Parts_dealer = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txt_Parts_location = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txt_Parts_date = new com.toedter.calendar.JDateChooser();
        jLabel16 = new javax.swing.JLabel();
        txt_Parts_state = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txt_Parts_type = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        txt_Parts_notice = new javax.swing.JTextField();
        btn_Parts_delete = new javax.swing.JButton();
        btn_Parts_insert = new javax.swing.JButton();
        btn_Parts_first = new javax.swing.JButton();
        btn_Parts_next = new javax.swing.JButton();
        btn_Parts_back = new javax.swing.JButton();
        btn_Parts_last = new javax.swing.JButton();
        btn_Parts_refresh = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable_mfprinters_log = new javax.swing.JTable();
        jLabel19 = new javax.swing.JLabel();
        txt_Log_action_id = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        txt_Log_action = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txt_Log_dealer = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        txt_Log_date = new com.toedter.calendar.JDateChooser();
        jLabel23 = new javax.swing.JLabel();
        txt_Log_toner = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txt_Log_drum = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        txt_Log_roller = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        txt_Log_waste = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        txt_Log_count = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        txt_Log_notice = new javax.swing.JTextField();
        btn_Log_insert = new javax.swing.JButton();
        btn_Log_last = new javax.swing.JButton();
        btn_Log_next = new javax.swing.JButton();
        btn_Log_back = new javax.swing.JButton();
        btn_Log_first = new javax.swing.JButton();
        btn_Log_delete = new javax.swing.JButton();
        btn_Log_refresh = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable_analytics = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        canvas1 = new java.awt.Canvas();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1280, 800));
        setResizable(false);

        jPanel1.setAlignmentX(0.1F);
        jPanel1.setAlignmentY(0.1F);
        jPanel1.setAutoscrolls(true);
        jPanel1.setName(""); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1280, 724));

        jPanel2.setToolTipText("Tool bar");
        jPanel2.setAlignmentX(0.1F);
        jPanel2.setAlignmentY(0.1F);
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(300, 43));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setToolTipText("Панель инструментов");
        jToolBar1.setFont(new java.awt.Font("Comic Sans MS", 0, 10)); // NOI18N

        buttonOptions.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        buttonOptions.setLabel("Настройки");
        buttonOptions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOptionsActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonOptions);

        buttonCreateDatabase.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        buttonCreateDatabase.setLabel("Создать Базу данных");
        buttonCreateDatabase.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCreateDatabaseActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonCreateDatabase);

        buttonCreateTable.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        buttonCreateTable.setLabel("Создать таблицы");
        buttonCreateTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonCreateTableActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonCreateTable);
        buttonCreateTable.getAccessibleContext().setAccessibleName("Создать таблицы");
        buttonCreateTable.getAccessibleContext().setAccessibleDescription("Создать таблицы");

        buttonLoadSQLDump.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        buttonLoadSQLDump.setLabel("Загрузить SQL дамп");
        buttonLoadSQLDump.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonLoadSQLDumpActionPerformed(evt);
            }
        });
        jToolBar1.add(buttonLoadSQLDump);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jToolBar1, gridBagConstraints);

        jPanel3.setAlignmentX(0.1F);
        jPanel3.setAlignmentY(0.1F);

        jTabbedPane1.setAlignmentX(0.1F);
        jTabbedPane1.setAlignmentY(0.1F);
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(800, 587));
        jTabbedPane1.setName("mfp"); // NOI18N
        jTabbedPane1.setPreferredSize(new java.awt.Dimension(900, 966));

        jPanel4.setAlignmentX(0.1F);
        jPanel4.setAlignmentY(0.1F);
        jPanel4.setName("mfp"); // NOI18N
        jPanel4.setLayout(new java.awt.GridBagLayout());

        jScrollPane1.setAlignmentX(0.1F);
        jScrollPane1.setAlignmentY(0.1F);

        jTable_mfprinters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id прибора", "название прибора", "поставщик", "расположение", "дата", "состояние", "тонер картридж", "барабан", "ролик", "контейнер отраб.", "комментарий"
            }
        ));
        jTable_mfprinters.setAlignmentX(0.1F);
        jTable_mfprinters.setAlignmentY(0.1F);
        jTable_mfprinters.setName(""); // NOI18N
        jTable_mfprinters.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_mfprintersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable_mfprinters);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1582;
        gridBagConstraints.ipady = 342;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane1, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Название:");
        jLabel1.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 38, 0, 0);
        jPanel4.add(jLabel1, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setText(" Расположение:");
        jLabel2.setToolTipText("");
        jLabel2.setAlignmentX(0.1F);
        jLabel2.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        jPanel4.add(jLabel2, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setText("Ролик:");
        jLabel3.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 69, 0, 0);
        jPanel4.add(jLabel3, gridBagConstraints);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel4.setText("Дата:");
        jLabel4.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 64, 0, 0);
        jPanel4.add(jLabel4, gridBagConstraints);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setText("       Состояние:");
        jLabel5.setToolTipText("");
        jLabel5.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        jPanel4.add(jLabel5, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setText("Тонер:");
        jLabel6.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(19, 68, 0, 0);
        jPanel4.add(jLabel6, gridBagConstraints);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setText("Барабан:");
        jLabel7.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 58, 0, 0);
        jPanel4.add(jLabel7, gridBagConstraints);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setText("Поставщик:");
        jLabel8.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 29, 0, 0);
        jPanel4.add(jLabel8, gridBagConstraints);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel9.setText("Бункер:");
        jLabel9.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 63, 0, 0);
        jPanel4.add(jLabel9, gridBagConstraints);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel10.setText("Комментарий:");
        jLabel10.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 26, 0, 0);
        jPanel4.add(jLabel10, gridBagConstraints);

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel29.setText("ID устройства:");
        jLabel29.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(19, 10, 0, 0);
        jPanel4.add(jLabel29, gridBagConstraints);

        txt_Printers_device_id.setAlignmentX(0.1F);
        txt_Printers_device_id.setAlignmentY(0.1F);
        txt_Printers_device_id.setEnabled(false);
        txt_Printers_device_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Printers_device_idActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.insets = new java.awt.Insets(17, 3, 0, 0);
        jPanel4.add(txt_Printers_device_id, gridBagConstraints);

        txt_Printers_device_name.setAlignmentX(0.1F);
        txt_Printers_device_name.setAlignmentY(0.1F);
        txt_Printers_device_name.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Printers_device_nameActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel4.add(txt_Printers_device_name, gridBagConstraints);

        txt_Printers_dealer.setAlignmentX(0.1F);
        txt_Printers_dealer.setAlignmentY(0.1F);
        txt_Printers_dealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Printers_dealerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel4.add(txt_Printers_dealer, gridBagConstraints);

        txt_Printers_location.setAlignmentX(0.1F);
        txt_Printers_location.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel4.add(txt_Printers_location, gridBagConstraints);

        txt_Printers_state.setAlignmentX(0.1F);
        txt_Printers_state.setAlignmentY(0.1F);
        txt_Printers_state.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Printers_stateActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.ABOVE_BASELINE_LEADING;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel4.add(txt_Printers_state, gridBagConstraints);

        txt_Printers_drum_cartridge.setAlignmentX(0.1F);
        txt_Printers_drum_cartridge.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel4.add(txt_Printers_drum_cartridge, gridBagConstraints);

        txt_Printers_roller.setAlignmentX(0.1F);
        txt_Printers_roller.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel4.add(txt_Printers_roller, gridBagConstraints);

        txt_Printers_toner_cartridge.setAlignmentX(0.1F);
        txt_Printers_toner_cartridge.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.insets = new java.awt.Insets(17, 0, 0, 0);
        jPanel4.add(txt_Printers_toner_cartridge, gridBagConstraints);

        txt_Printers_notice.setAlignmentX(0.1F);
        txt_Printers_notice.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 0, 0, 0);
        jPanel4.add(txt_Printers_notice, gridBagConstraints);

        txt_Printers_waste_toner.setAlignmentX(0.1F);
        txt_Printers_waste_toner.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 9;
        gridBagConstraints.gridy = 11;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 0, 0, 0);
        jPanel4.add(txt_Printers_waste_toner, gridBagConstraints);

        txt_Printers_date.setAlignmentX(0.1F);
        txt_Printers_date.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 109;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 4, 0, 0);
        jPanel4.add(txt_Printers_date, gridBagConstraints);

        btn_Printers_insert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/check.png"))); // NOI18N
        btn_Printers_insert.setText("Вставить");
        btn_Printers_insert.setAlignmentY(0.1F);
        btn_Printers_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_insertActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel4.add(btn_Printers_insert, gridBagConstraints);

        btn_Printers_refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/refresh_22.png"))); // NOI18N
        btn_Printers_refresh.setText("Обновить");
        btn_Printers_refresh.setAlignmentY(0.1F);
        btn_Printers_refresh.setName(""); // NOI18N
        btn_Printers_refresh.setPreferredSize(new java.awt.Dimension(109, 29));
        btn_Printers_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_refreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        jPanel4.add(btn_Printers_refresh, gridBagConstraints);

        btn_Printers_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/delete_18.png"))); // NOI18N
        btn_Printers_delete.setText("Удалить");
        btn_Printers_delete.setAlignmentY(0.1F);
        btn_Printers_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_deleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 11;
        gridBagConstraints.gridy = 14;
        gridBagConstraints.ipadx = 10;
        jPanel4.add(btn_Printers_delete, gridBagConstraints);

        btn_Printers_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/next_20.png"))); // NOI18N
        btn_Printers_next.setText("Следующий");
        btn_Printers_next.setAlignmentY(0.1F);
        btn_Printers_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_nextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.ipadx = 6;
        jPanel4.add(btn_Printers_next, gridBagConstraints);

        btn_Printers_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/first_20.png"))); // NOI18N
        btn_Printers_first.setText("Вначало");
        btn_Printers_first.setAlignmentY(0.1F);
        btn_Printers_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_firstActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 7, 3, 7);
        jPanel4.add(btn_Printers_first, gridBagConstraints);

        btn_Printers_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/back_20.png"))); // NOI18N
        btn_Printers_back.setText("Предыдущий");
        btn_Printers_back.setAlignmentY(0.1F);
        btn_Printers_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_backActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 11;
        jPanel4.add(btn_Printers_back, gridBagConstraints);

        btn_Printers_last.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/last_20.png"))); // NOI18N
        btn_Printers_last.setText("Вконец");
        btn_Printers_last.setAlignmentY(0.1F);
        btn_Printers_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Printers_lastActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 12;
        gridBagConstraints.gridy = 13;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 0, 0);
        jPanel4.add(btn_Printers_last, gridBagConstraints);

        jTabbedPane1.addTab("MFP", jPanel4);

        jPanel5.setAlignmentX(0.1F);
        jPanel5.setAlignmentY(0.1F);
        jPanel5.setName("mfp_parts"); // NOI18N
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jScrollPane2.setAlignmentX(0.1F);
        jScrollPane2.setAlignmentY(0.1F);

        jTable_mf_spare_parts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id прибора", "название прибора", "поставщик", "расположение", "дата", "состояние", "тип", "комментарий"
            }
        ));
        jTable_mf_spare_parts.setAlignmentX(0.1F);
        jTable_mf_spare_parts.setAlignmentY(0.1F);
        jTable_mf_spare_parts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable_mf_spare_partsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTable_mf_spare_parts);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1242;
        gridBagConstraints.ipady = 410;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel5.add(jScrollPane2, gridBagConstraints);

        jLabel30.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel30.setText("ID детали:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 45, 0, 0);
        jPanel5.add(jLabel30, gridBagConstraints);

        txt_Parts_device_id.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 10, 0, 0);
        jPanel5.add(txt_Parts_device_id, gridBagConstraints);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel11.setText("Наименование:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 18, 0, 0);
        jPanel5.add(jLabel11, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel5.add(txt_Parts_device_name, gridBagConstraints);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel12.setText("Поставщик:");
        jLabel12.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 39, 0, 0);
        jPanel5.add(jLabel12, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel5.add(txt_Parts_dealer, gridBagConstraints);

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setText("Расположение:");
        jLabel13.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 17, 0, 0);
        jPanel5.add(jLabel13, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel5.add(txt_Parts_location, gridBagConstraints);

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Дата:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 75, 0, 0);
        jPanel5.add(jLabel14, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 9;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.ipadx = 119;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 3, 0);
        jPanel5.add(txt_Parts_date, gridBagConstraints);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setText("Состояние:");
        jLabel16.setToolTipText("");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(20, 49, 0, 0);
        jPanel5.add(jLabel16, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(18, 10, 0, 0);
        jPanel5.add(txt_Parts_state, gridBagConstraints);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Тип:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 83, 0, 0);
        jPanel5.add(jLabel17, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel5.add(txt_Parts_type, gridBagConstraints);

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel18.setText("Комментарий:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 28, 0, 0);
        jPanel5.add(jLabel18, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 323;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 10, 0, 0);
        jPanel5.add(txt_Parts_notice, gridBagConstraints);

        btn_Parts_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/delete_18.png"))); // NOI18N
        btn_Parts_delete.setText("Удалить");
        btn_Parts_delete.setAlignmentY(0.1F);
        btn_Parts_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_deleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_delete, gridBagConstraints);

        btn_Parts_insert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/check.png"))); // NOI18N
        btn_Parts_insert.setText("Вставить");
        btn_Parts_insert.setAlignmentY(0.1F);
        btn_Parts_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_insertActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_insert, gridBagConstraints);

        btn_Parts_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/first_20.png"))); // NOI18N
        btn_Parts_first.setText("Вначало");
        btn_Parts_first.setAlignmentY(0.1F);
        btn_Parts_first.setPreferredSize(new java.awt.Dimension(125, 29));
        btn_Parts_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_firstActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_first, gridBagConstraints);

        btn_Parts_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/next_20.png"))); // NOI18N
        btn_Parts_next.setText("Следующий");
        btn_Parts_next.setAlignmentY(0.1F);
        btn_Parts_next.setPreferredSize(new java.awt.Dimension(125, 29));
        btn_Parts_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_nextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_next, gridBagConstraints);

        btn_Parts_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/back_20.png"))); // NOI18N
        btn_Parts_back.setText("Предыдущий");
        btn_Parts_back.setAlignmentY(0.1F);
        btn_Parts_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_backActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_back, gridBagConstraints);

        btn_Parts_last.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/last_20.png"))); // NOI18N
        btn_Parts_last.setText("Вконец");
        btn_Parts_last.setAlignmentY(0.1F);
        btn_Parts_last.setPreferredSize(new java.awt.Dimension(125, 29));
        btn_Parts_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_lastActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_last, gridBagConstraints);

        btn_Parts_refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/refresh_22.png"))); // NOI18N
        btn_Parts_refresh.setText("Обновить");
        btn_Parts_refresh.setAlignmentY(0.1F);
        btn_Parts_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Parts_refreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel5.add(btn_Parts_refresh, gridBagConstraints);

        jTabbedPane1.addTab("Запчасти", jPanel5);

        jPanel6.setAlignmentX(0.1F);
        jPanel6.setAlignmentY(0.1F);
        jPanel6.setName("mfp_log"); // NOI18N
        jPanel6.setLayout(new java.awt.GridBagLayout());

        jScrollPane3.setAlignmentX(0.1F);
        jScrollPane3.setAlignmentY(0.1F);

        jTable_mfprinters_log.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id операции", "операция", "поставщик", "дата", "тонер картридж", "барабан", "ролик", "контейнер отраб.", "количество", "комментарий"
            }
        ));
        jTable_mfprinters_log.setAlignmentX(0.1F);
        jTable_mfprinters_log.setAlignmentY(0.1F);
        jScrollPane3.setViewportView(jTable_mfprinters_log);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 9;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1242;
        gridBagConstraints.ipady = 390;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jScrollPane3, gridBagConstraints);

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setText("ID операции:");
        jLabel19.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 10, 0, 0);
        jPanel6.add(jLabel19, gridBagConstraints);

        txt_Log_action_id.setEnabled(false);
        txt_Log_action_id.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Log_action_idActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        jPanel6.add(txt_Log_action_id, gridBagConstraints);

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setText("Операция:");
        jLabel20.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 24, 0, 0);
        jPanel6.add(jLabel20, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 0, 0);
        jPanel6.add(txt_Log_action, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel21.setText("Поставщик:");
        jLabel21.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 20, 0, 0);
        jPanel6.add(jLabel21, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 0, 0);
        jPanel6.add(txt_Log_dealer, gridBagConstraints);

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setText("Дата:");
        jLabel22.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(4, 55, 0, 0);
        jPanel6.add(jLabel22, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 160;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 10, 0, 0);
        jPanel6.add(txt_Log_date, gridBagConstraints);

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel23.setText("Тонер:");
        jLabel23.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 45, 0, 0);
        jPanel6.add(jLabel23, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(1, 10, 0, 0);
        jPanel6.add(txt_Log_toner, gridBagConstraints);

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setText("Барабан:");
        jLabel24.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 37, 0, 0);
        jPanel6.add(jLabel24, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel6.add(txt_Log_drum, gridBagConstraints);

        jLabel25.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel25.setText("Ролик:");
        jLabel25.setToolTipText("");
        jLabel25.setAlignmentY(0.1F);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 49, 0, 0);
        jPanel6.add(jLabel25, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel6.add(txt_Log_roller, gridBagConstraints);

        jLabel26.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel26.setText("Бункер:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 43, 0, 0);
        jPanel6.add(jLabel26, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel6.add(txt_Log_waste, gridBagConstraints);

        jLabel27.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel27.setText("Количество:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 16, 0, 0);
        jPanel6.add(jLabel27, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(3, 4, 0, 0);
        jPanel6.add(txt_Log_count, gridBagConstraints);

        jLabel28.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel28.setText("Комментарий:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(8, 7, 0, 0);
        jPanel6.add(jLabel28, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 10;
        gridBagConstraints.ipadx = 302;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(6, 4, 0, 0);
        jPanel6.add(txt_Log_notice, gridBagConstraints);

        btn_Log_insert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/check.png"))); // NOI18N
        btn_Log_insert.setText("Вставить");
        btn_Log_insert.setAlignmentY(0.1F);
        btn_Log_insert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_insertActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_insert, gridBagConstraints);

        btn_Log_last.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/last_20.png"))); // NOI18N
        btn_Log_last.setText("Вконец");
        btn_Log_last.setAlignmentY(0.1F);
        btn_Log_last.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_lastActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_last, gridBagConstraints);

        btn_Log_next.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/next_20.png"))); // NOI18N
        btn_Log_next.setText("Следующий");
        btn_Log_next.setAlignmentY(0.1F);
        btn_Log_next.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_nextActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_next, gridBagConstraints);

        btn_Log_back.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/back_20.png"))); // NOI18N
        btn_Log_back.setText("Предыдущий");
        btn_Log_back.setAlignmentY(0.1F);
        btn_Log_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_backActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_back, gridBagConstraints);

        btn_Log_first.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/first_20.png"))); // NOI18N
        btn_Log_first.setText("Вначало");
        btn_Log_first.setAlignmentY(0.1F);
        btn_Log_first.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_firstActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_first, gridBagConstraints);

        btn_Log_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/delete_18.png"))); // NOI18N
        btn_Log_delete.setText("Удалить");
        btn_Log_delete.setAlignmentY(0.1F);
        btn_Log_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_deleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 8;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_delete, gridBagConstraints);

        btn_Log_refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pict/icons/refresh_22.png"))); // NOI18N
        btn_Log_refresh.setText("Обновить");
        btn_Log_refresh.setAlignmentY(0.1F);
        btn_Log_refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Log_refreshActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 6;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 14, 0, 13);
        jPanel6.add(btn_Log_refresh, gridBagConstraints);

        jTabbedPane1.addTab("Протокол обслуживания", jPanel6);

        jTable_analytics.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "1", "2", "3", "4"
            }
        ));
        jScrollPane4.setViewportView(jTable_analytics);

        jButton2.setText("Детали");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Картриджи");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Заправка");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Экспорт");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(941, 941, 941)
                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
                .addGap(122, 122, 122))
            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 476, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Аналитический срез", jPanel8);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1270, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 610, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 97, Short.MAX_VALUE))
        );

        java.awt.GridBagLayout jPanel7Layout = new java.awt.GridBagLayout();
        jPanel7Layout.columnWidths = new int[] {0};
        jPanel7Layout.rowHeights = new int[] {0};
        jPanel7.setLayout(jPanel7Layout);

        canvas1.setBackground(new java.awt.Color(255, 51, 51));
        canvas1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        canvas1.setName("DB"); // NOI18N
        canvas1.setPreferredSize(new java.awt.Dimension(50, 25));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel7.add(canvas1, gridBagConstraints);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("Файл");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Сервис");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 714, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable_mf_spare_partsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_mf_spare_partsMouseClicked
        // обработка события клика мыши по строкам таблицы. Таблица Запчасти (Parts)
        int index = jTable_mf_spare_parts.getSelectedRow();        
        ShowItemParts(index);
    }//GEN-LAST:event_jTable_mf_spare_partsMouseClicked

    private void txt_Log_action_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Log_action_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Log_action_idActionPerformed

    private void btn_Printers_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_insertActionPerformed
        // обработчик кнопки Вставить        
        String INSERTQUERY = "INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) values(?,?,?,?,?,?,?,?,?,?)";
               
        
        if(checkInputsPrinters() && txt_Printers_device_id.getText() != null) {
            try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            PreparedStatement preparedStatement = connection.prepareStatement(INSERTQUERY);
            // чтобы вставить что-то вместо вопросительных знаков в запросе существует ряд методов set и тип данных
            preparedStatement.setString(1, txt_Printers_device_name.getText());
            preparedStatement.setString(2, txt_Printers_dealer.getText());
            preparedStatement.setString(3, txt_Printers_location.getText());
            // преобразуем формат даты из формы               
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String addDate = dateFormat.format(txt_Printers_date.getDate());
            preparedStatement.setString(4, addDate);            
            //preparedStatement.setString(4, "08-05-2018");
            
            preparedStatement.setString(5, txt_Printers_state.getText());
            preparedStatement.setString(6, txt_Printers_toner_cartridge.getText());
            preparedStatement.setString(7, txt_Printers_drum_cartridge.getText());
            preparedStatement.setString(8, txt_Printers_roller.getText());
            preparedStatement.setString(9, txt_Printers_waste_toner.getText());
            preparedStatement.setString(10, txt_Printers_notice.getText());
            
            // и выполним запрос
            preparedStatement.execute();
            
            System.out.println("Connection => "+connection);
            System.out.println("Prepared statements => "+preparedStatement);
            
            Show_Data_In_JTable_mfprinters();                
                
            JOptionPane.showMessageDialog(null, "Данные добавлены");
                
            } catch (SQLException e){
                e.printStackTrace();
                //JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else{
            JOptionPane.showMessageDialog(null, "Некоторые поля пустые");
        }
    
        /*       
                
        System.out.println("Название => "+txt_Printers_device_name.getText());
        System.out.println("Поставщик => "+txt_Printers_dealer.getText());
        System.out.println("Локация => "+txt_Printers_location.getText());
        System.out.println("Дата => "+txt_Printers_date.getDate());
        System.out.println("Состояние => "+txt_Printers_state.getText());
        System.out.println("Тонер => "+txt_Printers_toner_cartridge.getText());
        System.out.println("Барабан => "+txt_Printers_drum_cartridge.getText());
        System.out.println("Ролик => "+txt_Printers_roller.getText());
        System.out.println("Бункер => "+txt_Printers_roller.getText());
        System.out.println("Комментарий => "+txt_Printers_notice.getText());
        
        System.out.println("checkInputsPrinters => "+checkInputsPrinters());
        */

    }//GEN-LAST:event_btn_Printers_insertActionPerformed

    private void btn_Printers_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_refreshActionPerformed
        // Обработчик кнопки Обновить (Update). Таблица MFP (Printers)
        //String INSERTQUERY = "INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) values(?,?,?,?,?,?,?,?,?,?)";
        String UPDATEQUERY = "UPDATE mfprinters SET device_name=?, dealer=?, location=?, date=?, state=?, toner_cartridge=?, drum_cartridge=?, roller=?, waste_toner_container=?, notice=? WHERE device_id=?";
        
        //Connection connection = null;
        //создадим переменную
        //PreparedStatement preparedStatement = null;
        if(checkInputsPrinters() && txt_Printers_device_id.getText() != null) {
            try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);            
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATEQUERY);
            // чтобы вставить что-то вместо вопросительных знаков в запросе существует ряд методов set и тип данных
            preparedStatement.setString(1, txt_Printers_device_name.getText());
            preparedStatement.setString(2, txt_Printers_dealer.getText());
            preparedStatement.setString(3, txt_Printers_location.getText());
            // преобразуем формат даты из формы               
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String addDate = dateFormat.format(txt_Printers_date.getDate());
            preparedStatement.setString(4, addDate);            
            //preparedStatement.setString(4, "08-05-2018");
            
            preparedStatement.setString(5, txt_Printers_state.getText());
            preparedStatement.setString(6, txt_Printers_toner_cartridge.getText());
            preparedStatement.setString(7, txt_Printers_drum_cartridge.getText());
            preparedStatement.setString(8, txt_Printers_roller.getText());
            preparedStatement.setString(9, txt_Printers_waste_toner.getText());
            preparedStatement.setString(10, txt_Printers_notice.getText());
            
            preparedStatement.setInt(11, Integer.parseInt(txt_Printers_device_id.getText()));
            
            // и выполним запрос
            preparedStatement.execute();
            
            System.out.println("Connection => "+connection);
            System.out.println("Prepared statements => "+preparedStatement);
            
            Show_Data_In_JTable_mfprinters();                
                
            JOptionPane.showMessageDialog(null, "Данные обновлены");
            } catch (SQLException e){
                e.printStackTrace();
                //JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else{
            JOptionPane.showMessageDialog(null, "Некоторые поля пустые");
        }
    }//GEN-LAST:event_btn_Printers_refreshActionPerformed

    private void btn_Printers_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_deleteActionPerformed
        // Обработчик кнопки Удалить (Delete). Таблица MFP (Printers)

    //public static final String INSERTQUERY = "INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) values(?,?,?,?,?,?,?,?,?,?)";
    //public static final String SELECTALL = "SELECT * FROM mfprinters";
    String DELETE = "DELETE FROM mfprinters WHERE device_id=?";
        
        //Connection connection = null;
        //создадим переменную
        //PreparedStatement preparedStatement = null;
        
        if(!txt_Printers_device_id.getText().equals(""))
        {

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);            
            int idPrint = Integer.parseInt(txt_Printers_device_id.getText());                    
            preparedStatement.setInt(1, idPrint);        
            preparedStatement.executeUpdate();
            // отобразить информацию из базы в таблице
            Show_Data_In_JTable_mfprinters();
            
            JOptionPane.showMessageDialog(null, "Запись удалена");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Запись не удалена");
        }
        
        }else{
            JOptionPane.showMessageDialog(null, "Запись не удалена: Нет ID продукта");
        }
        
    }//GEN-LAST:event_btn_Printers_deleteActionPerformed

    private void txt_Printers_stateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Printers_stateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Printers_stateActionPerformed

    private void txt_Printers_device_nameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Printers_device_nameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Printers_device_nameActionPerformed

    private void txt_Printers_device_idActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Printers_device_idActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Printers_device_idActionPerformed

    private void txt_Printers_dealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Printers_dealerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_Printers_dealerActionPerformed

    private void jTable_mfprintersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable_mfprintersMouseClicked
        // обработка события клика мыши по строкам таблицы. Таблица MFP (Printers)
        int index = jTable_mfprinters.getSelectedRow();
        ShowItemPrinters(index);
        
    }//GEN-LAST:event_jTable_mfprintersMouseClicked

    private void btn_Printers_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_firstActionPerformed
        // обработчик кнопки Вначало. Таблица MFP (Printers)
        posPrint = 0;
        ShowItemPrinters(posPrint);
    }//GEN-LAST:event_btn_Printers_firstActionPerformed

    private void btn_Printers_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_lastActionPerformed
        // обработчик кнопки Вконец. Таблица MFP (Printers)
        posPrint = getPrintersList().size()-1;
        ShowItemPrinters(posPrint);
        
    }//GEN-LAST:event_btn_Printers_lastActionPerformed

    private void btn_Printers_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_nextActionPerformed
        // обработчик кнопки Следующий. Таблица MFP (Printers)        
        posPrint++;
        if(posPrint >= getPrintersList().size())
        {
            posPrint = getPrintersList().size()-1;
        }
        ShowItemPrinters(posPrint);
    }//GEN-LAST:event_btn_Printers_nextActionPerformed

    private void btn_Printers_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Printers_backActionPerformed
        // TODO add your handling code here:
        posPrint--;
        if(posPrint < 0)
        {
            posPrint = 0;            
        }
        ShowItemPrinters(posPrint);
    }//GEN-LAST:event_btn_Printers_backActionPerformed

    private void buttonCreateTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateTableActionPerformed
        // Кнопка Создать Таблицы
        
        DBMySQL worker = new DBMySQL();
        //String query = "SELECT * FROM mfprinters_log";     
        
        try {
            Statement statement = worker.getConnection().createStatement();
            
            //ResultSet resultSetLog = statement.executeQuery(query);
            // теперь как выполняется пакетная обработка
            //statement.addBatch("INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) VALUES('Xerox','XCOM-SHOP','Buh','12-05-2018','turn off','108R457345','108D457345','108ROL457345','108BUN457345','In my otpusk period');");
            //statement.addBatch("INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) VALUES('Xerox','XCOM-SHOP','Buh','12-05-2018','turn off','108R457345','108D457345','108ROL457345','108BUN457345','In my otpusk period');");
            //statement.addBatch("INSERT INTO mfprinters(device_name, dealer, location, date, state, toner_cartridge, drum_cartridge, roller, waste_toner_container, notice) VALUES('Xerox','XCOM-SHOP','Buh','12-05-2018','turn off','108R457345','108D457345','108ROL457345','108BUN457345','In my otpusk period');");
            statement.addBatch("CREATE TABLE IF NOT EXISTS `erpdb`.`mfprinters_log` (\n" +
                                "  `action_id` INT(8) NOT NULL AUTO_INCREMENT,\n" +
                                "  `action` VARCHAR(20) NOT NULL,\n" +
                                "  `dealer` VARCHAR(45) NOT NULL,\n" +
                                "  `date` VARCHAR(20) NOT NULL,\n" +
                                "  `toner_cartridge` VARCHAR(45) NOT NULL,\n" +
                                "  `drum_cartridge` VARCHAR(45) NOT NULL,\n" +
                                "  `roller` VARCHAR(45) NOT NULL,\n" +
                                "  `waste_toner_container` VARCHAR(45) NOT NULL,\n" +
                                "  `count` INT(3) NOT NULL,\n" +
                                "  `notice` VARCHAR(45) NOT NULL,\n" +
                                "  PRIMARY KEY (`action_id`))\n" +
                                "ENGINE=InnoDB DEFAULT CHARACTER SET = utf8 , COLLATE = utf8_general_ci;");
            // теперь выполним Batch
            statement.executeBatch();
            // если нужно очистить Batch существует метод Clear
            statement.clearBatch();

            // проверить закрыт ли наш statement
            boolean status = statement.isClosed();
            System.out.println(status);

            // следующий метод getConnection. С помощью него мы получаем соединение
            //statement.getConnection();

            // закрыть соединение вручную. Но так как он в try catch, то закрывать его не обязательно
            statement.close();
            
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
    }//GEN-LAST:event_buttonCreateTableActionPerformed

    private void btn_Parts_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_deleteActionPerformed
        // обработчик кнопки Удалить. Таблица Запчасти    
        String DELETE = "DELETE FROM mf_spare_parts WHERE device_id=?";        
        //Connection connection = null;
        //создадим переменную
        //PreparedStatement preparedStatement = null;
        
        if(!txt_Parts_device_id.getText().equals(""))
        {

        try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE);            
            int idPrint = Integer.parseInt(txt_Parts_device_id.getText());                    
            preparedStatement.setInt(1, idPrint);        
            preparedStatement.executeUpdate();
            // отобразить информацию из базы в таблице        
            Show_Data_In_JTable_mf_spare_parts();
            
            JOptionPane.showMessageDialog(null, "Запись удалена");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Запись не удалена");
        }
        
        }else{
            JOptionPane.showMessageDialog(null, "Запись не удалена: Нет ID продукта");
        }
        
    }//GEN-LAST:event_btn_Parts_deleteActionPerformed

    private void btn_Parts_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_insertActionPerformed
        // обработчик кнопки Вставить. Таблица Запчасти
        //device_id, device_name, dealer, location, date, state, type, notice
        //txt_Parts_device_id, txt_Parts_device_name, txt_Parts_dealer, txt_Parts_location, txt_Parts_date, txt_Parts_state, txt_Parts_type, txt_Parts_notice
        String INSERTQUERY = "INSERT INTO mf_spare_parts(device_name, dealer, location, date, state, type, notice) values(?,?,?,?,?,?,?)";
        //Connection connection = null;
        //создадим переменную
        //PreparedStatement preparedStatement = null;
        if(checkInputsParts() && txt_Parts_device_id.getText() != null) {
            try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERTQUERY);
            // чтобы вставить что-то вместо вопросительных знаков в запросе существует ряд методов set и тип данных
            preparedStatement.setString(1, txt_Parts_device_name.getText());
            preparedStatement.setString(2, txt_Parts_dealer.getText());
            preparedStatement.setString(3, txt_Parts_location.getText());
            // преобразуем формат даты из формы               
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String addDate = dateFormat.format(txt_Parts_date.getDate());
            preparedStatement.setString(4, addDate);            
            //preparedStatement.setString(4, "08-05-2018");
            
            preparedStatement.setString(5, txt_Parts_state.getText());            
            preparedStatement.setString(6, txt_Parts_type.getText());
            preparedStatement.setString(7, txt_Parts_notice.getText());            
            
            // и выполним запрос
            preparedStatement.execute();
            
            System.out.println("Connection => "+connection);
            System.out.println("Prepared statements => "+preparedStatement);            
            
            Show_Data_In_JTable_mf_spare_parts();
                
            JOptionPane.showMessageDialog(null, "Данные добавлены");
                
            } catch (SQLException e){
                e.printStackTrace();
                //JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else{
            JOptionPane.showMessageDialog(null, "Некоторые поля пустые");
        }
        
    }//GEN-LAST:event_btn_Parts_insertActionPerformed

    private void btn_Parts_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_firstActionPerformed
        // обработчик кнопки Вначало. Таблица Запчасти
        posSpare = 0;
        ShowItemParts(posSpare);
    }//GEN-LAST:event_btn_Parts_firstActionPerformed

    private void btn_Parts_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_nextActionPerformed
        // обработчик кнопки Вперед. Таблица Запчасти
        posSpare++;
        if(posSpare >= getPartsList().size())
        {
            posSpare = getPartsList().size()-1;
        }
        ShowItemParts(posSpare);        
    }//GEN-LAST:event_btn_Parts_nextActionPerformed

    private void btn_Parts_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_backActionPerformed
        // обработчик кнопки Назад. Таблица Запчасти
        posSpare--;
        if(posSpare < 0)
        {
            posSpare = 0;            
        }
        ShowItemParts(posSpare);
        
    }//GEN-LAST:event_btn_Parts_backActionPerformed

    private void btn_Parts_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_lastActionPerformed
        // обработчик кнопки Вконец. Таблица Запчасти        
        posSpare = getPartsList().size()-1;
        ShowItemParts(posSpare);
        
    }//GEN-LAST:event_btn_Parts_lastActionPerformed

    private void btn_Parts_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Parts_refreshActionPerformed
        // обработчик кнопки Обновить. Таблица Запчасти
        String UPDATEQUERY = "UPDATE mf_spare_parts SET device_name=?, dealer=?, location=?, date=?, state=?, type=?, notice=? WHERE device_id=?";
        //device_id, device_name, dealer, location, date, state, type, notice
        //Connection connection = null;
        //создадим переменную
        //PreparedStatement preparedStatement = null;
        if(checkInputsParts() && txt_Parts_device_id.getText() != null) {
            try {
            Driver driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);            
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATEQUERY);
            // чтобы вставить что-то вместо вопросительных знаков в запросе существует ряд методов set и тип данных
            preparedStatement.setString(1, txt_Parts_device_name.getText());
            preparedStatement.setString(2, txt_Parts_dealer.getText());
            preparedStatement.setString(3, txt_Parts_location.getText());
            // преобразуем формат даты из формы               
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String addDate = dateFormat.format(txt_Parts_date.getDate());
            preparedStatement.setString(4, addDate);            
            //preparedStatement.setString(4, "08-05-2018");
            
            preparedStatement.setString(5, txt_Parts_state.getText());
            preparedStatement.setString(6, txt_Parts_type.getText());
            preparedStatement.setString(7, txt_Parts_notice.getText());            
            
            preparedStatement.setInt(8, Integer.parseInt(txt_Parts_device_id.getText()));
            
            // и выполним запрос
            preparedStatement.execute();
            
            System.out.println("Connection => "+connection);
            System.out.println("Prepared statements => "+preparedStatement);            
            
            Show_Data_In_JTable_mf_spare_parts();
                
            JOptionPane.showMessageDialog(null, "Данные обновлены");
            } catch (SQLException e){
                e.printStackTrace();
                //JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        } else{
            JOptionPane.showMessageDialog(null, "Некоторые поля пустые");
        }
        
        
    }//GEN-LAST:event_btn_Parts_refreshActionPerformed

    private void btn_Log_insertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_insertActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_insertActionPerformed

    private void btn_Log_lastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_lastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_lastActionPerformed

    private void btn_Log_nextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_nextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_nextActionPerformed

    private void btn_Log_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_backActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_backActionPerformed

    private void btn_Log_firstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_firstActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_firstActionPerformed

    private void btn_Log_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_deleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_deleteActionPerformed

    private void btn_Log_refreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Log_refreshActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_Log_refreshActionPerformed

    private void buttonLoadSQLDumpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonLoadSQLDumpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buttonLoadSQLDumpActionPerformed

    private void buttonCreateDatabaseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonCreateDatabaseActionPerformed
        // Кнопка создать базу данных    
        DBMySQL worker = new DBMySQL();

	try {
            Statement statement = worker.getConnection().createStatement();
            statement.executeUpdate(SQLcreateDB);
            System.out.println(statement);
            
            statement.close();            
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_buttonCreateDatabaseActionPerformed

    private void buttonOptionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOptionsActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null,"Открывается диалог настроек");
        String result = JOptionPane.showInputDialog(
                        this,
                        "<html><h3>Конфигуратор");
                JOptionPane.showInputDialog(
                        this,
                        "Параметр", result);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
    }//GEN-LAST:event_buttonOptionsActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Аналитический срез. Детали.        
        String SQLcreateViewParts = "CREATE OR REPLACE VIEW view_mf_spare_parts AS SELECT device_id, device_name, dealer FROM erpdb.mfprinters";

        DBMySQL worker = new DBMySQL();

	try {
            Statement statement = worker.getConnection().createStatement();
            statement.executeUpdate(SQLcreateViewParts);
            System.out.println(statement);
            
            statement.close();            
            
        } catch (SQLException ex) {
            Logger.getLogger(AppWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Аналитический срез. Картриджи.
        
        
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Аналитический срез. Заправка.
        
        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Аналитический срез. Экспорт.
        
        
    }//GEN-LAST:event_jButton5ActionPerformed

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
            java.util.logging.Logger.getLogger(AppWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AppWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AppWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AppWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AppWindow().setVisible(true);
                //сообщение в консоли.
                System.out.println("Отображается форма");
            }
        });
        // выведем в консоль календарик. мне удобно
        //CalendarTest cal = new CalendarTest();
        //cal.calendarTest();
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Log_back;
    private javax.swing.JButton btn_Log_delete;
    private javax.swing.JButton btn_Log_first;
    private javax.swing.JButton btn_Log_insert;
    private javax.swing.JButton btn_Log_last;
    private javax.swing.JButton btn_Log_next;
    private javax.swing.JButton btn_Log_refresh;
    private javax.swing.JButton btn_Parts_back;
    private javax.swing.JButton btn_Parts_delete;
    private javax.swing.JButton btn_Parts_first;
    private javax.swing.JButton btn_Parts_insert;
    private javax.swing.JButton btn_Parts_last;
    private javax.swing.JButton btn_Parts_next;
    private javax.swing.JButton btn_Parts_refresh;
    private javax.swing.JButton btn_Printers_back;
    private javax.swing.JButton btn_Printers_delete;
    private javax.swing.JButton btn_Printers_first;
    private javax.swing.JButton btn_Printers_insert;
    private javax.swing.JButton btn_Printers_last;
    private javax.swing.JButton btn_Printers_next;
    private javax.swing.JButton btn_Printers_refresh;
    private java.awt.Button buttonCreateDatabase;
    private java.awt.Button buttonCreateTable;
    private java.awt.Button buttonLoadSQLDump;
    private java.awt.Button buttonOptions;
    private java.awt.Canvas canvas1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable_analytics;
    private javax.swing.JTable jTable_mf_spare_parts;
    private javax.swing.JTable jTable_mfprinters;
    private javax.swing.JTable jTable_mfprinters_log;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txt_Log_action;
    private javax.swing.JTextField txt_Log_action_id;
    private javax.swing.JTextField txt_Log_count;
    private com.toedter.calendar.JDateChooser txt_Log_date;
    private javax.swing.JTextField txt_Log_dealer;
    private javax.swing.JTextField txt_Log_drum;
    private javax.swing.JTextField txt_Log_notice;
    private javax.swing.JTextField txt_Log_roller;
    private javax.swing.JTextField txt_Log_toner;
    private javax.swing.JTextField txt_Log_waste;
    private com.toedter.calendar.JDateChooser txt_Parts_date;
    private javax.swing.JTextField txt_Parts_dealer;
    private javax.swing.JTextField txt_Parts_device_id;
    private javax.swing.JTextField txt_Parts_device_name;
    private javax.swing.JTextField txt_Parts_location;
    private javax.swing.JTextField txt_Parts_notice;
    private javax.swing.JTextField txt_Parts_state;
    private javax.swing.JTextField txt_Parts_type;
    private com.toedter.calendar.JDateChooser txt_Printers_date;
    private javax.swing.JTextField txt_Printers_dealer;
    private javax.swing.JTextField txt_Printers_device_id;
    private javax.swing.JTextField txt_Printers_device_name;
    private javax.swing.JTextField txt_Printers_drum_cartridge;
    private javax.swing.JTextField txt_Printers_location;
    private javax.swing.JTextField txt_Printers_notice;
    private javax.swing.JTextField txt_Printers_roller;
    private javax.swing.JTextField txt_Printers_state;
    private javax.swing.JTextField txt_Printers_toner_cartridge;
    private javax.swing.JTextField txt_Printers_waste_toner;
    // End of variables declaration//GEN-END:variables
}
