import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownServiceException;
import java.util.ArrayList;

public class LoginPage extends JFrame {
    private int LOGINATTEMPTS = 3;
    private JTextField userNameTextField = new JTextField();
    private JPasswordField passwordTextField   = new JPasswordField();
    private JPanel panel = new JPanel();

    ArrayList<User> users = new ArrayList<>();

    String CurrentUser;

    public LoginPage(){
        InitializedComponents();
        SetUILocations();
        AddEvents();
        LoadUsers();
    }
    JButton loginButton = new JButton("Login");
    private void InitializedComponents(){
        JLabel usernameLabel = new JLabel ("Username");
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(usernameLabel);
        panel.add(userNameTextField);
        panel.add(passwordLabel);
        panel.add(passwordTextField);
        panel.add(loginButton);
        add(panel);
    }
    private void SetUILocations(){

        this.setMinimumSize(new Dimension(100,600));
        userNameTextField.setColumns(8);
        userNameTextField.setAlignmentY(30.00f);
        userNameTextField.setAlignmentX(30.00f);
        passwordTextField.setColumns(8);
    }
    protected boolean IsAuth = false;

    private Boolean AuthenticateUser() throws URISyntaxException, IOException {
        String userName = userNameTextField.getText();
        char[] password = passwordTextField.getPassword();
        String pword = new String(password);
        if(GetUser(userName,pword)) {
            CurrentUser = userName;
            System.out.println("Login");
            DisplayMainPage();
            IsAuth = true;
            return IsAuth;
        }else {
            LOGINATTEMPTS = LOGINATTEMPTS - 1;
            if(LOGINATTEMPTS > 0){
                JOptionPane.showMessageDialog(this, "Login Failed. You Have" + LOGINATTEMPTS + "attempts left.");
            } else {
                    JOptionPane.showMessageDialog(this, "Login attempts exceeded. Account Locked");
                    System.exit(0);
            }
        }

        System.out.println("Please enter the correct credentials");
        return false;
    }

    public  Boolean GetUser(String user,String pwd){
        var userExist = users.stream().filter(r->r.UserName.equals(user) && r.Password.equals(pwd)).count();
        return userExist > 0 ? true : false;
    }
    public  void LoadUsers(){

        User admin = new User();
        admin.Id = 1;
        admin.Password = "admin123";
        admin.UserName = "admin";
        admin.FirstName = "Admin";
        admin.LastName = "Last Name";
        User user1 = new User();
        user1.Id = 1;
        user1.UserName = "user";
        user1.Password = "user123";
        user1.LastName = "Last Name";
        user1.FirstName = "User";

        users.add(admin);
        users.add(user1);

    }
    public class User {
        public int Id;
        public String UserName;
        public String Password;

        public String FirstName;
        public String LastName;
    }

    public void DisplayMainPage() throws URISyntaxException, IOException {
        JOptionPane.showMessageDialog(this, "Login Successful");
        GradingPage gradingPage = new GradingPage(CurrentUser);
        gradingPage.show();
        gradingPage.setLocationRelativeTo(null);
        setVisible(false);
    }

    private void AddEvents(){
        loginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    AuthenticateUser();
                } catch (URISyntaxException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
