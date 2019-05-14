package ch.fhnw.mada.assesment2.view;

import ch.fhnw.mada.assesment2.huffman.Huffman;
import ch.fhnw.mada.assesment2.huffman.HuffmanBuilder;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class PresentationModel {
    public final static String DEFAULT_FOLDER = "C:\\temp";
    public final static String INPUT_FILE = "input.txt";
    public final static String HUFFAAN_CODE_STORE = "dec_tab.txt";
    public final static String ENCODED_FILE = "output.dat";
    public final static String DECODED_FILE = "decompressed.txt";
    private final static DateTimeFormatter LOG_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final StringProperty workingDirectoryProperty = new SimpleStringProperty();
    public final BooleanProperty processingProperty = new SimpleBooleanProperty();
    public final DoubleProperty processingProgressProperty = new SimpleDoubleProperty(0);
    private final ObservableList<String> logList = FXCollections.observableArrayList();
    public final ListProperty<String> logProperty = new SimpleListProperty<String>(logList);

    public String getSourceFile() {
        return workingDirectoryProperty.get() + "\\" + INPUT_FILE;
    }

    public String getHuffamnCodeFilePath() {
        return workingDirectoryProperty.get() + "\\" + HUFFAAN_CODE_STORE;
    }

    public String getEncodedFile() {
        return workingDirectoryProperty.get() + "\\" + ENCODED_FILE;
    }

    public String getDecodedFile() {
        return workingDirectoryProperty.get() + "\\" + DECODED_FILE;
    }

    public void resetDirectory() {
        workingDirectoryProperty.set(DEFAULT_FOLDER);
    }


    public void generateHuffmanCode() {
        var huffman = new HuffmanBuilder();
        try (var inputStream = new BufferedReader(new FileReader(getSourceFile()));
             var outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(getHuffamnCodeFilePath())))) {
            int current;
            while ((current = inputStream.read()) != -1) {
                huffman.add((char) current);
            }
            outputWriter.println(huffman.build().serializeHuffmanCode());
        } catch (Exception e) {

            log("ERROR - Could not read input file", 0);
            e.printStackTrace();
        }

    }


    public void decompressFile() {
        File file = new File(getEncodedFile());
        byte[] bFile = new byte[(int) file.length()];
        try (FileInputStream fis = new FileInputStream(file);
             var outputWriter = new PrintWriter(new BufferedWriter(new FileWriter(getDecodedFile())))) {
            var huffman = getHuffmanFromFile();
            outputWriter.write(huffman.decompressString(fis));

        } catch (Exception e) {

            log("ERROR - Could not read input file", 0);
            e.printStackTrace();
        }

    }

    public void compressFile() {
        try (var inputStream = new BufferedReader(new FileReader(getSourceFile()));
             FileOutputStream fos = new FileOutputStream(getEncodedFile());) {
            var huffman = getHuffmanFromFile();
            var result = inputStream.lines().map(huffman::compressString).reduce((x, y) -> x + y).orElse("");
            var resultBulilder = new StringBuilder(result);
            resultBulilder.append(1);
            while (resultBulilder.length() % 8 != 0) {
                resultBulilder.append(0);
            }
            var bytes = new ArrayList<Byte>();
            var length = resultBulilder.length();
            for (int i = 0; i < length; i += 8) {
                var str = resultBulilder.substring(i, i + 8);
                bytes.add((byte) (int) Integer.valueOf(str, 2));
            }
            for (var item : bytes) {
                fos.write(item);
            }

        } catch (Exception e) {

            log("ERROR - Could not read input file", 0);
            e.printStackTrace();
        }

    }

    private Huffman getHuffmanFromFile() throws IOException {
        try (var inputStream = new BufferedReader(new FileReader(getHuffamnCodeFilePath()));) {
            var huffman = new Huffman();
            huffman.deserializeHuffmanCode((inputStream).readLine());
            return huffman;
        } catch (Exception e) {

            log("ERROR - Could not decode huffman table", 0);
            throw e;
        }
    }


    private void log(String message, double progress) {
        Platform.runLater(() -> {
            if (!processingProperty.get()) {
                processingProperty.set(true);
            }

            logList.add(String.format("[%s] %s", LocalDateTime.now().format(LOG_DATE_FORMAT), message));
            processingProgressProperty.setValue(progress);
        });

    }

}
