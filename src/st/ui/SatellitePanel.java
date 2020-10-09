package st.ui;

import st.SatelliteManager;

import javax.swing.*;


import java.awt.Dimension;

import java.io.IOException;
import java.util.List;

import static javax.swing.SpringLayout.NORTH;
import static javax.swing.SpringLayout.EAST;
import static javax.swing.SpringLayout.SOUTH;
import static javax.swing.SpringLayout.WEST;

public class SatellitePanel extends JPanel {

    private ControlPanel parent;

    private SatelliteManager satMgr;

    private JTextArea noradArea;
    private JButton update, save;


    public SatellitePanel(ControlPanel parent, SatelliteManager satMgr) {
        this.satMgr = satMgr;
        this.parent = parent;

        setPreferredSize(new Dimension(300, 150));

        SpringLayout sl = new SpringLayout();
        this.setLayout(sl);


        update = new JButton("Update viewport");
        sl.putConstraint(WEST, update, 10, WEST, this);
        sl.putConstraint(SOUTH, update, -10, SOUTH, this);
        this.add(update);

        noradArea = new JTextArea();
        noradArea.setLineWrap(true);
        noradArea.setToolTipText("Enter whitespace separated norad ids");
        JScrollPane noradPane = new JScrollPane(noradArea);
        noradPane.setPreferredSize(new Dimension(280, 100));

        sl.putConstraint(WEST, noradPane, 10, WEST, this);
        sl.putConstraint(NORTH, noradPane, 10, NORTH, this);
        sl.putConstraint(EAST, noradPane, -10, EAST, this);
        this.add(noradPane);

        update.addActionListener(e -> {
            String str = noradArea.getText();
            String[] strs = str.split("\s+");
            int[] norads = new int[strs.length];

            for (int i = 0; i < strs.length; i++) {
                norads[i] = Integer.parseInt(strs[i]);
            }

            try {
                satMgr.track(norads);
            } catch (Throwable t) {
                t.printStackTrace();
            }

            parent.getAccount().saveSatellite(satMgr.getNoradIds());
        });

    }

    public void updateSatelliteList() throws IOException, InterruptedException {
        List<Integer> norads = parent.getAccount().getSatellites();
        int[] norads_ = new int[norads.size()];

        for (int i = 0; i < norads.size(); i++) {
            norads_[i] = norads.get(i).intValue();
        }

        satMgr.track(norads_);

        String str = "";
        for(int norad : norads_) {
            str += norad + " ";
        }

        noradArea.setText(str);


    }



}
