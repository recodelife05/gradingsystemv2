import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        LoginPage loginPage = new LoginPage();

        loginPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPage.setLocationRelativeTo(null);
        loginPage.setVisible(true);
        loginPage.pack();
    }
}