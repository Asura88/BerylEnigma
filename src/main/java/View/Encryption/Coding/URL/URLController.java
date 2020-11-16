package View.Encryption.Coding.URL;

import Controller.Encryption.Coding.URL.CodingURL;
import Init.ViewInit;
import Kit.Utils.ViewUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import javafx.fxml.FXML;

import java.io.UnsupportedEncodingException;

public class URLController {
    @FXML private JFXButton JBT_enCode;
    @FXML private JFXButton JBT_deCode;
    @FXML private JFXTextArea JTA_src;
    @FXML private JFXTextArea JTA_dst;
    @FXML private JFXComboBox JCB_charset;

    @FXML private void initialize(){
        ViewInit.comboBoxCharset(JCB_charset);
        ViewInit.textAreaErrorInfo(JTA_dst);
    }

    @FXML
    public void ONClick_JBT_enCode(){
        try {
            JTA_dst.setText(CodingURL.enCode(JTA_src.getText(),JCB_charset.getValue().toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            JTA_dst.validate();
        }
    }

    @FXML
    public void ONClick_JBT_deCode(){
        try {
            JTA_dst.setText(CodingURL.deCode(JTA_src.getText(),JCB_charset.getValue().toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            JTA_dst.validate();
        }
    }

    @FXML
    public void ResetValidator(){
        JTA_dst.resetValidation();
    }
}
