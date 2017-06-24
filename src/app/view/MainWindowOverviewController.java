package app.view;

        import app.model.Actions;
        import javafx.collections.ObservableList;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.Node;
        import javafx.scene.control.Button;
        import javafx.scene.control.CheckBox;
        import javafx.scene.control.SplitPane;
        import javafx.scene.control.TextField;
        import javafx.scene.layout.AnchorPane;

        import java.io.FileNotFoundException;
        import java.net.URL;
        import java.util.*;
        import java.util.stream.Collectors;

        import static app.model.Actions.*;

public class MainWindowOverviewController implements Initializable {
    private Actions action = new Actions();
    private Map preset = new HashMap<String, String>();

    @FXML
    private CheckBox bit0, bit1, bit2, bit3, bit4, bit5, bit6, bit7;
    @FXML
    private CheckBox bit8, bit9, bit10, bit11, bit12, bit13, bit14, bit15;
    @FXML
    private CheckBox bit16, bit17, bit18, bit19, bit20, bit21, bit22, bit23;
    @FXML
    private CheckBox bit24, bit25, bit26, bit27, bit28, bit29, bit30, bit31;

    @FXML
    private Button presetPro, presetProWithoutPermissions;
    @FXML
    private Button presetMaster, presetMasterWithoutPermissions;
    @FXML
    private Button presetUser;
    @FXML
    private Button Convert, Clear;

    @FXML
    private TextField accessMaskField;

    @FXML
    private SplitPane CheckboxContainer;

    private List<CheckBox> allCheckBoxes = new ArrayList<>();

    @FXML
    public void setPresetPro() {
        if (preset.isEmpty()){
            System.out.println("set PresetUser as hardCode value ffffffff");
            accessMaskField.setText("ffffffff");
        }else {
            accessMaskField.setText(preset.get("Pro").toString());
        }
    }

    @FXML
    public void setPresetProWithoutPermissions() {
        if (preset.isEmpty()){
            System.out.println("set PresetUser as hardCode value 00000000");
            accessMaskField.setText("00000000");
        }else {
            accessMaskField.setText(preset.get("ProWithoutPermissions").toString());
        }
    }

    @FXML
    public void setPresetMaster() {
        if (preset.isEmpty()){
            System.out.println("set PresetUser as hardCode value ffffffff");
            accessMaskField.setText("ffffffff");
        }else {
            accessMaskField.setText(preset.get("Master").toString());
        }
    }

    @FXML
    public void setPresetMasterWithoutPermissions() {
        if (preset.isEmpty()){
            System.out.println("set PresetUser as hardCode value 00090f26");
            accessMaskField.setText("00090f26");
        }else {
            accessMaskField.setText(preset.get("MasterWithoutPermissions").toString());
        }
    }

    @FXML
    public void setPresetUser() {
        if (preset.isEmpty()){
            System.out.println("set PresetUser as hardCode value 00010f00");
            accessMaskField.setText("00010f00");
        }else {
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
            accessMaskField.setText(fromCheckBoxesToHex(getAllCheckBoxes()));
        } else {
            fromHexToCheckBoxes(accessMaskField.getText(), getAllCheckBoxes());
        }
    }

    private List<CheckBox> getAllCheckBoxes(){
        allCheckBoxes.clear();
        for (int i = 0; i < CheckboxContainer.getItems().size(); i++) {
            ObservableList<Node> nodes = ((AnchorPane) CheckboxContainer.getItems().get(i)).getChildren().;
            allCheckBoxes.addAll(nodes.stream().map(node -> (CheckBox) node).collect(Collectors.toList()));
        }
        return allCheckBoxes;
    }

    private static void addTextFieldValidationListener(final TextField tf, final int maxLength) {
        String numberMatcher = "[0-9AaBbCcDdEeFf]*";
        tf.textProperty().addListener((ov, oldValue, newValue) -> {

            // text must be maximum 8 symbols and contains only HEX symbols
            if (tf.getText().length() > maxLength || !newValue.matches(numberMatcher)) {
                tf.setText(oldValue);
            }else {
                tf.setText(newValue.toUpperCase());
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addTextFieldValidationListener(accessMaskField, 8);
        try {
            preset = action.getCollectionFromJson("settings.json", "preset");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
