import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JDialog {


    private JTextField textField1;
    private JPasswordField passwordField;
    private JButton btnOk;
    private JButton btnCancel;
    private JPanel LoginPanel;
    private JButton registerButton;
    private JComboBox<String> comboBox1;


    public Login (JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(LoginPanel);
        setMinimumSize(new Dimension(600,610));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = textField1.getText();
                String password = String.valueOf(passwordField.getPassword());
                String usertype = (String) comboBox1.getSelectedItem();


                user = getAuthentication(username, password,usertype);

                if(user != null){
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Username or Password Invalid",
                            "Try Again", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();

            }
        });


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openRegistrationForm();
            }
        });
        setVisible(true);
    }
    public User user;
    private User getAuthentication(String username, String password,String usertype){
        User user = null;

        final  String DB_URL =  "jdbc:sqlserver://localhost;database=mystore;"+"encrypt=true;trustServerCertificate=true";
        final String USERNAME = "sa";
        final String PASSWORD = "123";

        try{
            Connection conn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            //cONNECTED SUCCESSFULLY
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM Userstype WHERE Username = ? AND Password = ? AND Usertype = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,usertype);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                user =  new User();
                user.username = resultSet.getString("username");
                user.password = resultSet.getString("password");
            }
            stmt.close();
            conn.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return user;
    }
    private void openRegistrationForm(){
        dispose();
        Registration registration = new Registration(null);
    }
    public static void main(String [] args){
        Login login = new Login(null);
        User user = login.user;
        if(user != null){
            System.out.println("Successfully Authentication of "+user.username);
            System.out.println("        Username:   "+user.username     );
        } else {
            System.out.println("Authenticaiton canceled");
        }
    }


}
