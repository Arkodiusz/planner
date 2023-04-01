package com.arje.gui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static com.arje.Main.*;

public class GUI implements ActionListener {

    private String pathToTemplateHtml = "C:\\xyz\\template.html";
    private String pathToXlsFile = "C:\\xyz\\plan1.xlsx";

    JPanel panel = new JPanel();

    private final JButton chooseHtmlButton = new JButton("Choose HTML template");
    private final JLabel htmlPathLabel = new JLabel("HTML path: ");

    private final JButton chooseXlsButton = new JButton("Choose XLS with plan");
    private final JLabel xlsPathLabel = new JLabel("XLS path: ");

    private final JButton generateButton = new JButton("Generate PDF");

    private final JFileChooser fileChooser = new JFileChooser();

    public GUI() {

        JFrame frame = new JFrame("TRENUJ ZDROWO");

        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));


        chooseHtmlButton.setSize(120, 30);
        chooseHtmlButton.addActionListener(this);

        chooseXlsButton.setSize(120, 30);
        chooseXlsButton.addActionListener(this);

        generateButton.setSize(120, 30);
        generateButton.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(120, 120, 120, 120));
        panel.setLayout(new GridLayout(3, 2));

        panel.add(chooseHtmlButton);
        panel.add(htmlPathLabel);

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

        if (event.getSource() == chooseHtmlButton) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("HTML file", "html"));
            int returnVal = fileChooser.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                pathToTemplateHtml = fileChooser.getSelectedFile().getAbsolutePath();
                htmlPathLabel.setText("HTML path: " + pathToTemplateHtml);
            }
        }

        if (event.getSource() == chooseXlsButton) {
            fileChooser.setFileFilter(new FileNameExtensionFilter("XLS file", "xls", "xlsx"));
            int returnVal = fileChooser.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                pathToXlsFile = fileChooser.getSelectedFile().getAbsolutePath();
                xlsPathLabel.setText("XLS path: " + pathToXlsFile);
            }
        }

        if (event.getSource() == generateButton) {
            if (!pathToTemplateHtml.endsWith(HTML)) {
                JOptionPane.showMessageDialog(panel, "Selected template is not " + HTML);
            } else if (!(pathToXlsFile.endsWith(XLS) || pathToXlsFile.endsWith(XLSX))) {
                JOptionPane.showMessageDialog(panel, "Selected plan is not " + XLS + " or " + XLSX);
            } else {
                try {
                    generatePdfFromGivenFiles(pathToTemplateHtml, pathToXlsFile);
                    JOptionPane.showMessageDialog(panel, "Success!");
                } catch (Exception e) { //TODO: exception handling
                    JOptionPane.showMessageDialog(panel, e.getMessage());
                }
            }
        }
    }
}
