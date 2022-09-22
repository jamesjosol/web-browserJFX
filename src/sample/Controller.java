package sample;

import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.util.Callback;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    String htLink = "http://";

    @FXML
    TextField addressBar;

    @FXML
    WebView webView;

    public static WebEngine webEngine;
    WebHistory history;

    @FXML
    Button prevBtn;
    @FXML
    Button forwardBtn;
    @FXML
    Button reloadBtn;

    @FXML
    ComboBox<Entry> historyList = new ComboBox<>();

    public void onGo(ActionEvent ev) {
        load();
    }

    public void onEnterPressed(KeyEvent ke) {
        if(ke.getCode() == KeyCode.ENTER) {
            load();
        }
    }

    private void load() {
        if(!addressBar.getText().isEmpty()) {
            String addressLink = addressBar.getText();
            String newAddr;
            if(addressLink.startsWith("http:")) {
                newAddr = addressLink.replace("http://", "");
            }else if(addressLink.startsWith("https:")) {
                newAddr = addressLink.replace("https://", "");
            }else {
                newAddr = "http://" + addressLink;
            }

            webEngine.load(newAddr);
            System.out.println("button clicked");
            System.out.println(newAddr);
        }
    }

    public void onPrevious(ActionEvent ev) {
        history.go(-1);
    }

    public void onForward(ActionEvent ev) {
        history.go(1);
    }

    public void onReload(ActionEvent ev) {
        webEngine.reload();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = webView.getEngine();
        history = webEngine.getHistory();
        webEngine.load(htLink+"www.google.com");
        addressBar.setText("www.google.com");
        Stage stage = Main.getPrimaryStage();

        webEngine.titleProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                                final String oldvalue, final String newvalue) {
                // Set the Title of the Stage
                stage.setTitle(newvalue!=null ? newvalue+" - JavaFX Browser" : "JavaFX Browser");
//                String webLoc = webEngine.getLocation();
//                String url = webLoc.substring(7,10).equals("http") ? webLoc.substring(7, webLoc.length()) : webLoc;
                addressBar.setText(webEngine.getLocation());

            }
        });

        forwardBtn.setDisable(true);
        prevBtn.setDisable(true);
        history.currentIndexProperty().addListener(new ChangeListener<Number>()
        {
            public void changed(ObservableValue<? extends Number> ov,
                                final Number oldvalue, final Number newvalue) {
                int currentIndex = newvalue.intValue();

                if (currentIndex <= 0) {
                    prevBtn.setDisable(true);
                }
                else {
                    prevBtn.setDisable(false);
                }

                if (currentIndex >= history.getEntries().size()) {
                    forwardBtn.setDisable(true);
                }
                else {
                    forwardBtn.setDisable(false);
                }
            }
        });

        historyList.setPrefWidth(150);
        historyList.setItems(history.getEntries());

        historyList.setCellFactory(new Callback<ListView<WebHistory.Entry>, ListCell<WebHistory.Entry>>() {
            @Override public ListCell<WebHistory.Entry> call(ListView<WebHistory.Entry> list) {
                ListCell<Entry> cell = new ListCell<Entry>() {
                    @Override
                    public void updateItem(Entry item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        }else {
                            String pageTitle = item.getTitle();
                            this.setText(pageTitle);
                        }
                    }
                };

                return cell;
            }
        });

        historyList.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                int currentIndex = history.getCurrentIndex();

                Entry selectedEntry = historyList.getValue();

                int selectedIndex = historyList.getItems().indexOf(selectedEntry);
                int offset = selectedIndex - currentIndex;
                history.go(offset);
            }
        });

        addressBar.requestFocus();
        Text leftArrowIcon = GlyphsDude.createIcon(FontAwesomeIcons.ARROW_LEFT, "15px");
        Text rightArrowIcon = GlyphsDude.createIcon(FontAwesomeIcons.ARROW_RIGHT, "15px");
        Text reloadIcon = GlyphsDude.createIcon(FontAwesomeIcons.REFRESH, "15px");

        leftArrowIcon.setId("arrow-icon");
        rightArrowIcon.setId("arrow-icon");
        reloadIcon.setId("arrow-icon");
        prevBtn.setGraphic(leftArrowIcon);
        forwardBtn.setGraphic(rightArrowIcon);
        reloadBtn.setGraphic(reloadIcon);
    }
}
