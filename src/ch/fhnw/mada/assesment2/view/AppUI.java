package ch.fhnw.mada.assesment2.view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class AppUI extends VBox {
    private static final String INTRO_TEMPLATE = "This application uses the folder %s. Make sure this folder exists. All files are stored in this folder.";
    private static final String GENERATE_DESCRIPTION_TEMPLATE = "Creates a huffman table of %s and  stores it under %s";
    private static final String COMPRESS_FILE_TEMPLATE = "Compresses the file %s with the huffman code from  %s and outputs into %s";
    private static final String DECOMPRESS_FILE_TEMPLATE = "Decompresss the file %s with the huffman code from  %s and outputs into %s";
    private static final int PADDING_SIDES = 25;
    private Text title;
    private Text intro;
    private StackPane titleLayout;
    private PresentationModel pm;
    private Stage stage;

    private Text generateDescription;
    private Button generateButton;
    private GridPane generateLayout;
    private Text generateLabel;

    private Text encryptFileDescription;
    private Button encryptFileButton;
    private GridPane encryptFileLayout;
    private Text encryptFileLabel;


    private Text decryptFileDescription;
    private Button decryptFileButton;
    private GridPane decryptFileLayout;
    private Text decryptFileLabel;

    private DirectoryChooser mainDirectoryChooser;
    private TextField mainDirectoryTextField;
    private Button mainDirectoryButton;
    private HBox mainDirectoryLayout;
    private Text mainDirectoryLabel;

    private Text progressLabel;
    private ProgressBar progressBar;
    private ListView<String> logView;

    public AppUI(PresentationModel pm, Stage stage) {
        this.pm = pm;
        this.stage = stage;
        initializeControls();
        layoutControls();
    }

    private void initializeControls() {
        titleLayout = new StackPane();
        intro = new Text("");
        title = new Text("Huffmann Assement");
        widthProperty().addListener((o, old, newValue) -> intro.setWrappingWidth(newValue.doubleValue() - PADDING_SIDES * 2));
        pm.workingDirectoryProperty.addListener((o, old, newValue) -> intro.setText(String.format(INTRO_TEMPLATE, newValue)));

        generateDescription = new Text();
        widthProperty().addListener((o, old, newValue) -> generateDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100));
        generateButton = new Button("Generate");
        generateLayout = new GridPane();
        generateLabel = new Text("Generate Huffamn Table");
        generateButton.setOnAction(x -> pm.generateHuffmanCode());
        pm.workingDirectoryProperty.addListener((o, old, newValue) -> generateDescription.setText(String.format(GENERATE_DESCRIPTION_TEMPLATE, pm.getSourceFile(), pm.getHuffamnCodeFilePath())));

        encryptFileDescription = new Text();
        widthProperty().addListener((o, old, newValue) -> encryptFileDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100));
        encryptFileButton = new Button("Compress");
        encryptFileLayout = new GridPane();
        encryptFileLabel = new Text("Compress File");
        encryptFileButton.setOnAction(x -> pm.compressFile());
        pm.workingDirectoryProperty.addListener((o, old, newValue) -> encryptFileDescription.setText(String.format(COMPRESS_FILE_TEMPLATE, pm.getSourceFile(), pm.getHuffamnCodeFilePath(), pm.getEncodedFile())));


        decryptFileDescription = new Text();
        widthProperty().addListener((o, old, newValue) -> decryptFileDescription.setWrappingWidth(newValue.doubleValue() < 200 ? 100 : newValue.doubleValue() - 100));
        decryptFileButton = new Button("Decrypt");
        decryptFileLayout = new GridPane();
        decryptFileLabel = new Text("Decrypt File");
        decryptFileButton.setOnAction(x -> pm.decompressFile());
        pm.workingDirectoryProperty.addListener((o, old, newValue) -> decryptFileDescription.setText(String.format(DECOMPRESS_FILE_TEMPLATE, pm.getEncodedFile(), pm.getHuffamnCodeFilePath(), pm.getDecodedFile())));


        mainDirectoryChooser = new DirectoryChooser();
        mainDirectoryLayout = new HBox();
        mainDirectoryButton = new Button("Select");
        mainDirectoryTextField = new TextField();
        mainDirectoryLabel = new Text("Directory");
        mainDirectoryButton.setOnAction(x -> {
            var directory = mainDirectoryChooser.showDialog(stage);
            if (directory != null) {
                pm.workingDirectoryProperty.set(directory.getPath());
            }
        });
        mainDirectoryTextField.textProperty().bind(pm.workingDirectoryProperty);

        progressLabel = new Text("Progress");
        progressBar = new ProgressBar(0);
        progressBar.progressProperty().bind(pm.processingProgressProperty);

        logView = new ListView<>();
        logView.itemsProperty().bind(pm.logProperty);

        pm.processingProperty.addListener((o, old, newValue) -> {
            progressBar.setDisable(newValue);
        });

        pm.resetDirectory();
    }

    private void layoutControls() {

        setPadding(new Insets(5, PADDING_SIDES, 5, PADDING_SIDES));

        title.setFont(Font.font("Segoe UI", 20));
        titleLayout.getChildren().add(title);


        mainDirectoryLayout.getChildren().addAll(mainDirectoryTextField, mainDirectoryButton);
        HBox.setHgrow(mainDirectoryTextField, Priority.ALWAYS);
        layoutLabel(mainDirectoryLabel);

        setMargin(progressBar, new Insets(10, 0, 10, 0));
        progressBar.setMinHeight(20);
        progressBar.prefWidthProperty().bind(widthProperty().subtract(2 * PADDING_SIDES));
        progressBar.setMaxWidth(USE_PREF_SIZE);
        progressBar.setMinWidth(USE_PREF_SIZE);
        layoutLabel(progressLabel);

        layoutButton(generateLayout, generateLabel, generateButton, generateDescription);
        layoutButton(encryptFileLayout, encryptFileLabel, encryptFileButton, encryptFileDescription);
        layoutButton(decryptFileLayout, decryptFileLabel, decryptFileButton, decryptFileDescription);

        getChildren().addAll(titleLayout, mainDirectoryLabel, mainDirectoryLayout, intro, generateLayout, encryptFileLayout, decryptFileLayout, progressLabel, progressBar, logView);
    }

    private void layoutButton(GridPane layout, Text label, Button button, Text buttonDescirption) {

        layoutLabel(label);
        layout.add(label, 0, 0);
        layout.add(button, 0, 1);
        layout.add(buttonDescirption, 1, 1);
        layout.getColumnConstraints().add(new ColumnConstraints(100));
        setMargin(layout, new Insets(10, 0, 0, 0));

    }

    private Text layoutLabel(Text label) {
        label.setFont(Font.font("Segoe UI", 15));
        return label;
    }


}
