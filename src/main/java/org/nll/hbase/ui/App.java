/*
 * Copyright 2014 fivesmallq.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nll.hbase.ui;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.plaf.FontUIResource;
import org.nll.hbase.ui.component.StartFrame;

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
