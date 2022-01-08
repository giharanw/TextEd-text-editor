package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainFormController {
    public MenuBar menuBar;
    public TextArea txtArea;
    public AnchorPane anchorPaneMainForm;

    public void initialize() {

        MenuItem newFile = new MenuItem("New");
        MenuItem open = new MenuItem("Open");
        MenuItem save = new MenuItem("Save");
        MenuItem print = new MenuItem("Print");
        MenuItem exit = new MenuItem("Exit");

        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");
        MenuItem selectAll = new MenuItem("Select All");

        MenuItem about = new MenuItem("About");

        menuBar.getMenus().get(0).getItems().add(newFile);
        menuBar.getMenus().get(0).getItems().add(open);
        menuBar.getMenus().get(0).getItems().add(save);
        menuBar.getMenus().get(0).getItems().add(print);
        menuBar.getMenus().get(0).getItems().add(exit);
        menuBar.getMenus().get(0).getItems().remove(0);

        menuBar.getMenus().get(1).getItems().add(cut);
        menuBar.getMenus().get(1).getItems().add(copy);
        menuBar.getMenus().get(1).getItems().add(paste);
        menuBar.getMenus().get(1).getItems().add(selectAll);
        menuBar.getMenus().get(1).getItems().remove(0);

        menuBar.getMenus().get(2).getItems().add(about);
        menuBar.getMenus().get(2).getItems().remove(0);

//        MenuItem about = menuBar.getMenus().get(2).getItems().get(0);

        newFile.setOnAction(event ->{

            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.show();
        });

        open.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open a file");
            fileChooser.getExtensionFilters().
                    add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            File file = fileChooser.showOpenDialog(null);
            try {
                readData(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        save.setOnAction(event -> {
            FileChooser fileChooser=new FileChooser();
            fileChooser.setTitle("Select a destination");
            File file = fileChooser.showSaveDialog(null);
            try {
                saveFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        exit.setOnAction(event -> {
            System.exit(0);
        });

        selectAll.setOnAction(event -> {
            txtArea.selectAll();
        });

        cut.setOnAction(event -> {
            if(txtArea.getSelectedText()!=null){
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(txtArea.getSelectedText());
                systemClipboard.setContent(clipboardContent);
                txtArea.setText(txtArea.getText().replaceAll(clipboardContent.getString(),""));
            }
        });

        copy.setOnAction(event -> {
            if(txtArea.getSelectedText()!=null){
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(txtArea.getSelectedText());
                systemClipboard.setContent(clipboardContent);
            }
        });

        paste.setOnAction(event -> {
            Clipboard pasteClip=Clipboard.getSystemClipboard();
            txtArea.setText(txtArea.getText()+pasteClip.getString());
        });

        about.setOnAction(event -> {
            anchorPaneMainForm.setVisible(false);
//            Stage stage = new Stage();
            Stage stage = (Stage) anchorPaneMainForm.getScene().getWindow();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/AboutForm.fxml"))));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.setResizable(false);
            stage.setTitle("About TextEd");
            stage.show();
        });
    }
    private void saveFile(File file) throws IOException {
        Path path = Paths.get(String.valueOf(file));
        OutputStream outputStream = Files.newOutputStream(path);
        String newString=txtArea.getText();
        outputStream.write(newString.getBytes());
    }

    private void readData(File file) throws IOException {
        Path path = Paths.get(String.valueOf(file));
        InputStream inputStream = Files.newInputStream(path);
        byte[] fileBytes = new byte[inputStream.available()];
        inputStream.read(fileBytes);
        String fileContent=new String(fileBytes);
        txtArea.setText(fileContent);
    }
}
