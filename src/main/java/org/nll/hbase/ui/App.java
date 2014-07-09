package org.nll.hbase.ui;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;

public class App {

    /**
     * @param args
     */
    public static void main(String[] args) {
        launch();
        for (LookAndFeelInfo lf : UIManager.getInstalledLookAndFeels()) {
            System.out.println(lf.getName() + " " + lf.getClassName());
        }
    }

    /**
     * 设置全局字体
     *
     * @param fnt
     */
    public static void initGlobalFontSetting(Font fnt) {
        FontUIResource fontRes = new FontUIResource(fnt);
        for (Enumeration<?> keys = UIManager.getDefaults().keys(); keys
                .hasMoreElements();) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }

    public static void launch() {
        Font myfont = new Font("微软雅黑", Font.PLAIN, 12);
        initGlobalFontSetting(myfont);
        try {

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // JFrame.setDefaultLookAndFeelDecorated(true);
                    // JDialog.setDefaultLookAndFeelDecorated(true);
                    StartFrame frame = new StartFrame();
                    frame.setVisible(true);
                }
            });
        } catch (Exception e) {
        }
    }
}
