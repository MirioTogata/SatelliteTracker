package st.ui;

import st.Account;
import st.SatelliteManager;

import javax.swing.*;

public class ControlPanel extends JPanel {

    private LoginPanel lp;
    private SatellitePanel sp;
    private Account acc;


    public ControlPanel(SatelliteManager satMgr) {

        lp = new LoginPanel(this);
        sp = new SatellitePanel(this, satMgr);

        this.add(lp);
        this.add(sp);
        sp.setVisible(false);

    }


    public void setAccount(Account acc) {
        this.acc = acc;

        lp.setVisible(false);
        sp.setVisible(true);

        try {
            sp.updateSatelliteList();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    public Account getAccount() {
        return acc;
    }

}
