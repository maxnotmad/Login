import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Registration extends JDialog {
    private JTextField usrnameField;
    private JPasswordField passwrdField;
    private JPasswordField cfirmpasswrdField;
    private JButton registerButton;
    private JButton cancelButton;
    private JPanel RegisterPanel;

    public Registration(JFrame parent){
        super(parent);
        setTitle("Create a new account");
        setContentPane(RegisterPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setResizable(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                returnLoginForm();
            }
        });
        setVisible(true);
    }

    private void registerUser() {
        String username = usrnameField.getText();
        String password = String.valueOf(passwrdField.getPassword());
        String confirmpassword = String.valueOf(cfirmpasswrdField.getPassword());

        //Kiểm tra các Fields chưa được điền
        if(username.isEmpty() || password.isEmpty() ){
            JOptionPane.showMessageDialog(this, "Please enter all fields","Try Again"
            ,JOptionPane.ERROR_MESSAGE);
            return;
        }
        //Kiểm tra đúng mất khẩu
        if(!password.equals(confirmpassword)){
            JOptionPane.showMessageDialog(this,"Confirm password does not match", "Try Again"
            ,JOptionPane.ERROR_MESSAGE);
            return ;
        }

        user =   addUsertoDB(username, password,"user");
        if(user  != null){
            dispose();
        }
        else{
            JOptionPane.showMessageDialog(this,"Failed to register new user","Try Again"
            ,JOptionPane.ERROR_MESSAGE);
        }

    }

    public User user;

    private User addUsertoDB(String username, String password,String usertype) {
        User user = null;
        final  String DB_URL =  "jdbc:sqlserver://localhost;database=mystore;"+"encrypt=true;trustServerCertificate=true";
        final String USERNAME = "sa";
        final String PASSWORD = "123";

        try{
             Connection connection = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            //NẾU KẾT NỐI ĐƯỢC CƠ SỞ DỮ LIỆU....
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO Userstype(username,password,usertype)"+"VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,usertype);


            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0){
                user = new User();
                user.username = username;
                user.password = password;
            }
            stmt.close();
            connection.close();
        } catch(Exception e){
            e.printStackTrace();
        }

        return user;
    }
    private void returnLoginForm(){
        dispose();
        Login login = new Login(null);
    }

    public static void main (String [] args){

        Registration registration = new Registration(null);
        User user = registration.user;
                if(user != null){
                    System.out.println("Successfully registration of: "+user.username);
                } else {
                    System.out.println("Registration failed !");
                }
    }
}
