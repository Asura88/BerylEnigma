package ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.FileHeadChecker;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.FileHeadChecker.Beans.FileHeadCheckerResultBean;
import ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.FileHeadChecker.Beans.FileHeaderBean;
import ffffffff0x.beryenigma.App.View.Modules.Tools.RedTeam.ReverseShellGenerator.ReverseShellGeneratorController;
import ffffffff0x.beryenigma.App.View.Viewobj.ViewController;
import ffffffff0x.beryenigma.Init.ImageListInit;
import ffffffff0x.beryenigma.Kit.Utils.ViewNode;
import ffffffff0x.beryenigma.Kit.Utils.ViewUtils;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: RyuZUSUNC
 * @create: 2023/8/16 20:11
 **/
@ViewNode(name = "FileHeadChecker",folderPath = "Root/Tools/RedTeam/",fxmlName = "FileHeadCheckerView.fxml")
public class FileHeadCheckerController extends ViewController {
    // 切换/加载 文件头信息JSON控件
    @FXML
    JFXToggleButton JTB_ModeSwitch;
    // 显示已经加载的文件头信息JSON的名称 默认为 UseDefaultHeaderJSON
    @FXML
    Label JLB_JSONName;
    // 显示JSON中文件头信息的数量
    @FXML
    Label JLB_JSONNum;
    // 显示已经加载的问价的数量
    @FXML
    Label JLB_FileNum;
    // 检测文件头十六进制数的数量 默认为4
    @FXML
    JFXTextField JTF_CheckHexNum;
    // 检测按钮 按下开始检测
    @FXML
    JFXButton JBT_Check;
    // 文件加载按钮 使用多选文件 不可选择文件夹
    @FXML
    JFXButton JBT_LoadFiles;
    // 显示所有已经加载文件的全路径
    @FXML
    JFXTextArea JTA_FileList;

    // 加载要检测文件头的文件列表
    ArrayList<File> files;

    // 缓存文件头信息-文件头信息
    HashMap<String, FileHeaderBean> fileTypes;

    // 默认使用的头文件对照表路径
    final String DefaultFileTypeJsonPath = "/json/redTeam/FileHead.json";

    // JSON序列化工具
    Gson gson = new Gson();

    // 加载文件按钮的图片
    final ImageView IMG_FileAdd = new ImageView(ViewUtils.getImage(ImageListInit.ICON_JBT_FILEADD));
    final ImageView IMG_FileAdd_M = new ImageView(ViewUtils.getImage(ImageListInit.ICON_MJBT_FILEADD));

    @Override
    protected void initialize() {
        // 初始化本地文件头信息
        String jsonData = new BufferedReader(new InputStreamReader(Objects.requireNonNull(FileHeadCheckerController.class.getResourceAsStream(DefaultFileTypeJsonPath))))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        // 保留类型信息使用的TypeToken
        Type type = new TypeToken<HashMap<String, FileHeaderBean>>(){}.getType();
        fileTypes = gson.fromJson(jsonData, type);

        // 显示默认文件头JSON文件中 文件头信息的数量
        JLB_JSONNum.setText("Num: " + fileTypes.size());

        fileLoadButtonLoadImage(JBT_LoadFiles);
    }

    @FXML
    public void ONClickLoadFiles() {
        zoomButton(JBT_LoadFiles);
        fileLoadButtonLoadSImage(JBT_LoadFiles);
        JBT_LoadFiles.setOpacity(0.8);
        LoadFilePaths();
    }

    public void LoadFilePaths() {
        files = ViewUtils.getFiles();
        StringJoiner stringJoiner = new StringJoiner("\n");
        if (files != null) {
            for (File file : files) {
                stringJoiner.add(file.getPath());
            }
            JTA_FileList.setText(stringJoiner.toString());
            JLB_FileNum.setText("FileNum: " + files.size());
        }
    }

    public void zoomButton(JFXButton jfxButton) {
        if (AnchorPane.getBottomAnchor(jfxButton) == 5.0) {
//            ViewUtils.setAnchor(jfxButton, 0.0, 0.0, 0.0, 0.0);
        } else {
            AnchorPane.clearConstraints(jfxButton);

            jfxButton.setPrefSize(40.0,40.0);

            AnchorPane.setRightAnchor(jfxButton, 20.0);
            AnchorPane.setBottomAnchor(jfxButton, 5.0);
        }
    }

    public void fileLoadButtonLoadImage(JFXButton jfxButton) {
        IMG_FileAdd.setFitHeight(290);
        IMG_FileAdd.setFitWidth(220);
        IMG_FileAdd.setPreserveRatio(true);
        jfxButton.setGraphic(IMG_FileAdd);
    }

    public void fileLoadButtonLoadSImage(JFXButton jfxButton) {
        IMG_FileAdd_M.setFitHeight(32);
        IMG_FileAdd_M.setFitWidth(32);
        IMG_FileAdd_M.setPreserveRatio(true);
        jfxButton.setGraphic(IMG_FileAdd_M);
    }

    public void buttonAnimate() {
        JBT_LoadFiles.setOnAction(actionEvent -> {
            // Create KeyValues for scaleX and scaleY properties
            KeyValue scaleXValue = new KeyValue(JBT_LoadFiles.scaleXProperty(), 0.3);
            KeyValue scaleYValue = new KeyValue(JBT_LoadFiles.scaleYProperty(), 0.3);

            // Create KeyFrame with KeyValues and duration
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.8), scaleXValue, scaleYValue);

            // Create a Timeline with the KeyFrame
            Timeline timeline = new Timeline(keyFrame);

            // Play the animation
            timeline.play();
        });
    }

    void setResultTableView() {

    }

    AnchorPane initResultPane() {
        AnchorPane resultBlackground = new AnchorPane();

        HBox hBox = new HBox();

//        hBox.getChildren().add();

        return resultBlackground;
    }

    JFXTreeTableView<BeanWrapper> initResultTableView() {
        final TreeItem<BeanWrapper> root = new RecursiveTreeItem<>(
                FXCollections.observableArrayList(FileHeadCheckerImpl.getFileTypes(files, fileTypes)),
                RecursiveTreeObject::getChildren);
        JFXTreeTableView<BeanWrapper> resultTreeTableView = new JFXTreeTableView<>();
        resultTreeTableView.setShowRoot(false);

        JFXTreeTableColumn<BeanWrapper, String> fileName = new JFXTreeTableColumn<>("fileName");
        JFXTreeTableColumn<BeanWrapper, String> filePath = new JFXTreeTableColumn<>("filePath");
        JFXTreeTableColumn<BeanWrapper, String> extName = new JFXTreeTableColumn<>("extName");
        JFXTreeTableColumn<BeanWrapper, String> fileHeaderHEX = new JFXTreeTableColumn<>("fileHeaderHEX");
        JFXTreeTableColumn<BeanWrapper, String> fileDescription = new JFXTreeTableColumn<>("fileDescription");

        resultTreeTableView.getColumns().setAll(fileName, filePath, extName, fileHeaderHEX, fileDescription);


        fileName.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileHeadCheckerController.BeanWrapper, String> param) -> {
            if (fileName.validateValue(param)) {
                return param.getValue().getValue().fileNameProperty;
            } else {
                return fileName.getComputedValue(param);
            }
        });

        filePath.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileHeadCheckerController.BeanWrapper, String> param) -> {
            if (filePath.validateValue(param)) {
                return param.getValue().getValue().filePathProperty;
            } else {
                return filePath.getComputedValue(param);
            }
        });

        extName.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileHeadCheckerController.BeanWrapper, String> param) -> {
            if (extName.validateValue(param)) {
                return param.getValue().getValue().extensionNameProperty;
            } else {
                return extName.getComputedValue(param);
            }
        });

        fileHeaderHEX.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileHeadCheckerController.BeanWrapper, String> param) -> {
            if (fileHeaderHEX.validateValue(param)) {
                return param.getValue().getValue().fileHeaderHEXProperty;
            } else {
                return fileHeaderHEX.getComputedValue(param);
            }
        });

        fileDescription.setCellValueFactory((TreeTableColumn.CellDataFeatures<FileHeadCheckerController.BeanWrapper, String> param) -> {
            if (fileDescription.validateValue(param)) {
                return param.getValue().getValue().fileDescriptionProperty;
            } else {
                return fileDescription.getComputedValue(param);
            }
        });

        return resultTreeTableView;
    }

    // 最终结果显示用bean类
    class BeanWrapper extends RecursiveTreeObject<BeanWrapper> {
        private final SimpleStringProperty filePathProperty = new SimpleStringProperty();
        private final SimpleStringProperty fileNameProperty = new SimpleStringProperty();
        private final SimpleStringProperty extensionNameProperty = new SimpleStringProperty();
        private final SimpleStringProperty fileHeaderHEXProperty = new SimpleStringProperty();
        private final SimpleStringProperty fileDescriptionProperty = new SimpleStringProperty();

        public BeanWrapper(FileHeadCheckerResultBean fileHeadCheckerResultBean) {
            filePathProperty.set(fileHeadCheckerResultBean.getFilePath());
            fileNameProperty.set(fileHeadCheckerResultBean.getFileName());
            extensionNameProperty.set(fileHeadCheckerResultBean.getExtensionName());
            fileHeaderHEXProperty.set(fileHeadCheckerResultBean.getFileHeaderHEX());
            fileDescriptionProperty.set(fileHeadCheckerResultBean.getFileDescription());
        }

        public String getFilePathProperty() {
            return filePathProperty.get();
        }

        public SimpleStringProperty filePathPropertyProperty() {
            return filePathProperty;
        }

        public String getFileNameProperty() {
            return fileNameProperty.get();
        }

        public SimpleStringProperty fileNamePropertyProperty() {
            return fileNameProperty;
        }

        public String getExtensionNameProperty() {
            return extensionNameProperty.get();
        }

        public SimpleStringProperty extensionNamePropertyProperty() {
            return extensionNameProperty;
        }

        public String getFileHeaderHEXProperty() {
            return fileHeaderHEXProperty.get();
        }

        public SimpleStringProperty fileHeaderHEXPropertyProperty() {
            return fileHeaderHEXProperty;
        }

        public String getFileDescriptionProperty() {
            return fileDescriptionProperty.get();
        }

        public SimpleStringProperty fileDescriptionPropertyProperty() {
            return fileDescriptionProperty;
        }
    }
}
