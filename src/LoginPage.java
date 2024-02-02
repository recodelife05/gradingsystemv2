import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private static int LOGINATTEMPTS = 3;
    private JTextField userNameTextField = new JTextField();
    private JPasswordField passwordTextField   = new JPasswordField();
    private JPanel panel = new JPanel();

    public LoginPage(){
        InitializedComponents();
        SetUILocations();
        AddEvents();
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

    private Boolean AuthenticateUser(){
        String userName = userNameTextField.getText();
        char[] password = passwordTextField.getPassword();
        String pword = new String(password);
        if(userName.equals("admin") &&
           pword.equals("admin123")){
            System.out.println("Login");
            DisplayMainPage();

            return  true;
        }
        System.out.println("Please enter the correct credentials");
        return false;
    }

    public void DisplayMainPage(){
        JOptionPane.showMessageDialog(this, "Login Successful");
        GradingPage gradingPage = new GradingPage();
        gradingPage.show();
        gradingPage.setLocationRelativeTo(null);
        setVisible(false);
    }

    private void AddEvents(){

        loginButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                AuthenticateUser();
            }
        });
    }
}
