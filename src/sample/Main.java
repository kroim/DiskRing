package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Optional;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.TimeUnit;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class Main extends Application {

    private int speed1 = 1;
    private int speed2 = 2;
    private int size1 = 200;
    private int size2 = 200;
    private int centerX = 370;
    private int centerY = 400;
    private int ring1_radius = 205;
    private int ring2_radius = 230;
    private int ring1_thickness = 15;
    private int ring2_thickness = 15;
    private Color ring1_color = Color.RED;
    private ColorPicker colorPicker1 = new ColorPicker(Color.RED);
    private Color ring2_color = Color.BLUE;
    private ColorPicker colorPicker2 = new ColorPicker(Color.BLUE);
    private ImagePattern imagePattern1;
    private ImagePattern imagePattern2;
    private Image image1;
    private Image image2;
    private RotateTransition rotateTransition1 = new RotateTransition();
    private RotateTransition ring1Transition = new RotateTransition();
    private RotateTransition rotateTransition2 = new RotateTransition();
    private RotateTransition ring2Transition = new RotateTransition();
    private TextField r_speed1 = new TextField();
    private TextField r_speed2 = new TextField();
    private TextField disk1_size = new TextField();
    private TextField disk2_size = new TextField();
    private TextField ring1_dis = new TextField();
    private TextField ring2_dis = new TextField();
    private TextField ring1_width = new TextField();
    private TextField ring2_width = new TextField();
    private Circle circle1 = new Circle(size1);
    private Circle circle2 = new Circle(size2);
    private Path ring1 = drawSemiRing(centerX, centerY, ring1_thickness, ring1_radius, ring1_color, Color.TRANSPARENT);
    private Path ring2 = drawSemiRing(centerX, centerY, ring2_thickness, ring2_radius, ring2_color, Color.TRANSPARENT);
    private Group group1 = new Group();
    private int FRAME_RATE = 50;
    private int SECONDS_TO_RUN_FOR;
    private ToggleGroup step_toggle = new ToggleGroup();
    private RadioButton speed_step_h = new RadioButton("Hours");
    private RadioButton speed_step_m = new RadioButton("Minutes");
    private String outputFilename;
    private Dimension screenBounds;
    private Text vr1 = new Text();
    private Text vr2 = new Text();
    private Text vr_speed1 = new Text();
    private Text vr_speed2 = new Text();

    @Override
    public void start(Stage primaryStage) throws Exception{
        ScreenSaver screenSaver = new ScreenSaver();
        Group main_group = new Group();
        primaryStage.setTitle("CD Simulation");
        DropShadow ds = new DropShadow();
        ds.setOffsetY(10.0f);
        ds.setColor(Color.color(0.5765, 0.4784, 1));

        Text t = new Text();
        t.setEffect(ds);
        t.setCache(true);
        t.setX(300.0f);
        t.setY(60.0f);
        t.setFill(Color.RED);
        t.setText("--- CD Simulation ---");
        t.setFont(Font.font("Cambria", FontWeight.BOLD, 50));
//  group 1 part
        vr1.setX(300); vr1.setY(150); vr1.setStyle("-fx-font: normal bold 20px 'serif' ");
        vr_speed1.setX(220); vr_speed1.setY(150); vr_speed1.setStyle("-fx-font: normal bold 20px 'serif' ");
        vr2.setX(450); vr2.setY(150); vr2.setStyle("-fx-font: normal bold 20px 'serif' ");
        vr_speed2.setX(370); vr_speed2.setY(150); vr_speed2.setStyle("-fx-font: normal bold 20px 'serif' ");
        Circle circle = new Circle(320);
        circle.setCenterX(centerX);
        circle.setCenterY(centerY);
        Image image = new Image("disk_back1.png");
        ImagePattern imagePattern = new ImagePattern(image);
        circle.setFill(imagePattern);
        // Circle 1
        circle1.setCenterX(centerX);
        circle1.setCenterY(centerY);
        image1 = new Image("circle1.png");
        imagePattern1 = new ImagePattern(image1);
        // Circle 2
        circle2.setCenterX(centerX);
        circle2.setCenterY(centerY);
        image2 = new Image("circle2.png");
        imagePattern2 = new ImagePattern(image2);
//  group 2 part
        Group group2 = new Group();
        Text info = new Text("***** CD Information *****"); info.setY(70); info.setX(900);
        info.setFont(Font.font("serif", FontWeight.BOLD, 25));
        Text import_disk = new Text("Import DiskImage"); import_disk.setX(800); import_disk.setY(110);
        Button im_disk1 = new Button("Import"); im_disk1.setLayoutX(1000); im_disk1.setLayoutY(90);
        Button im_disk2 = new Button("Import"); im_disk2.setLayoutX(1200); im_disk2.setLayoutY(90);
        Text disk_size = new Text("Disk Size"); disk_size.setX(800); disk_size.setY(173);
        disk1_size.setLayoutX(1000); disk1_size.setLayoutY(148); disk2_size.setLayoutX(1200); disk2_size.setLayoutY(148);
        Text disk_over = new Text("Disk Over"); disk_over.setX(800); disk_over.setY(226);
        ToggleGroup toggleGroup = new ToggleGroup();
        RadioButton disk1_over = new RadioButton("Disk1"); disk1_over.setLayoutX(1000); disk1_over.setLayoutY(211);
        RadioButton disk2_over = new RadioButton("Disk2"); disk2_over.setLayoutX(1200); disk2_over.setLayoutY(211);
        disk1_over.setToggleGroup(toggleGroup);
        disk2_over.setToggleGroup(toggleGroup);
        Text rotation_speed = new Text("Rotation Speed Time"); rotation_speed.setX(800); rotation_speed.setY(279);
        r_speed1.setLayoutX(1000); r_speed1.setLayoutY(254); r_speed2.setLayoutX(1200); r_speed2.setLayoutY(254);
        Text text_ring_dis = new Text("Ring Distances"); text_ring_dis.setX(800); text_ring_dis.setY(340);
        ring1_dis.setLayoutX(1000); ring1_dis.setLayoutY(317); ring2_dis.setLayoutX(1200); ring2_dis.setLayoutY(317);
        Text text_ring_width = new Text("Ring Widths"); text_ring_width.setX(800); text_ring_width.setY(405);
        ring1_width.setLayoutX(1000); ring1_width.setLayoutY(380); ring2_width.setLayoutX(1200); ring2_width.setLayoutY(380);
        colorPicker1.setLayoutX(1010); colorPicker1.setLayoutY(430); colorPicker2.setLayoutX(1210); colorPicker2.setLayoutY(430);
        speed_step_h.setToggleGroup(step_toggle); speed_step_h.setLayoutX(800); speed_step_h.setLayoutY(475);
        speed_step_m.setToggleGroup(step_toggle); speed_step_m.setLayoutX(900); speed_step_m.setLayoutY(475);
        speed_step_h.setSelected(true);
        Button rotation1 = new Button("Rotation"); rotation1.setLayoutX(1000); rotation1.setLayoutY(475);
        Button rotation2 = new Button("Rotation"); rotation2.setLayoutX(1200); rotation2.setLayoutY(475);
        Button apply_button = new Button("Apply"); apply_button.setLayoutX(800); apply_button.setLayoutY(535);
        ComboBox emailComboBox = new ComboBox(); emailComboBox.setLayoutX(930);emailComboBox.setLayoutY(535);
        emailComboBox.getItems().addAll("Save As...", "Save As Video", "Screen Saver", "Print");
        CheckBox ch1 = new CheckBox("D-1"); ch1.setLayoutX(1150);ch1.setLayoutY(542);
        CheckBox ch2 = new CheckBox("D-2"); ch2.setLayoutX(1220); ch2.setLayoutY(542);
        Button ring_button = new Button("Reset Ring"); ring_button.setLayoutX(800); ring_button.setLayoutY(600);
        Button start = new Button("Start"); start.setLayoutX(970); start.setLayoutY(600);
        Button stop = new Button("Stop"); stop.setLayoutX(1085); stop.setLayoutY(600);
        Button close = new Button("Close"); close.setLayoutX(1210); close.setLayoutY(600);

// ********* CSS for Controls *********** //
        import_disk.setStyle("-fx-font: normal bold 20px 'serif' "); disk_size.setStyle("-fx-font: normal bold 20px 'serif' ");
        disk_over.setStyle("-fx-font: normal bold 20px 'serif' "); rotation_speed.setStyle("-fx-font: normal bold 20px 'serif' ");
        text_ring_dis.setStyle("-fx-font: normal bold 20px 'serif' "); text_ring_width.setStyle("-fx-font: normal bold 20px 'serif' ");
        disk1_over.setStyle("-fx-font: normal bold 15px 'serif' "); disk2_over.setStyle("-fx-font: normal bold 15px 'serif' ");
        im_disk1.setStyle("-fx-background-color: \n" +
                "        linear-gradient(#ffd65b, #e68400),\n" +
                "        linear-gradient(#ffef84, #f2ba44),\n" +
                "        linear-gradient(#ffea6a, #efaa22),\n" +
                "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
                "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 10;\n" +
                "    -fx-background-insets: 0,1,2,3,0;\n" +
                "    -fx-text-fill: #654b00;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-padding: 10 20 10 20;");
        im_disk2.setStyle("-fx-background-color: \n" +
                "        linear-gradient(#ffd65b, #e68400),\n" +
                "        linear-gradient(#ffef84, #f2ba44),\n" +
                "        linear-gradient(#ffea6a, #efaa22),\n" +
                "        linear-gradient(#ffe657 0%, #f8c202 50%, #eea10b 100%),\n" +
                "        linear-gradient(from 0% 0% to 15% 50%, rgba(255,255,255,0.9), rgba(255,255,255,0));\n" +
                "    -fx-background-radius: 10;\n" +
                "    -fx-background-insets: 0,1,2,3,0;\n" +
                "    -fx-text-fill: #654b00;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 14px;\n" +
                "    -fx-padding: 10 20 10 20;");
        apply_button.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        emailComboBox.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 18px;");
        start.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        stop.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        close.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        ring_button.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: orangered;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        disk1_size.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-pr: 40;\n" +
                "    -fx-font-size: 20px;");
        disk2_size.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        r_speed1.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        r_speed2.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        ring1_dis.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        ring2_dis.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        ring1_width.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        ring2_width.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 0 10 0 10;\n" +
                "    -fx-text-fill: black;\n" +
                "    -fx-max-width: 150;\n" +
                "    -fx-min-height: 40;\n" +
                "    -fx-font-size: 20px;");
        rotation1.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: blue;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        rotation2.setStyle("-fx-background-radius: 10;\n" +
                "    -fx-padding: 3 30 3 30;\n" +
                "    -fx-text-fill: blue;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-font-size: 20px;");
        speed_step_h.setStyle("-fx-font-family: 'Times New Roman';" +
                "-fx-font-size: 16px;");
        speed_step_m.setStyle("-fx-font-family: 'Times New Roman';" +
                "-fx-font-size: 16px;");

        r_speed1.setText(Integer.toString(speed1)); r_speed2.setText(Integer.toString(speed2));
        disk1_size.setText(Integer.toString(size1)); disk2_size.setText(Integer.toString(size2));
        ring1_dis.setText(Integer.toString(ring1_radius)); ring2_dis.setText(Integer.toString(ring2_radius));
        ring1_width.setText(Integer.toString(ring1_thickness)); ring2_width.setText(Integer.toString(ring2_thickness));
        disk2_over.setSelected(true);
        screenSaver.top_on = "2";

        im_disk1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            screenSaver.image1_path = file.toString();
                            try {
                                image1 = new Image(file.toURI().toURL().toExternalForm());
                                imagePattern1 = new ImagePattern(image1);
                                circle1.setFill(imagePattern1);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }
        );
        im_disk2.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        FileChooser fileChooser = new FileChooser();
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            screenSaver.image2_path = file.toString();
                            try {
                                image2 = new Image(file.toURI().toURL().toExternalForm());
                                imagePattern2 = new ImagePattern(image2);
                                circle2.setFill(imagePattern2);
                            } catch (IOException ex) {
                                System.out.println(ex.getMessage());
                            }
                        }
                    }
                }
        );
        TextInputDialog e_dialog = new TextInputDialog("input email");
        Alert alert = new Alert(Alert.AlertType.WARNING);
        apply_button.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
//                        start.onMouseClickedProperty();
                        if (emailComboBox.getSelectionModel().getSelectedItem() == null){
                            alert.setTitle("Warning!!!");
                            alert.setHeaderText(null);
                            alert.setContentText("Please Choose the Apply Menu !");
                            alert.showAndWait();
                        }
                        else if(emailComboBox.getSelectionModel().getSelectedItem().toString().equals("Save As Video")){

                            TextInputDialog dialog = new TextInputDialog("Input Number");
                            dialog.setTitle("Video Property Window");
                            dialog.setHeaderText("Please Input Video Time!");
                            dialog.setContentText("Video Time (s)");
                            Optional<String> result = dialog.showAndWait();

                            if (result.isPresent()){
                                vr1.setText(r_speed1.getText());
                                vr2.setText(r_speed2.getText());
                                vr_speed1.setText("Speed1 : ");
                                vr_speed2.setText("Speed2 : ");
                                FileChooser chooser = new FileChooser();
                                chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video", "*.mp4"));
                                File file = chooser.showSaveDialog(null);
                                if(file != null){
                                    outputFilename = file.toString();
                                }
                                SECONDS_TO_RUN_FOR = Integer.parseInt(result.get());
                                System.out.println(SECONDS_TO_RUN_FOR);
//                                Ratation();
                                videoRotation(SECONDS_TO_RUN_FOR);
                                videoCreate(outputFilename, SECONDS_TO_RUN_FOR, FRAME_RATE);
                            }
                        }
                        else if (emailComboBox.getSelectionModel().getSelectedItem().toString().equals("Screen Saver")){
                            try {
                                screenSaver.disk1_speed = r_speed1.getText();
                                screenSaver.disk2_speed = r_speed2.getText();
                                if (speed_step_h.isSelected()){
                                    screenSaver.tmp = "h";
                                }else {
                                    screenSaver.tmp = "m";
                                }
                                screenSaver.saveJson();
                                screenSaver.screensaver();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else if(ch1.isSelected() && ch2.isSelected()){
                            Group newGroup = new Group();
                            newGroup.getChildren().addAll(circle1, circle2, ring1, ring2);
                            WritableImage img = newGroup.snapshot(new SnapshotParameters(), null);
                            BufferedImage img1 = SwingFXUtils.fromFXImage(img,null);
                            if(emailComboBox.getSelectionModel().getSelectedItem().toString().equals( "Save As...")){
                                FileChooser chooser = new FileChooser();
                                chooser.getExtensionFilters().addAll(
                                        new FileChooser.ExtensionFilter("PNG", "*.png"),
                                        new FileChooser.ExtensionFilter("JPG", "*.jpg")
                                );
                                File file = chooser.showSaveDialog(null);
                                if(file != null){
                                    try {
                                        String file_name = file.getName();
                                        String[] parts = file_name.split("\\.");
                                        String file_ext = parts[parts.length - 1];
                                        if (file_ext.equals("jpg")) {
                                            System.out.println("JPG image: " + file_name);
                                            BufferedImage img1_result = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                                            img1_result.createGraphics().drawImage(img1, 0, 0, java.awt.Color.WHITE, null);
                                            ImageIO.write(img1_result, file_ext, file);
                                        } else {
                                            ImageIO.write(img1, file_ext, file);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                newGroup.getChildren().removeAll(circle1, circle2, ring1, ring2);
                                group1.getChildren().addAll(circle1, circle2, ring1, ring2);
                            }
                            else if(emailComboBox.getSelectionModel().getSelectedItem().toString() == "Print"){
                                primaryStage.hide();
                                printImage(img1);
                                primaryStage.show();
                            }
                        }
                        else if(ch1.isSelected()){
                            Group newGroup1 = new Group();
                            newGroup1.getChildren().addAll(circle1, ring1);
                            WritableImage img = newGroup1.snapshot(new SnapshotParameters(), null);
                            BufferedImage img1 = SwingFXUtils.fromFXImage(img,null);
                            if(emailComboBox.getSelectionModel().getSelectedItem().toString() == "Save As..."){
                                FileChooser chooser = new FileChooser();
                                chooser.getExtensionFilters().addAll(
                                        new FileChooser.ExtensionFilter("PNG", "*.png"),
                                        new FileChooser.ExtensionFilter("JPG", "*.jpg")

                                );
                                File file = chooser.showSaveDialog(null);
                                if(file != null){
                                    try {
                                        String file_name = file.getName();
                                        String[] parts = file_name.split("\\.");
                                        String file_ext = parts[parts.length - 1];
                                        if (file_ext.equals("jpg")) {
                                            System.out.println("JPG image: " + file_name);
                                            BufferedImage img1_result = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                                            img1_result.createGraphics().drawImage(img1, 0, 0, java.awt.Color.WHITE, null);
                                            ImageIO.write(img1_result, file_ext, file);
                                        } else {
                                            ImageIO.write(img1, file_ext, file);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                newGroup1.getChildren().removeAll(circle1, ring1);
                                group1.getChildren().addAll(circle1, ring1);
                            }
                            else if(emailComboBox.getSelectionModel().getSelectedItem().toString() == "Print"){
                                primaryStage.hide();
                                printImage(img1);
                                primaryStage.show();
                            }
                        }
                        else if(ch2.isSelected()){
                            Group newGroup2 = new Group();
                            newGroup2.getChildren().addAll(circle2, ring2);
                            WritableImage img = newGroup2.snapshot(new SnapshotParameters(), null);
                            BufferedImage img1 = SwingFXUtils.fromFXImage(img,null);
                            if(emailComboBox.getSelectionModel().getSelectedItem().toString() == "Save As..."){
                                FileChooser chooser = new FileChooser();
                                chooser.getExtensionFilters().addAll(
                                        new FileChooser.ExtensionFilter("PNG", "*.png"),
                                        new FileChooser.ExtensionFilter("JPG", "*.jpg")
                                );
                                File file = chooser.showSaveDialog(null);
                                if(file != null){
                                    try {
                                        String file_name = file.getName();
                                        String[] parts = file_name.split("\\.");
                                        String file_ext = parts[parts.length - 1];
                                        if (file_ext.equals("jpg")) {
                                            System.out.println("JPG image: " + file_name);
                                            BufferedImage img1_result = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_BGR);
                                            img1_result.createGraphics().drawImage(img1, 0, 0, java.awt.Color.WHITE, null);
                                            ImageIO.write(img1_result, file_ext, file);
                                        } else {
                                            ImageIO.write(img1, file_ext, file);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                newGroup2.getChildren().removeAll(circle2, ring2);
                                group1.getChildren().addAll(circle2, ring2);
                            }
                            else if(emailComboBox.getSelectionModel().getSelectedItem().toString() == "Print"){
                                primaryStage.hide();
                                printImage(img1);
                                primaryStage.show();
                            }
                        }
                        else {

                            alert.setTitle("Warning!!!");
                            alert.setHeaderText(null);
                            alert.setContentText("Please Choose the Disk number !");
                            alert.showAndWait();

                        }
                    }
                }
        );
        ring_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                group1.getChildren().remove(ring1);
                ring1_radius = Integer.parseInt(ring1_dis.getText());
                ring1_thickness = Integer.parseInt(ring1_width.getText());
                ring1 = drawSemiRing(centerX, centerY, ring1_thickness, ring1_radius, ring1_color, Color.TRANSPARENT);
                group1.getChildren().add(ring1);
                group1.getChildren().remove(ring2);
                ring2_radius = Integer.parseInt(ring2_dis.getText());
                ring2_thickness = Integer.parseInt(ring2_width.getText());
                ring2 = drawSemiRing(centerX, centerY, ring2_thickness, ring2_radius, ring2_color, Color.TRANSPARENT);
                group1.getChildren().add(ring2);
            }
        });
        rotation1.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        vr1.setText(r_speed1.getText());
                        speed1 = Integer.parseInt(r_speed1.getText());
                        size1 = Integer.parseInt(disk1_size.getText());
                        circle1.setRadius(size1);
                        if (speed_step_h.isSelected()){
                            rotateTransition1.setDuration(Duration.hours(((float)speed1)));
                            ring1Transition.setDuration(Duration.hours(((float)speed1)));
                        }else if (speed_step_m.isSelected()){
                            rotateTransition1.setDuration(Duration.minutes(((float)speed1)));
                            ring1Transition.setDuration(Duration.minutes(((float)speed1)));
                        }
                        rotateTransition1.setNode(circle1);
                        ring1Transition.setNode(ring1);
                        rotateTransition1.setByAngle(360);
                        ring1Transition.setByAngle(360);
                        rotateTransition1.setCycleCount(5000);
                        ring1Transition.setCycleCount(5000);
                        rotateTransition1.setInterpolator(Interpolator.LINEAR);
                        ring1Transition.setInterpolator(Interpolator.LINEAR);
                        rotateTransition1.play();
                        ring1Transition.play();
                    }
                }
        );
        rotation2.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        vr2.setText(r_speed2.getText());
                        speed2 = Integer.parseInt(r_speed2.getText());
                        size2 = Integer.parseInt(disk2_size.getText());
                        circle2.setRadius(size2);
                        if (speed_step_h.isSelected()){
                            rotateTransition2.setDuration(Duration.hours(((float)speed2)));
                            ring2Transition.setDuration(Duration.hours(((float)speed2)));
                        }else if (speed_step_m.isSelected()){
                            rotateTransition2.setDuration(Duration.minutes(((float)speed2)));
                            ring2Transition.setDuration(Duration.minutes(((float)speed2)));
                        }
                        rotateTransition2.setNode(circle2);
                        ring2Transition.setNode(ring2);
                        rotateTransition2.setByAngle(360);
                        ring2Transition.setByAngle(360);
                        rotateTransition2.setCycleCount(5000);
                        ring2Transition.setCycleCount(5000);
                        rotateTransition2.setInterpolator(Interpolator.LINEAR);
                        ring2Transition.setInterpolator(Interpolator.LINEAR);
                        rotateTransition2.play();
                        ring2Transition.play();
                    }
                }
        );
        start.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        speed1 = Integer.parseInt(r_speed1.getText()); vr1.setText(r_speed1.getText());
                        speed2 = Integer.parseInt(r_speed2.getText()); vr2.setText(r_speed2.getText());
                        size1 = Integer.parseInt(disk1_size.getText());
                        size2 = Integer.parseInt(disk2_size.getText());
                        circle1.setRadius(size1); circle2.setRadius(size2);
                        if (speed_step_h.isSelected()){
                            rotateTransition1.setDuration(Duration.hours(((float)speed1)));
                            ring1Transition.setDuration(Duration.hours(((float)speed1)));
                            rotateTransition2.setDuration(Duration.hours(((float)speed2)));
                            ring2Transition.setDuration(Duration.hours(((float)speed2)));
                        }else if (speed_step_m.isSelected()){
                            rotateTransition1.setDuration(Duration.minutes(((float)speed1)));
                            ring1Transition.setDuration(Duration.minutes(((float)speed1)));
                            rotateTransition2.setDuration(Duration.minutes(((float)speed2)));
                            ring2Transition.setDuration(Duration.minutes(((float)speed2)));
                        }
                        rotateTransition1.setNode(circle1);
                        ring1Transition.setNode(ring1);
                        rotateTransition1.setByAngle(360);
                        ring1Transition.setByAngle(360);
                        rotateTransition1.setCycleCount(500);
                        ring1Transition.setCycleCount(500);
                        rotateTransition1.setInterpolator(Interpolator.LINEAR);
                        ring1Transition.setInterpolator(Interpolator.LINEAR);

                        rotateTransition2.setNode(circle2);
                        ring2Transition.setNode(ring2);
                        rotateTransition2.setByAngle(360);
                        ring2Transition.setByAngle(360);
                        rotateTransition2.setCycleCount(500);
                        ring2Transition.setCycleCount(500);
                        rotateTransition2.setInterpolator(Interpolator.LINEAR);
                        ring2Transition.setInterpolator(Interpolator.LINEAR);
                        rotateTransition1.play();
                        ring1Transition.play();
                        rotateTransition2.play();
                        ring2Transition.play();
                    }
                }
        );
        stop.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        vr1.setText("");
                        vr2.setText("");
                        rotateTransition1.stop();
                        ring1Transition.stop();
                        rotateTransition2.stop();
                        ring2Transition.stop();
                    }
                }
        );
        close.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        System.exit(0);
                    }
                }
        );

        circle1.setFill(imagePattern1);
        circle2.setFill(imagePattern2);
// ********** group 1 ********* //
        group1.getChildren().addAll(circle1, circle2, vr1, vr2, vr_speed1, vr_speed2, ring1, ring2);
        disk1_over.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        circle1.toFront();
                        screenSaver.top_on = "1";
                    }
                }
        );
        disk2_over.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        circle2.toFront();
                        screenSaver.top_on = "2";
                    }
                }
        );
        colorPicker1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ring1_color = colorPicker1.getValue();
            }
        });
        colorPicker2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ring2_color = colorPicker2.getValue();
            }
        });
// ********* group2 *********** //
        group2.getChildren().addAll(info, import_disk, im_disk1, im_disk2, disk_size, disk1_size, disk2_size);
        group2.getChildren().addAll(disk_over, disk1_over, disk2_over, rotation_speed, r_speed1, r_speed2);
        group2.getChildren().addAll(text_ring_dis, ring1_dis, ring2_dis, text_ring_width, ring1_width, ring2_width);
        group2.getChildren().addAll(speed_step_h, speed_step_m, rotation1, rotation2, colorPicker1, colorPicker2);
        group2.getChildren().addAll(apply_button, emailComboBox, ch1, ch2, start, stop, close, ring_button);

        main_group.getChildren().add(t);
        main_group.getChildren().add(group1);
        main_group.getChildren().add(group2);
        Scene scene = new Scene(main_group);
        primaryStage.getIcons().add(new Image("file:lib/CD-ROM.png"));
        primaryStage.setScene(scene);
//        scene.setFill(Color.rgb(194,153,255));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
    private void printImage(BufferedImage image) {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        boolean aaa = printJob.printDialog();
        if(aaa){
            printJob.setPrintable(new Printable() {
                @Override
                public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                    // Get the upper left corner that it printable
                    int x = (int) Math.ceil(pageFormat.getImageableX());
                    int y = (int) Math.ceil(pageFormat.getImageableY());
                    if (pageIndex != 0) {
                        return NO_SUCH_PAGE;
                    }
                    graphics.drawImage(image, 50, 150, 450, 450, null);
                    return PAGE_EXISTS;
                }
            });
            try {
                printJob.print();
            } catch (PrinterException e1) {
                e1.printStackTrace();
            }
        }

    }
    private static BufferedImage convertToType(BufferedImage sourceImage, int targetType){
        BufferedImage image;
        if(sourceImage.getType() == targetType){
            image = sourceImage;
        }
        else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }
    private void Rotation(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                speed1 = Integer.parseInt(r_speed1.getText());
                speed2 = Integer.parseInt(r_speed2.getText());
                size1 = Integer.parseInt(disk1_size.getText());
                size2 = Integer.parseInt(disk2_size.getText());
                circle1.setRadius(size1); circle2.setRadius(size2);
                if (speed_step_h.isSelected()){
                    rotateTransition1.setDuration(Duration.hours(((float)speed1)));
                    ring1Transition.setDuration(Duration.hours(((float)speed1)));
                    rotateTransition2.setDuration(Duration.hours(((float)speed2)));
                    ring2Transition.setDuration(Duration.hours(((float)speed2)));
                }else if (speed_step_m.isSelected()){
                    rotateTransition1.setDuration(Duration.minutes(((float)speed1)));
                    ring1Transition.setDuration(Duration.minutes(((float)speed1)));
                    rotateTransition2.setDuration(Duration.minutes(((float)speed2)));
                    ring2Transition.setDuration(Duration.minutes(((float)speed2)));
                }
                rotateTransition1.setNode(circle1);
                ring1Transition.setNode(ring1);
                rotateTransition1.setByAngle(360);
                ring1Transition.setByAngle(360);
                rotateTransition1.setCycleCount(500);
                ring1Transition.setCycleCount(500);
                rotateTransition1.setInterpolator(Interpolator.LINEAR);
                ring1Transition.setInterpolator(Interpolator.LINEAR);

                rotateTransition2.setNode(circle2);
                ring2Transition.setNode(ring2);
                rotateTransition2.setByAngle(360);
                ring2Transition.setByAngle(360);
                rotateTransition2.setCycleCount(500);
                ring2Transition.setCycleCount(500);
                rotateTransition2.setInterpolator(Interpolator.LINEAR);
                ring2Transition.setInterpolator(Interpolator.LINEAR);
                rotateTransition1.play();
                ring1Transition.play();
                rotateTransition2.play();
                ring2Transition.play();
            }
        }).start();
    }
    private void videoRotation(int timeFrame){
        remove_log();
        Rotation();
        int big_speed = 0;
        int small_speed = 0;
        speed1 = Integer.parseInt(r_speed1.getText());
        speed2 = Integer.parseInt(r_speed2.getText());
        if (speed1 < speed2){
            big_speed = speed2;
            small_speed = speed1;
        } else {
            big_speed = speed1;
            small_speed = speed2;
        }
        int timeline = timeCalc(timeFrame, small_speed, big_speed);
        if (timeline != 0 && speed_step_m.isSelected()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(timeline * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rotateTransition1.stop();
                    ring1Transition.stop();
                    rotateTransition2.stop();
                    ring2Transition.stop();
//                    speed_step_m.setSelected(false);
//                    speed_step_h.setSelected(true);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Rotation();
                }
            }).start();
        }

    }
    private void videoCreate(String output, int timeframe, int framestep){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final IMediaWriter writer = ToolFactory.makeWriter(output);
                screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
                writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,640, 640);
                final long startTime = System.nanoTime();
//                group1.getChildren().add(r_speed1);
//                group1.getChildren().add(r_speed2);
                for (int index = 0; index < timeframe * framestep; index++) {
                    Platform.runLater(new Runnable() {
                        final IMediaWriter _writer = writer;
                        final long _startTime = startTime;
                        @Override
                        public void run() {

                            WritableImage img3 = group1.snapshot(new SnapshotParameters(), null);
                            BufferedImage screenvideo = SwingFXUtils.fromFXImage(img3, null);
                            BufferedImage bgrScreen = convertToType(screenvideo, BufferedImage.TYPE_3BYTE_BGR);
                            _writer.encodeVideo(0, bgrScreen, System.nanoTime() - _startTime, TimeUnit.NANOSECONDS);
                        }
                    });
                    try {
                        Thread.sleep(((long) (1000 / framestep)));
                    } catch (InterruptedException e) {
                    }
                }
                writer.close();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        vr1.setText("");
                        vr2.setText("");
                        vr_speed1.setText("");
                        vr_speed2.setText("");
                        rotateTransition1.stop();
                        ring1Transition.stop();
                        rotateTransition2.stop();
                        ring2Transition.stop();
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Video Window");
                        alert.setHeaderText(null);
                        alert.setContentText("You Made video Successfully!");
//                        alert.showAndWait();
                        Optional<ButtonType> result1 = alert.showAndWait();
                        if(result1.isPresent()){
                            System.out.println("Made a video");
                        }
                    }
                });
            }
        }).start();
    }
    private int timeCalc(int timeLine, int firstSpeed, int secondSpeed){
        int rotationNum = 0;
        if (timeLine > firstSpeed * 120){
            rotationNum = timeLine / 120;
        }
        float res = ((((float)firstSpeed/secondSpeed) / ((float) 1/firstSpeed - (float) 1/secondSpeed)) * 60 * rotationNum);
        return (int) (res+60*rotationNum) ;
    }
    private void remove_log(){
        final String dir = System.getProperty("user.dir");
        File folder = new File(dir);
        System.out.println(dir);
        final File[] files = folder.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                return name.matches( "hs_err_pid.*\\.log" );
            }
        } );
        for ( final File file : files ) {
            if ( !file.delete() ) {
                System.err.println( "Can't remove " + file.getAbsolutePath() );
            }
        }
    }
    private Path drawSemiRing(double centerX, double centerY, double thickness, double innerRadius, Color bgColor, Color borderColor) {
        double outerRadius = thickness + innerRadius;
        Path path = new Path();
        path.setFill(bgColor);
        path.setStroke(borderColor);
        path.setFillRule(FillRule.EVEN_ODD);

        MoveTo moveTo = new MoveTo();
        moveTo.setX(centerX - 3);
        moveTo.setY(centerY - innerRadius);

        ArcTo arcToInner = new ArcTo();
        arcToInner.setX(centerX + 3);
        arcToInner.setY(centerY - innerRadius);
        arcToInner.setRadiusX(innerRadius);
        arcToInner.setRadiusY(innerRadius);
        arcToInner.setLargeArcFlag(true);

        MoveTo moveTo2 = new MoveTo();
        moveTo2.setX(centerX - 3);
        moveTo2.setY(centerY - innerRadius);

        VLineTo vLineTo = new VLineTo();
        vLineTo.setY(centerY - outerRadius);

        ArcTo arcTo = new ArcTo();
        arcTo.setX(centerX + 3);
        arcTo.setY(centerY - outerRadius);
        arcTo.setRadiusX(outerRadius);
        arcTo.setRadiusY(outerRadius);
        arcTo.setLargeArcFlag(true);

        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.setY(centerY - innerRadius);

        path.getElements().add(moveTo);
        path.getElements().add(arcToInner);
        path.getElements().add(moveTo2);
        path.getElements().add(vLineTo);
        path.getElements().add(arcTo);
        path.getElements().add(vLineTo1);

        return path;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
