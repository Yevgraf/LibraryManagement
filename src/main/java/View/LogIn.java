/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 *
 * @author franc
 */

public class LogIn extends JFrame implements ActionListener {
    private JLabel labelUsuario, labelSenha;
    private JTextField campoUsuario;
    private JPasswordField campoSenha;
    private JButton botaoLogin;

    public LogIn() {
        setTitle("LogIn");
        setSize(300, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        labelUsuario = new JLabel("Utilizador:");
        campoUsuario = new JTextField(10);

        labelSenha = new JLabel("Password:");
        campoSenha = new JPasswordField(10);

        botaoLogin = new JButton("LogIn");
        botaoLogin.addActionListener(this);

        JPanel painel = new JPanel(new GridLayout(3, 1));
        painel.add(labelUsuario);
        painel.add(campoUsuario);
        painel.add(labelSenha);
        painel.add(campoSenha);
        painel.add(botaoLogin);

        add(painel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == botaoLogin) {
            String usuario = campoUsuario.getText();
            String senha = new String(campoSenha.getPassword());
            if (usuario.equals("admin") && senha.equals("1234")) {
                JOptionPane.showMessageDialog(null, "Login realizado com sucesso!");
            } else {
                JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos!");
            }
        }
    }

    public static void main(String[] args) {
        LogIn tela = new LogIn();
    }
}
