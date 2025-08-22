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

public class App {
    public static String directory = "cncm";
    public static String fileDirectory;
    public static String board;
    public static String version;
    public static String machine;
    public static String date;
    public static String time;
    public static void main(String[] args) throws Exception {
        setFileDirectory();
        setBoardType();
        setMachineType();
        setRawVersion();
        setDateTime();
        // renameDirectory();
        
        System.out.println(directory + "_" + version + "_" + board + "_" + machine + "_" + date + "_" + time);
    }

    public static void renameDirectory() {
        Path sourcePath = Paths.get("C:/" + directory);
        Path destinationPath = Paths.get("C:/" + directory + "_" + version + "_" + board + "_" + machine + "_" + date + "_" + time);
        try {
            Files.move(sourcePath, destinationPath);
        } catch (Exception e) {
            System.out.println("exception thrown while renaming " + directory);
            System.out.println(e);
        }
    }

    public static void setDateTime() {
        String dateNow = String.valueOf(LocalDate.now());
        String timeNow = String.valueOf(LocalTime.now());
        String[] dateSplit = dateNow.split("-");
        date = dateSplit[1] + "-" + dateSplit[2] + "-" + dateSplit[0].split("0")[1];
        String[] timeSplit = timeNow.split(":");
        time = timeSplit[0] + "." + timeSplit[1];
    }

    public static void setMachineType() {
        File cncmExe = new File("C:/" + directory + "/cncm.exe");
        File cnctExe = new File("C:/" + directory + "/cnct.exe");
        File cncrExe = new File("C:/" + directory + "/cncr.exe");
        File cncpExe = new File("C:/" + directory + "/cncp.exe");
        File cnclExe = new File("C:/" + directory + "/cncl.exe");
        if (cncmExe.exists()) {
            machine = "mill";
        } else if (cnctExe.exists()) {
            machine = "lathe";
        } else if (cncrExe.exists()) {
            machine = "router";
        } else if (cncpExe.exists()) {
            machine = "plasma";
        } else if (cnclExe.exists()) {
            machine = "laser";
        }
    }

    public static void setFileDirectory() {
        if (directory.equals("cnct")) {
            fileDirectory = "cnct";
        } else {
            fileDirectory = "cncm";
        }
    }

    public static void setBoardType() {
        NodeList boardVersionNodeList;
        String boardVersion = null;
        String oldFilePath = "C:/" + directory + "/mpu_info.xml";
        try {
            boardVersionNodeList = getRootElement(getDocument(oldFilePath)).getElementsByTagName("PLCDeviceID");
            boardVersion = boardVersionNodeList.item(0).getTextContent();
            board = boardVersion.split("_")[2];
        } catch (Exception e) {
            System.out.println("Exception thrown while setting board type");
            System.out.println(e);
        }
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

    public static void setRawVersion() {
        NodeList softwareVersionNodeList;
        String softwareVersion;
        String[] softwareVersionSplit = null;
        try {
            softwareVersionNodeList = getRootElement(getDocument("C:/" + directory + "/" + fileDirectory + ".prm.xml")).getElementsByTagName("SoftwareVersion");
            softwareVersion = softwareVersionNodeList.item(0).getTextContent();
            softwareVersionSplit = softwareVersion.split(" ");
        } catch (Exception e) {
            System.out.println("Exception thrown while setting raw version");
            System.out.println(e);
        }
        if (softwareVersionSplit[0].equals("ACORN")) {
            version = softwareVersionSplit[3];
        } else {
            version = softwareVersionSplit[2];
        }
    }
}
