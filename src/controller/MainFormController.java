package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFormController {
    public MenuBar menuBar;
    public TextArea txtArea;
    public AnchorPane anchorPaneMainForm;
    public Button btnNewFile;
    public Button btnOpenFile;
    public Button btnSaveFile;
    public Button btnCut;
    public Button btnCopy;
    public Button btnPaste;
    public TextField txtFind;
    public TextField txtReplace;
    public Button btnUpArrow;
    public Button btnDownArrow;
    public Button btnReplace;
    public ToggleButton btnRegex;
    public ToggleButton btnCaseSensitive;
    public Label lblWordCount;
    public Label lblFindCount;
    public static int untitledFileCount = 1;

    Path currentPath;
    boolean txtChanged=false;
    Matcher matcher;
    ArrayList<Integer> referBack = new ArrayList();
    ArrayList<Integer> referForward = new ArrayList();

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

        btnNewFile.setTooltip(new Tooltip("New"));
        btnOpenFile.setTooltip(new Tooltip("Open"));
        btnSaveFile.setTooltip(new Tooltip("Save"));
        btnCut.setTooltip(new Tooltip("Cut"));
        btnCopy.setTooltip(new Tooltip("Copy"));
        btnPaste.setTooltip(new Tooltip("Paste"));

        newFile.setOnAction(event ->{
            btnNewFile.fire();
        });
        open.setOnAction(event -> {
            btnOpenFile.fire();
        });
        save.setOnAction(event -> {
            btnSaveFile.fire();
        });
        print.setOnAction(event -> {
            //---------------Print text file-----------------
        });
        exit.setOnAction(event -> {
            System.exit(0);
        });

        cut.setOnAction(event -> {
            btnCut.fire();
        });
        copy.setOnAction(event -> {
            btnCopy.fire();
        });
        paste.setOnAction(event -> {
            btnPaste.fire();
        });
        selectAll.setOnAction(event -> {
            txtArea.selectAll();
        });

        txtArea.textProperty().addListener((observable, oldValue, newValue) -> {
            setWordCount();
            txtChanged=true;
            findWords();
        });

        txtFind.textProperty().addListener((observable, oldValue, newValue) -> {
            findWords();
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

    public static String setFileName(){
        String fileName="Untitled document"+untitledFileCount;
        untitledFileCount++;
        return fileName;
    }

    private void saveFile(File file) throws IOException {
        Path path = Paths.get(String.valueOf(file));
        OutputStream outputStream = Files.newOutputStream(path);
        String newString=txtArea.getText();
        outputStream.write(newString.getBytes());
    }

    private void readData(File file) throws IOException {
        Path path = Paths.get(String.valueOf(file));
        try {
            InputStream inputStream = Files.newInputStream(path);
            byte[] fileBytes = new byte[inputStream.available()];
            inputStream.read(fileBytes);
            String fileContent=new String(fileBytes);
            txtArea.setText(fileContent);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnNewFileClickOnAction(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setMinHeight(800);
        stage.setMinWidth(800);
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle(setFileName());
        stage.show();
    }

    public void btnOpenFileClickOnAction(ActionEvent actionEvent) {
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
    }

    public void btnSaveFileClickOnAction(ActionEvent actionEvent) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.setTitle("Select a destination");
        File file = fileChooser.showSaveDialog(null);
        try {
            saveFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnCutClickOnAction(ActionEvent actionEvent) {
        if(txtArea.getSelectedText()!=null){
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(txtArea.getSelectedText());
            systemClipboard.setContent(clipboardContent);
            txtArea.setText(txtArea.getText().replaceAll(clipboardContent.getString(),""));
        }
    }

    public void btnCopyClickOnAction(ActionEvent actionEvent) {
        if(txtArea.getSelectedText()!=null){
            Clipboard systemClipboard = Clipboard.getSystemClipboard();
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString(txtArea.getSelectedText());
            systemClipboard.setContent(clipboardContent);
        }
    }

    public void btnPasteClickOnAction(ActionEvent actionEvent) {
        Clipboard pasteClip=Clipboard.getSystemClipboard();
        int caretPosition = txtArea.getCaretPosition();
        txtArea.insertText(caretPosition, pasteClip.getString());
    }

    private void findWords() {
        if (!txtFind.getText().isEmpty()){
            if (txtChanged){
                int flag=0;
                if (!btnCaseSensitive.isSelected()){
                    flag=flag | Pattern.CASE_INSENSITIVE;
                }
                if (!btnRegex.isSelected()){
                    flag=flag | Pattern.LITERAL;
                }
                matcher = Pattern.compile(txtFind.getText(),flag).matcher(txtArea.getText());
                foundedWords();
                txtChanged=false;
                System.gc();
            }
            if (referForward.size()>2){
                txtArea.selectRange(referForward.get(referForward.size()-4),referForward.get(referForward.size()-3));
                referForward.remove(referForward.size()-4);
                referForward.remove(referForward.size()-3);

            }else if(matcher.find()){
                referForward.clear();
                txtArea.selectRange(matcher.start(),matcher.end());
                referBack.add(matcher.start());
                referBack.add(matcher.end());
            }else {
                matcher.reset();
            }
        }
    }

    private void setWordCount() {
        if(txtArea.getText().isEmpty()){
            lblWordCount.setText(String.valueOf(0));
            return;
        }
        int count=0;
        Matcher matcher = Pattern.compile("\\S+").matcher(txtArea.getText());
        while(matcher.find()){
            count++;
        }
        lblWordCount.setText(String.valueOf(count));
    }

    private void foundedWords() {
        int count =0;
        if(matcher!=null){
            while(matcher.find()){
                count++;
            }
            matcher.reset();
            lblFindCount.setText(String.valueOf(count));
        }
    }

    public void btnDownArrowClickOnAction(ActionEvent actionEvent) {
        findWords();
    }

    public void btnUpArrowClickOnAction(ActionEvent actionEvent) {
        if (referForward.size()==0){
            referForward.add(referBack.get(referBack.size()-2));
            referForward.add(referBack.get(referBack.size()-1));
        }
        txtArea.selectRange(referBack.get((referBack.size())-4),referBack.get((referBack.size()-3)));
        referForward.add(referBack.get((referBack.size()-4)));
        referForward.add(referBack.get((referBack.size()-3)));
        referBack.remove(referBack.size()-4);
        referBack.remove(referBack.size()-3);
    }

    public void btnReplaceClickOnAction(ActionEvent actionEvent) {
        if (!txtFind.getText().isEmpty()){
            txtArea.setText(txtArea.getText().replaceAll(txtFind.getText(),txtReplace.getText()));
        }
    }

    public void btnRegexClickOnAction(ActionEvent actionEvent) {
        txtChanged=true;
        findWords();
    }

    public void btnCaseSensitiveClickOnAction(ActionEvent actionEvent) {
        txtChanged=true;
        findWords();
    }
}
