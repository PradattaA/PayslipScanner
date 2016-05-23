package com.neelhridoy.view;

import com.alee.laf.WebLookAndFeel;
import com.neelhridoy.util.Utility;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Palash on 5/23/2016.
 */
public class MainPanel {
    public MainPanel() {
        //todo support for browse folder and export excel
        JPanel panel = new JPanel();
        JButton jButton = new JButton("Browse");
        panel.add(jButton);

        JFrame frame = new JFrame("View Test");
        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension defaultSize = new Dimension(690, 500);
        frame.setSize(defaultSize);
        frame.setMinimumSize(defaultSize);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((screenSize.width / 2) - 300, (screenSize.height / 2) - 250);
        frame.setTitle("Payslip Organizer");
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            WebLookAndFeel.setDecorateAllWindows(true);
            UIManager.setLookAndFeel(Utility.getString("default.LookAndFeel"));

            new MainPanel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
