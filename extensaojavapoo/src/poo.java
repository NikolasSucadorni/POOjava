import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataEntryAppDatabase {

    public static void main(String[] args) {
        //Criando o frame
        JFrame frame = new JFrame("Data Entry");
        frame.setSize(300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(4, 2));

        // Criando o componente e campos
        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("Age:");
        JTextField ageField = new JTextField();
        JLabel cityLabel = new JLabel("City:");
        JTextField cityField = new JTextField();

        // Botão
        JButton saveButton = new JButton("Save");

        // Componentes para preenchimento
        frame.add(nameLabel);
        frame.add(nameField);
        frame.add(ageLabel);
        frame.add(ageField);
        frame.add(cityLabel);
        frame.add(cityField);
        frame.add(new JLabel());  // Campo vazio pra espaçamento
        frame.add(saveButton);

        // Adicionando função ao botão salvar
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Pegando valores dos dados de texto
                String name = nameField.getText();
                String age = ageField.getText();
                String city = cityField.getText();

                // INSERT no SQL
                try (Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db")) {
                    String sql = "INSERT INTO users (name, age, city) VALUES (?, ?, ?)";
                    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setInt(2, Integer.parseInt(age));
                        pstmt.setString(3, city);
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Data saved successfully!");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error saving data: " + ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid age format");
                }
            }
        });

        // Set the frame visibility
        frame.setVisible(true);

        // iniciando database
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:mydatabase.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, age INTEGER, city TEXT)";
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.execute();
            }
        } catch (SQLException ex) {
            System.out.println("Error initializing database: " + ex.getMessage());
        }
    }
}