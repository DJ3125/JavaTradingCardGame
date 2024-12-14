module com.example.tradingcardgame_9_20_23 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tradingcardgame_9_20_23 to javafx.fxml;
    exports com.example.tradingcardgame_9_20_23;
}