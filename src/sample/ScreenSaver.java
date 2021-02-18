package sample;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

public class ScreenSaver {
    public String image1_path = "circle1.png";
    public String image2_path = "circle2.png";
    public String top_on;
    public String disk1_speed;
    public String disk2_speed;
    public String tmp;
    Thread thread = null;

    public void check_key(JFrame jFrame){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int index = 0;
                while (true){
                    try {
                        Thread.sleep(100);
                        index++;
                        if (index > 100) {
                            jFrame.dispose();
                            thread.join();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
    public void saveJson(){
        String start_t = "";
        String stop_t = "";
        String disk1_an = "0";
        String disk2_an = "0";
        String desPath1 = "./cache/image1.png";
        String desPath2 = "./cache/image2.png";
        try {
            Files.copy(new File(image1_path).toPath(), new File(desPath1).toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(new File(image2_path).toPath(), new File(desPath2).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String propPath = "./cache/config.properties";
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream(propPath);
            prop.setProperty("disk1_an", disk1_an);
            prop.setProperty("disk2_an", disk2_an);
            prop.setProperty("start_t", start_t);
            prop.setProperty("stop_t", stop_t);
            prop.setProperty("disk1_speed", disk1_speed);
            prop.setProperty("disk2_speed", disk2_speed);
            prop.setProperty("tmp", tmp);
            prop.setProperty("top_on", top_on);
            prop.store(output, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void screensaver() throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        final JFrame screenSaverFrame = new JFrame();
        screenSaverFrame.setDefaultCloseOperation(
                WindowConstants.EXIT_ON_CLOSE);
        screenSaverFrame.setUndecorated(true);
        screenSaverFrame.setResizable(false);
        JLabel jLabel = new JLabel("Screen Saver is saving...!", SwingConstants.CENTER);
        jLabel.setFont(new Font("Sans Serif", Font.BOLD, 80));
        jLabel.setForeground(Color.BLUE);
        screenSaverFrame.add(jLabel, BorderLayout.CENTER);
        screenSaverFrame.validate();
        GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .setFullScreenWindow(screenSaverFrame);
        check_key(screenSaverFrame);
    }

}
