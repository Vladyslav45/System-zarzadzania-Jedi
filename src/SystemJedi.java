import javax.swing.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class SystemJedi extends JFrame {
    private JLabel jLabel;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JLabel jLabel7;
    private JLabel jLabel8;

    private JList zakonList;
    private JList zakonJedilist;
    private JList jediList;

    private JTextField jTextField;
    private JTextField jTextField1;
    private JTextField jTextField2;
    private JTextField jTextField3;

    private JButton wybierzButtonZakon;
    private JButton importButtonZakon;
    private JButton exportButtonZakon;
    private JButton zarejestrujButtonZakon;
    private JButton wyczyscButtonZakon;
    private JButton importButtonJedi;
    private JButton exportButtonJedi;
    private JButton zarejestrujButtonJedi;
    private JButton wyczyscButtonJedi;

    private JComboBox jComboBox;
    private JRadioButton radioButton;
    private JRadioButton radioButton1;
    private ButtonGroup buttonGroup;
    private int idJedi;

    private JSlider jSlider;

    private Connection connection;

    public SystemJedi(){
        setLayout(null);
        jLabel = new JLabel("Zakony Jedi");
        jLabel1 = new JLabel("Jedi");
        jLabel2 = new JLabel("Rejestracja Zakonu Jedi");
        jLabel3 = new JLabel("Rejestracja Jedi");
        jLabel4 = new JLabel("Nazwa: ");
        jLabel5 = new JLabel("Nazwa: ");
        jLabel6 = new JLabel("Kolor miecza: ");
        jLabel7 = new JLabel("Moc: ");
        jLabel8 = new JLabel("Strona mocy: ");

        zakonList = new JList();
        zakonJedilist = new JList<>();
        jediList = new JList<>();

        jTextField = new JTextField();
        jTextField1 = new JTextField();
        jTextField1.setText("zakon.txt");
        jTextField1.setEnabled(false);
        jTextField2 = new JTextField();
        jTextField3 = new JTextField();
        jTextField3.setText("jedi.txt");
        jTextField3.setEnabled(false);

        wybierzButtonZakon = new JButton("Wybierz");
        importButtonZakon = new JButton("Import");
        exportButtonZakon = new JButton("Export");
        zarejestrujButtonZakon = new JButton("Zarejestruj");
        wyczyscButtonZakon = new JButton("Wyczysc");
        importButtonJedi = new JButton("Import");
        exportButtonJedi = new JButton("Export");
        zarejestrujButtonJedi = new JButton("Zarejestruj");
        wyczyscButtonJedi = new JButton("Wyczysc");

        String[] color = {"Green", "Yellow", "Blue", "Violet", "Orange"};
        jComboBox = new JComboBox(color);
        radioButton = new JRadioButton("ciemna");
        radioButton1 = new JRadioButton("jasna");
        buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButton);buttonGroup.add(radioButton1);
        jSlider = new JSlider();

        jLabel.setBounds(150,20, 100, 30);
        zakonList.setBounds(50,50,350,250);
        jLabel2.setBounds(120, 300,150,30);
        jLabel4.setBounds(20,330,70,30);
        jTextField.setBounds(150,335,200,20);
        wybierzButtonZakon.setBounds(20,370,100,20);
        zakonJedilist.setBounds(150, 370, 200,130);
        importButtonZakon.setBounds(20, 490, 100,20);
        jTextField1.setBounds(150,510,200,20);
        exportButtonZakon.setBounds(20,520,100,20);
        zarejestrujButtonZakon.setBounds(150,550,100,20);
        wyczyscButtonZakon.setBounds(260,550,90,20);

        wybierzButtonZakon.addActionListener(e -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT ID_Jedi FROM Jedi WHERE Jedi.name_jedi = ?");
                preparedStatement.setString(1, zakonJedilist.getSelectedValue().toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    idJedi = resultSet.getInt("ID_Jedi");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        });

        zarejestrujButtonZakon.addActionListener(e -> {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO Zakon(Jedi_ID,Nazwa_zakonu) VALUES(?,?)");
                preparedStatement.setInt(1, idJedi);
                preparedStatement.setString(2,jTextField.getText());
                preparedStatement.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            zakons();
        });

        importButtonZakon.addActionListener(e -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("zakon.txt"));
                String line;
                PreparedStatement p = connection.prepareStatement("INSERT INTO Zakon VALUES(?,?,?)");
                while ((line = bufferedReader.readLine()) != null){
                    String[] s = line.split("@");
                    byte[] bytes = Base64.getDecoder().decode(s[0]);
                    byte[] byte1 = Base64.getDecoder().decode(s[1]);
                    byte[] byte2 = Base64.getDecoder().decode(s[2]);
                    String integer = new String(bytes);
                    String integer1 = new String(byte1);
                    String s1 = new String(byte2);
                    p.setInt(1, Integer.parseInt(integer));
                    p.setInt(2, Integer.parseInt(integer1));
                    p.setString(3, s1);
                    p.executeUpdate();
                }
            } catch (IOException | SQLException e1) {
                e1.printStackTrace();
            }
            zakons();
        });


        exportButtonZakon.addActionListener(e -> {
            try {
                FileWriter fileWriter = new FileWriter("zakon.txt", true);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Zakon");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID_Zakon");
                    int idJedi = resultSet.getInt("Jedi_ID");
                    String zakon = resultSet.getString("Nazwa_zakonu");
                    fileWriter.write(Base64.getEncoder().encodeToString(String.valueOf(id).getBytes()) + "@" + Base64.getEncoder().encodeToString(String.valueOf(idJedi).getBytes()) + "@" + Base64.getEncoder().encodeToString(zakon.getBytes()) + "\n");
                }
                fileWriter.close();
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }

        });

        wyczyscButtonZakon.addActionListener(e -> jTextField.setText(null));

        jLabel1.setBounds(650,20,50,30);
        jediList.setBounds(500,50,350,250);
        jLabel3.setBounds(650,300, 100,30);
        jLabel5.setBounds(510,330,70,20);
        jTextField2.setBounds(610,330,200,20);
        jLabel6.setBounds(510, 370, 130,20);
        jComboBox.setBounds(610,370,200,20);
        jLabel7.setBounds(510,400,60,20);
        jSlider.setBounds(610,400,200,20);
        jLabel8.setBounds(610, 430,100,20);
        radioButton.setBounds(690, 430,70,20);
        radioButton1.setBounds(760,430,60,20);
        importButtonJedi.setBounds(510,460, 100,20);
        jTextField3.setBounds(630,480,200,20);
        exportButtonJedi.setBounds(510,490, 100,20);
        zarejestrujButtonJedi.setBounds(630, 520,100,20);
        wyczyscButtonJedi.setBounds(735,520,100,20);

        zarejestrujButtonJedi.addActionListener(e -> {
            try {
                PreparedStatement statement = connection.prepareStatement("INSERT INTO Jedi(name_jedi, Color_sword, Moc, Strona_mocy) VALUES(?,?,?,?)");
                statement.setString(1,jTextField2.getText());
                statement.setInt(3,jSlider.getValue());
                if (radioButton.isSelected()){
                    statement.setString(2, "RED");
                    statement.setString(4, radioButton.getText());
                } else if (radioButton1.isSelected()){
                    statement.setString(2, Objects.requireNonNull(jComboBox.getSelectedItem()).toString());
                    statement.setString(4, radioButton1.getText());
                }
                statement.executeUpdate();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            jedis();
        });

        importButtonJedi.addActionListener(e -> {
            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader("jedi.txt"));
                String line;
                PreparedStatement p = connection.prepareStatement("INSERT INTO Jedi VALUES(?,?,?,?,?)");
                while ((line = bufferedReader.readLine()) != null){
                    String[] s = line.split("@");
                    byte[] bytes = Base64.getDecoder().decode(s[0]);
                    byte[] byte1 = Base64.getDecoder().decode(s[1]);
                    byte[] byte2 = Base64.getDecoder().decode(s[2]);
                    byte[] byte3 = Base64.getDecoder().decode(s[3]);
                    byte[] byte4 = Base64.getDecoder().decode(s[4]);
                    String sw = new String(bytes);
                    String s1 = new String(byte1);
                    String s2 = new String(byte2);
                    String s3 = new String(byte3);
                    String s4 = new String(byte4);
                    p.setInt(1, Integer.parseInt(sw));
                    p.setString(2, s1);
                    p.setString(3, s2);
                    p.setInt(4,Integer.parseInt(s3));
                    p.setString(5, s4);
                    p.executeUpdate();
                }
            } catch (IOException | SQLException e1) {
                e1.printStackTrace();
            }
            jedis();
        });

        exportButtonJedi.addActionListener(e -> {
            try {
                FileWriter fileWriter = new FileWriter("jedi.txt", true);
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Jedi");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int id = resultSet.getInt("ID_Jedi");
                    String name = resultSet.getString("name_jedi");
                    String sword = resultSet.getString("Color_sword");
                    int moc = resultSet.getInt("Moc");
                    String stronaMocy = resultSet.getString("Strona_mocy");
                    fileWriter.write(Base64.getEncoder().encodeToString(String.valueOf(id).getBytes()) + "@" + Base64.getEncoder().encodeToString(name.getBytes()) + "@"+Base64.getEncoder().encodeToString(sword.getBytes()) + "@"+Base64.getEncoder().encodeToString(String.valueOf(moc).getBytes()) + "@" + Base64.getEncoder().encodeToString(stronaMocy.getBytes()) + "\n");
                }
                fileWriter.close();
            } catch (SQLException | IOException e1) {
                e1.printStackTrace();
            }
        });

        wyczyscButtonJedi.addActionListener(e -> jTextField2.setText(null));

        radioButton.addActionListener(e -> jComboBox.setEnabled(false));
        radioButton1.addActionListener(e -> jComboBox.setEnabled(true));
        add(jLabel);
        add(jLabel1);
        add(jLabel2);
        add(jLabel3);
        add(jLabel4);
        add(jLabel5);
        add(jLabel6);
        add(jLabel7);
        add(jLabel8);
        add(zakonList);
        add(zakonJedilist);
        add(jediList);
        add(jTextField);
        add(jTextField1);
        add(jTextField2);
        add(jTextField3);
        add(jComboBox);
        add(radioButton);
        add(radioButton1);
        add(jSlider);
        add(wybierzButtonZakon);
        add(importButtonZakon);
        add(exportButtonZakon);
        add(zarejestrujButtonZakon);
        add(wyczyscButtonZakon);
        add(importButtonJedi);
        add(exportButtonJedi);
        add(zarejestrujButtonJedi);
        add(wyczyscButtonJedi);
    }

    public void jedis(){
        List<String> names = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT name_jedi FROM Jedi");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                names.add(resultSet.getString("name_jedi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jediList.setListData(names.toArray());
        zakonJedilist.setListData(names.toArray());
    }

    public void zakons(){
        List<String> zakons = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("select Zakon.Nazwa_zakonu as nameZakon, Jedi.name_jedi as nameJedi\n" +
                    "From Zakon\n" +
                    "INNER JOIN Jedi ON Zakon.Jedi_ID = Jedi.ID_Jedi;");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                zakons.add(resultSet.getString("nameZakon"));
                zakons.add(resultSet.getString("nameJedi"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        zakonList.setListData(zakons.toArray());
    }

    public void con(){
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost/jedi","postgres","postgres");
            jedis();
            zakons();
        } catch (ClassNotFoundException e) {
            System.out.println("bledny sterownik do bazy danych: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Blad polaczenie z baza danych: " + e.getMessage());
        }
    }
}
