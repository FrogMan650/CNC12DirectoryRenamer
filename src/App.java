import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class App extends Application {
    //public static String directory;
    //public static String fileDirectory;
    //public static String board;
    //public static String version;
    //public static String machine;
    //public static String date;
    //public static String time;
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public static String renamedDirectory(String directory) {
        return directory + "_" + getVersion(directory) + "_" + getBoardType(directory) + "_" + getMachineType(directory) + "_" + getDate() + "_" + getTime();
    }

    public static void renameDirectory(String directory) {
        System.out.println(directory + "_" + getVersion(directory) + "_" + getBoardType(directory) + "_" + getMachineType(directory) + "_" + getDate() + "_" + getTime());
        // Path sourcePath = Paths.get("C:/" + directory);
        // Path destinationPath = Paths.get("C:/" + directory + "_" + getVersion(directory) + "_" + getBoardType(directory) + "_" + getMachineType(directory) + "_" + getDate() + "_" + getTime());
        // try {
        //     Files.move(sourcePath, destinationPath);
        // } catch (Exception e) {
        //     System.out.println("exception thrown while renaming " + directory);
        //     System.out.println(e);
        // }
    }

    public static void renameAllDirectory() {
        File cncm = new File("C:/cncm");
        File cnct = new File("C:/cnct");
        File cncr = new File("C:/cncr");
        File cncp = new File("C:/cncp");
        File cncl = new File("C:/cncl");
        File cncmExe = new File("C:/cncm/cncm.exe");
        File cnctExe = new File("C:/cncm/cnct.exe");
        File cncrExe = new File("C:/cncm/cncr.exe");
        File cncpExe = new File("C:/cncm/cncp.exe");
        File cnclExe = new File("C:/cncm/cncl.exe");
        if (cncm.exists()) {
            if (cncmExe.exists()) {
                renameDirectory("cncm");
            } else if (cnctExe.exists()) {
                renameDirectory("cnct");
            } else if (cncrExe.exists()) {
                renameDirectory("cncr");
            } else if (cncpExe.exists()) {
                renameDirectory("cncp");
            } else if (cnclExe.exists()) {
                renameDirectory("cncl");
            }
        }
        if (cnct.exists()) {
            renameDirectory("cnct");
        }
        if (cncr.exists()) {
            renameDirectory("cncr");
        }
        if (cncp.exists()) {
            renameDirectory("cncp");
        }
        if (cncl.exists()) {
            renameDirectory("cncl");
        }
    }

    public static String getDate() {
        String dateNow = String.valueOf(LocalDate.now());
        String[] dateSplit = dateNow.split("-");
        return dateSplit[1] + "-" + dateSplit[2] + "-" + dateSplit[0].split("0")[1];
    }

    public static String getTime() {
        String timeNow = String.valueOf(LocalTime.now());
        String[] timeSplit = timeNow.split(":");
        return timeSplit[0] + "." + timeSplit[1];
    }

    public static String getMachineType(String directory) {
        File cncmExe = new File("C:/" + directory + "/cncm.exe");
        File cnctExe = new File("C:/" + directory + "/cnct.exe");
        File cncrExe = new File("C:/" + directory + "/cncr.exe");
        File cncpExe = new File("C:/" + directory + "/cncp.exe");
        File cnclExe = new File("C:/" + directory + "/cncl.exe");
        if (cncmExe.exists()) {
            return "mill";
        } else if (cnctExe.exists()) {
            return "lathe";
        } else if (cncrExe.exists()) {
            return "router";
        } else if (cncpExe.exists()) {
            return "plasma";
        } else if (cnclExe.exists()) {
            return "laser";
        } else {
            return null;
        }
    }

    public static String getFileDirectory(String directory) {
        if (directory.equals("cnct")) {
            return "cnct";
        } else {
            return "cncm";
        }
    }

    public static String getBoardType(String directory) {
        NodeList boardVersionNodeList;
        String boardVersion = null;
        String oldFilePath = "C:/" + directory + "/mpu_info.xml";
        try {
            boardVersionNodeList = getRootElement(getDocument(oldFilePath)).getElementsByTagName("PLCDeviceID");
            boardVersion = boardVersionNodeList.item(0).getTextContent();
        } catch (Exception e) {
            System.out.println("Exception thrown while setting board type");
            System.out.println(e);
        }
        return boardVersion.split("_")[2];
    }

    public static Document getDocument(String filePath) {
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document document = null;
        try {
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            document = builder.parse(new File(filePath));
        } catch (Exception e) {
            System.out.println("Exception thrown while getting document from: " + filePath);
            System.out.println(e);
        }
        return document;
    }

    public static Element getRootElement(Document document) {
        Element rootElement = null;
        try {
            rootElement = document.getDocumentElement();
        } catch (Exception e) {
            System.out.println("Exception thrown while getting root element from document");
            System.out.println(e);
        }
            return trimEmptyElements(rootElement);
    }

    public static Element trimEmptyElements(Element node) {
        for (int i = node.getChildNodes().getLength()-1; i >= 0; i--) {
            if (node.getChildNodes().item(i).getTextContent().trim().isEmpty() && !node.getChildNodes().item(i).hasAttributes()) {
                node.removeChild(node.getChildNodes().item(i));
                continue;
            }
            if (node.getChildNodes().item(i).hasChildNodes()) {
                for (int j = node.getChildNodes().item(i).getChildNodes().getLength()-1; j >= 0; j--) {
                    if (node.getChildNodes().item(i).getChildNodes().item(j).getTextContent().trim().isEmpty() && !node.getChildNodes().item(i).getChildNodes().item(j).hasAttributes()) {
                        node.getChildNodes().item(i).removeChild(node.getChildNodes().item(i).getChildNodes().item(j));
                    }
                }
            }
        }
        return node;
    }

    public static String getVersion(String directory) {
        NodeList softwareVersionNodeList;
        String softwareVersion;
        String[] softwareVersionSplit = null;
        try {
            softwareVersionNodeList = getRootElement(getDocument("C:/" + directory + "/" + getFileDirectory(directory) + ".prm.xml")).getElementsByTagName("SoftwareVersion");
            softwareVersion = softwareVersionNodeList.item(0).getTextContent();
            softwareVersionSplit = softwareVersion.split(" ");
        } catch (Exception e) {
            System.out.println("Exception thrown while setting raw version");
            System.out.println(e);
        }
        if (softwareVersionSplit[0].equals("ACORN")) {
            return softwareVersionSplit[3];
        } else {
            return softwareVersionSplit[2];
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, Color.BLACK);

        stage.setTitle("Directory Renamer");
        Image icon = new Image("LK_logo.png");
        stage.getIcons().add(icon);
        stage.setWidth(250);
        stage.setHeight(500);
        stage.setResizable(false);
        stage.setX(50);
        stage.setY(50);
        stage.setFullScreen(false);

        Button cncmButton = new Button("cncm");
        Text cncmText = new Text("This placeholder text");
        cncmText.setX(50);
        cncmText.setY(25);
        cncmButton.setPrefSize(50, 25);
        cncmButton.setLayoutX(100);
        cncmButton.setLayoutY(0);
        cncmButton.setOnAction(event -> {
            renameDirectory("cncm");
            cncmButton.setText("DONE");
            cncmText.setText(renamedDirectory("cncm"));
        });
        Button cnctButton = new Button("cnct");
        Text cnctText = new Text();
        cnctButton.setPrefSize(50, 25);
        cnctButton.setLayoutX(100);
        cnctButton.setLayoutY(50);
        cnctButton.setOnAction(event -> {
            renameDirectory("cnct");
            cnctButton.setText("DONE");
            cnctText.setText(renamedDirectory("cnct"));
        });
        Button cncrButton = new Button("cncr");
        Text cncrText = new Text();
        cncrButton.setPrefSize(50, 25);
        cncrButton.setLayoutX(100);
        cncrButton.setLayoutY(100);
        cncrButton.setOnAction(event -> {
            renameDirectory("cncr");
            cncrButton.setText("DONE");
            cncrText.setText(renamedDirectory("cncr"));
        });
        Button cncpButton = new Button("cncp");
        Text cncpText = new Text();
        cncpButton.setPrefSize(50, 25);
        cncpButton.setLayoutX(100);
        cncpButton.setLayoutY(150);
        cncpButton.setOnAction(event -> {
            renameDirectory("cncp");
            cncpButton.setText("DONE");
            cncpText.setText(renamedDirectory("cncp"));
        });
        Button cnclButton = new Button("cncl");
        Text cnclText = new Text();
        cnclButton.setPrefSize(50, 25);
        cnclButton.setLayoutX(100);
        cnclButton.setLayoutY(200);
        cnclButton.setOnAction(event -> {
            renameDirectory("cncl");
            cnclButton.setText("DONE");
            cnclText.setText(renamedDirectory("cncl"));
        });
        Button allButton = new Button("All");
        allButton.setPrefSize(50, 25);
        allButton.setLayoutX(100);
        allButton.setLayoutY(250);
        allButton.setOnAction(event -> {
            renameAllDirectory();
            allButton.setText("DONE");
        });
        root.getChildren().add(allButton);
        root.getChildren().add(cncmButton);
        root.getChildren().add(cnctButton);
        root.getChildren().add(cncrButton);
        root.getChildren().add(cncpButton);
        root.getChildren().add(cnclButton);
        root.getChildren().add(cncmText);

        stage.setScene(scene);
        stage.show();
    }
}
