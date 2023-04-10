package com.arje.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.arje.Main.*;

public class GUI implements ActionListener {

    private String pathToXlsFile = "C:\\xyz\\plan_template.xlsx";
//    private String pathToXlsFile = "";

    JPanel panel = new JPanel();

//    private final JButton chooseHtmlButton = new JButton("Choose HTML template");
//    private final JLabel htmlPathLabel = new JLabel("HTML path: ");

    private final JButton chooseXlsButton = new JButton("Choose XLS with plan");
    private final JLabel xlsPathLabel = new JLabel("XLS path: ");

    private final JButton generateButton = new JButton("Generate PDF");

    private final JFileChooser fileChooser = new JFileChooser();

    public GUI() {

        String projectVersion = getClass().getPackage().getImplementationVersion();
        JFrame frame = new JFrame("Planner " + projectVersion);

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        chooseXlsButton.setSize(120, 30);
        chooseXlsButton.addActionListener(this);

        generateButton.setSize(120, 30);
        generateButton.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(120, 120, 120, 120));
        panel.setLayout(new GridLayout(2, 2));

        panel.add(chooseXlsButton);
        panel.add(xlsPathLabel);

        panel.add(generateButton);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation((JFrame.EXIT_ON_CLOSE));
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == chooseXlsButton) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("XLS file", "xls", "xlsx"));
            int returnVal = fileChooser.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                pathToXlsFile = fileChooser.getSelectedFile().getAbsolutePath();
                xlsPathLabel.setText("XLS path: " + pathToXlsFile);
            }
        }

        if (event.getSource() == generateButton) {
            try {
                generatePdfFromGivenFiles(pathToXlsFile);
//                JOptionPane.showMessageDialog(panel, "Success!");
            } catch (Exception e) { //TODO: exception handling
                JOptionPane.showMessageDialog(panel, e.getMessage());
            }
        }
    }
}
