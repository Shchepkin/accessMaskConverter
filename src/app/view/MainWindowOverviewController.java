package app.view;

import app.model.Actions;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static app.model.Actions.fromCheckBoxesToHex;
import static app.model.Actions.fromHexToCheckBoxes;

public class MainWindowOverviewController implements Initializable {
    private Actions action = new Actions();
    private Map preset = new HashMap<String, String>();
    private Map bitLabelsMap = new HashMap<String, String>();

    @FXML
    private CheckBox bit0, bit1, bit2, bit3, bit4, bit5, bit6, bit7;
    @FXML
    private CheckBox bit8, bit9, bit10, bit11, bit12, bit13, bit14, bit15;
    @FXML
    private CheckBox bit16, bit17, bit18, bit19, bit20, bit21, bit22, bit23;
    @FXML
    private CheckBox bit24, bit25, bit26, bit27, bit28, bit29, bit30, bit31;

    @FXML
    private Label labelBit0, labelBit1, labelBit2, labelBit3, labelBit4, labelBit5, labelBit6, labelBit7;
    @FXML
    private Label labelBit8, labelBit9, labelBit10, labelBit11, labelBit12, labelBit13, labelBit14, labelBit15;
    @FXML
    private Label labelBit16, labelBit17, labelBit18, labelBit19, labelBit20, labelBit21, labelBit22, labelBit23;
    @FXML
    private Label labelBit24, labelBit25, labelBit26, labelBit27, labelBit28, labelBit29, labelBit30, labelBit31;



    @FXML
    private Button presetPro, presetProWithoutPermissions;
    @FXML
    private Button presetMaster, presetMasterWithoutPermissions;
    @FXML
    private Button presetUser;
    @FXML
    private Button convert, clear;

    @FXML
    private TextField accessMaskField;

    @FXML
    private Pane paneCheckboxContainer;

    @FXML
    private Pane paneBitLabelsContainer;

    private List<CheckBox> allCheckBoxes = new ArrayList<>();
    private List<Label> allBitLabels = new ArrayList<>();

    @FXML
    public void setPresetPro() {
        if (preset.isEmpty()) {
            System.out.println("set PresetUser as hardCode value ffffffff");
            accessMaskField.setText("ffffffff");
        } else {
            accessMaskField.setText(preset.get("Pro").toString());
        }
    }

    @FXML
    public void setPresetProWithoutPermissions() {
        if (preset.isEmpty()) {
            System.out.println("set PresetUser as hardCode value 00000000");
            accessMaskField.setText("00000000");
        } else {
            accessMaskField.setText(preset.get("ProWithoutPermissions").toString());
        }
    }

    @FXML
    public void setPresetMaster() {
        if (preset.isEmpty()) {
            System.out.println("set PresetUser as hardCode value ffffffff");
            accessMaskField.setText("ffffffff");
        } else {
            accessMaskField.setText(preset.get("Master").toString());
        }
    }

    @FXML
    public void setPresetMasterWithoutPermissions() {
        if (preset.isEmpty()) {
            System.out.println("set PresetUser as hardCode value 00090f26");
            accessMaskField.setText("00090f26");
        } else {
            accessMaskField.setText(preset.get("MasterWithoutPermissions").toString());
        }
    }

    @FXML
    public void setPresetUser() {
        if (preset.isEmpty()) {
            System.out.println("set PresetUser as hardCode value 00010f00");
            accessMaskField.setText("00010f00");
        } else {
            accessMaskField.setText(preset.get("User").toString());
        }
    }

    @FXML
    public void clearField() {
        accessMaskField.clear();
    }

    @FXML
    public void startConvert() {
        if (accessMaskField.getText().isEmpty()) {
            accessMaskField.setText(fromCheckBoxesToHex(getAllCheckBoxesFromPane()));
        } else {
            fromHexToCheckBoxes(accessMaskField.getText(), getAllCheckBoxesFromPane());
        }
    }

    private List<CheckBox> getAllCheckBoxesFromPane() {
        allCheckBoxes.clear();
        ObservableList<Node> nodes = (paneCheckboxContainer.getChildren());
        allCheckBoxes.addAll(nodes.stream().map(node -> (CheckBox) node).collect(Collectors.toList()));
        return allCheckBoxes;
    }

    private List<Label> getAllBitLabelsElements() {
        allBitLabels.clear();
        ObservableList<Node> nodes = (paneBitLabelsContainer.getChildren());
        allBitLabels.addAll(nodes.stream().map(node -> (Label) node).collect(Collectors.toList()));
        return allBitLabels;
    }

    private static void addTextFieldValidationListener(final TextField tf, final int maxLength) {
        String numberMatcher = "[0-9AaBbCcDdEeFf]*";
        tf.textProperty().addListener((ov, oldValue, newValue) -> {

            // text must be maximum 8 symbols and contains only HEX symbols
            if (tf.getText().length() > maxLength || !newValue.matches(numberMatcher)) {
                tf.setText(oldValue);
            } else {
                tf.setText(newValue.toUpperCase());
            }
        });
    }

    private void setBitLabels() {
        if (!bitLabelsMap.isEmpty()) {
            getAllBitLabelsElements();
            for (Label bitLabel : allBitLabels) {
                System.out.println("set label for " + bitLabel.getId() + " as " + bitLabelsMap.get(bitLabel.getId()));
                bitLabel.setText(bitLabelsMap.get(bitLabel.getId()).toString());
            }
        }else System.out.println("can't read Labels from settings file");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String presetCollection = "preset";
        String labelsCollection = "bitLabels";

        addTextFieldValidationListener(accessMaskField, 8);
        try {
            preset = action.getCollectionFromJson("settings.json", presetCollection);
            bitLabelsMap = action.getCollectionFromJson("settings.json", labelsCollection);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.out.println("some collections was not found in settings file");
            e.printStackTrace();
        }
        setBitLabels();
    }
}
