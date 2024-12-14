package com.example.tradingcardgame_9_20_23;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

//Fix the money labels

public class HelloController{
    //FXML elements
    @FXML public ListView<String> ViewingCardsListView, BuyingCardsListView, SellingCardsListView, MyTradingListView, TraderListView, ViewingPeopleListView, SellingPeopleListView, FirstPeopleListView, SecondPeopleListView, BuyingPeopleListView;
    @FXML public ImageView ViewingImageView, BuyingImageView, SellingImageView, MyTradeImageView, TheirTradeImageView;
    @FXML public Label ViewingLabel, DisplayNameLabel, BuyingMoneyLabel, BuyingInfoLabel, SellingMoneyLabel, SellingInfoLabel, TradingInfoLabel;
    @FXML public HBox IntroHBox;
    @FXML public TabPane MyTabPane;
    @FXML public TextField NameTextBox;
    @FXML public Button SubmitNameButton, ConfirmPurchaseButton, ConfirmSellingButton, ConfirmTradeButton;
    @FXML public Slider BuyingSlider, SellingSlider, MyTradingSlider, MyAmountDesiredSlider;

    @FXML public void handleConfirmTrade(){ //Performs the trade once the button is clicked
        getTraderFromListView(FirstPeopleListView).performTrade(getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), (int)MyAmountDesiredSlider.getValue(), (int)MyTradingSlider.getValue());
        getTraderFromListView(SecondPeopleListView).performTrade(getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), (int)MyTradingSlider.getValue(), (int)MyAmountDesiredSlider.getValue());
        resetAll();
    }

    @FXML public void handleTradeAmountChange(){
        TradingInfoLabel.setText(getTraderFromListView(FirstPeopleListView).getUserName() + " is trading with " + getTraderFromListView(SecondPeopleListView).getUserName() + "\nThe trade is " + (int)MyTradingSlider.getValue() + " " + MyTradingListView.getSelectionModel().getSelectedItem() + "s for " + (int)MyAmountDesiredSlider.getValue() + " " + TraderListView.getSelectionModel().getSelectedItem() + "s");
        if(Math.abs(getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()).calculateValue() * MyTradingSlider.getValue() - MyAmountDesiredSlider.getValue() * getDinoReference(TraderListView.getSelectionModel().getSelectedItem()).calculateValue()) <= 0.1 * (getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()).calculateValue() * MyTradingSlider.getValue() + MyAmountDesiredSlider.getValue() * getDinoReference(TraderListView.getSelectionModel().getSelectedItem()).calculateValue())){
            String errorLabel = "";

            //Check if the trade can be made by making the trade.
            if(!getTraderFromListView(FirstPeopleListView).performTrade(getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), (int)MyAmountDesiredSlider.getValue(), (int)MyTradingSlider.getValue())){errorLabel += getTraderFromListView(FirstPeopleListView).getUserName();}
            else{getTraderFromListView(FirstPeopleListView).performTrade(getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), (int)-MyAmountDesiredSlider.getValue(), (int)-MyTradingSlider.getValue());}
            if(!getTraderFromListView(SecondPeopleListView).performTrade(getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), (int)MyTradingSlider.getValue(), (int)MyAmountDesiredSlider.getValue())){errorLabel += getTraderFromListView(SecondPeopleListView).getUserName();}
            else{getTraderFromListView(SecondPeopleListView).performTrade(getDinoReference(MyTradingListView.getSelectionModel().getSelectedItem()), getDinoReference(TraderListView.getSelectionModel().getSelectedItem()), (int)-MyTradingSlider.getValue(), (int)-MyAmountDesiredSlider.getValue());}

            //Shows an error label if at least one of the traders cannot make the trade due to a lack of slots
            if(!errorLabel.isEmpty()){TradingInfoLabel.setText(TradingInfoLabel.getText() + "\nWhoops! " + errorLabel + " cannot make that trade due to a lack of slots!");}
            if(MyTradingSlider.getValue()==0 || MyAmountDesiredSlider.getValue()==0){TradingInfoLabel.setText("This is not really a trade. You're literally trading 0 cards for at least one of them.");}
            ConfirmTradeButton.setDisable(!errorLabel.isEmpty()||MyTradingSlider.getValue()==0 || MyAmountDesiredSlider.getValue()==0); //Disable the trade if it doesn't work
        }else{TradingInfoLabel.setText(TradingInfoLabel.getText() + "\nThe Trade Doesn't Seem Fair");}
    }

    @FXML public void handleTradingSelectionChange(){ //Updates the area whenever a new user or dino is selected for each list view
        if(MyTradingListView.getSelectionModel().getSelectedItem() != null && !MyTradingListView.getSelectionModel().getSelectedItem().equals("No Cards") && TraderListView.getSelectionModel().getSelectedItem() != null && !TraderListView.getSelectionModel().getSelectedItem().equals("No Cards")) {
            TradingInfoLabel.setText("");
            if (getTraderFromListView(FirstPeopleListView) == getTraderFromListView(SecondPeopleListView)) {TradingInfoLabel.setText("Why Are You Trading With Yourself?");}
            else if (MyTradingListView.getSelectionModel().getSelectedItem().equals(TraderListView.getSelectionModel().getSelectedItem())) {TradingInfoLabel.setText("Why Are You Trading The Same Exact Card?");}
            else {
                MyTradingSlider.setDisable(false);
                MyAmountDesiredSlider.setDisable(false);
                MyTradingSlider.setMax(getTraderFromListView(FirstPeopleListView).getDinoWithSlot(MyTradingListView.getSelectionModel().getSelectedItem()).getQuantityOwned());
                MyAmountDesiredSlider.setMax(getTraderFromListView(SecondPeopleListView).getDinoWithSlot(TraderListView.getSelectionModel().getSelectedItem()).getQuantityOwned());
                handleTradeAmountChange();
            }
            setImage(MyTradeImageView, MyTradingListView.getSelectionModel().getSelectedItem());
            setImage(TheirTradeImageView, TraderListView.getSelectionModel().getSelectedItem());
        }
    }

    @FXML public void handleConfirmSelling(){//Handles the selling of the dino and resets
        getTraderFromListView(SellingPeopleListView).changeMoneyInAccount((int)(SellingSlider.getValue() * getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()).calculateValue()));
        getTraderFromListView(SellingPeopleListView).addDinoCard(getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()), -(int)(SellingSlider.getValue()));
        getTraderFromListView(SellingPeopleListView).deleteEmptySlots();
        resetAll();
    }

    @FXML public void handleSellSliderClick(){SellingInfoLabel.setText("You Are Selling " + (int)SellingSlider.getValue() + " "+ SellingCardsListView.getSelectionModel().getSelectedItem() + "'s for $" + getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()).calculateValue() + " each. That's a total of $" + (int)SellingSlider.getValue() * getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()).calculateValue());} //Changes the info label whenever the value of the slider changes

    @FXML public void handleSellClick(){//Initializes the area whenever a dino in the selling area is clicked
        if(SellingCardsListView.getSelectionModel().getSelectedItem() != null && !SellingCardsListView.getSelectionModel().getSelectedItem().equals("No Cards")){
            SellingSlider.setDisable(false);
            SellingSlider.setMax(getTraderFromListView(SellingPeopleListView).getDinoWithSlot(SellingCardsListView.getSelectionModel().getSelectedItem()).getQuantityOwned());
            setImage(SellingImageView, SellingCardsListView.getSelectionModel().getSelectedItem());
            ConfirmSellingButton.setDisable(false);
            SellingInfoLabel.setText("You Are Selling " + (int)SellingSlider.getValue() + " "+ SellingCardsListView.getSelectionModel().getSelectedItem() + "'s for $" + getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()).calculateValue() + " each. That's a total of $" + (int)SellingSlider.getValue() * getDinoReference(SellingCardsListView.getSelectionModel().getSelectedItem()).calculateValue());
        }
    }

    @FXML public void handlePurchase(){//Handles buying the certain dino
        getTraderFromListView(BuyingPeopleListView).changeMoneyInAccount((int)(-1*BuyingSlider.getValue() * getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()).calculateValue()));
        getTraderFromListView(BuyingPeopleListView).addDinoCard(getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()), (int) BuyingSlider.getValue());
        getTraderFromListView(BuyingPeopleListView).deleteEmptySlots();
        resetAll();
    }

    @FXML public void handleBuyingSliderChange(){BuyingInfoLabel.setText("You Are Buying " + (int)BuyingSlider.getValue() + " " + BuyingCardsListView.getSelectionModel().getSelectedItem() + "'s for a total of $" + (int)BuyingSlider.getValue() * getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()).calculateValue());}//Changes the info label whenever the slider value changes

    @FXML public void handleShopClicked(){//Updates the buying area whenever a new dino or a new dino or person is selected
        if(BuyingCardsListView.getSelectionModel().getSelectedItem() != null && !BuyingCardsListView.getSelectionModel().getSelectedItem().equals("No Cards")) {
            setImage(BuyingImageView, BuyingCardsListView.getSelectionModel().getSelectedItem());
            BuyingSlider.setMax((int) Math.floor((double) getTraderFromListView(BuyingPeopleListView).getMoneyInAccount() / getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()).calculateValue()));
            BuyingSlider.setDisable(BuyingSlider.getMax() == 0);
            ConfirmPurchaseButton.setDisable(false);
            BuyingInfoLabel.setText("You Are Buying " + (int)BuyingSlider.getValue() + " " + BuyingCardsListView.getSelectionModel().getSelectedItem() + "'s for a total of $" + (int)BuyingSlider.getValue() * getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()).calculateValue());
            if (BuyingSlider.getMax() == 0) {
                BuyingInfoLabel.setText("You Don't Have Enough Money For That Dino");
                ConfirmPurchaseButton.setDisable(true);
            }
            //Tests if there's any available slots
            if (!getTraderFromListView(BuyingPeopleListView).addDinoCard(getDinoReference(BuyingCardsListView.getSelectionModel().getSelectedItem()), 0)) {
                BuyingInfoLabel.setText(BuyingInfoLabel.getText() + "\nYou don't have any available slots for that card.");
                ConfirmPurchaseButton.setDisable(true);
            }
            getTraderFromListView(BuyingPeopleListView).deleteEmptySlots();
        }
        BuyingMoneyLabel.setText("Money In " + getTraderFromListView(BuyingPeopleListView).getUserName() + " Account: " + getTraderFromListView(BuyingPeopleListView).getMoneyInAccount());
    }

    @FXML public void handleViewingListClick(){//When the user wants to view a certain dinosaur owned by a certain person, the corresponding image is put up and the info is shown
        if(ViewingCardsListView.getSelectionModel().getSelectedItem() != null && ViewingPeopleListView.getSelectionModel().getSelectedItem() != null && !ViewingCardsListView.getSelectionModel().getSelectedItem().equals("No Cards")){
            setImage(ViewingImageView, ViewingCardsListView.getSelectionModel().getSelectedItem());
            DinosaurReference dinosaurReference = getTraderFromListView(ViewingPeopleListView).getDinoWithSlot(ViewingCardsListView.getSelectionModel().getSelectedItem()).getDinosaurReference();
            ViewingLabel.setText("My " + dinosaurReference.getDinoType() + ":\nRarity: " + dinosaurReference.getRarity() + "\nFamily: " + dinosaurReference.getFamily() + "\nAttack: " + dinosaurReference.getAttack() + "\nHealth: " + dinosaurReference.getHealth() + "\nValue: "+dinosaurReference.calculateValue() + "\nQuantity Owned: " + getTraderFromListView(ViewingPeopleListView).getDinoWithSlot(ViewingCardsListView.getSelectionModel().getSelectedItem()).getQuantityOwned());
        }
    }

    @FXML public void resetAll(){
        if(BuyingCardsListView != null) {
            //Resets all the list views and updates them
            ViewingCardsListView.getItems().clear();
            SellingCardsListView.getItems().clear();
            MyTradingListView.getItems().clear();
            TraderListView.getItems().clear();
            ViewingCardsListView.getSelectionModel().clearSelection();
            BuyingCardsListView.getSelectionModel().clearSelection();
            SellingCardsListView.getSelectionModel().clearSelection();
            MyTradingListView.getSelectionModel().clearSelection();
            TraderListView.getSelectionModel().clearSelection();
            setValueOfListView(ViewingCardsListView, getTraderFromListView(ViewingPeopleListView));
            setValueOfListView(SellingCardsListView, getTraderFromListView(SellingPeopleListView));
            setValueOfListView(MyTradingListView, getTraderFromListView(FirstPeopleListView));
            setValueOfListView(TraderListView, getTraderFromListView(SecondPeopleListView));

            //Hides all the image views
            ViewingImageView.setVisible(false);
            BuyingImageView.setVisible(false);
            SellingImageView.setVisible(false);
            MyTradeImageView.setVisible(false);
            TheirTradeImageView.setVisible(false);

            //Resets and disables the buttons and labels
            ViewingLabel.setText("");
            BuyingInfoLabel.setText("");
            SellingInfoLabel.setText("");
            TradingInfoLabel.setText("");
            BuyingMoneyLabel.setText("Money In " + getTraderFromListView(BuyingPeopleListView).getUserName() + " Account: " + getTraderFromListView(BuyingPeopleListView).getMoneyInAccount());
            SellingMoneyLabel.setText("Money In " + getTraderFromListView(SellingPeopleListView).getUserName() + " Account: " + getTraderFromListView(SellingPeopleListView).getMoneyInAccount());
            ConfirmPurchaseButton.setDisable(true);
            ConfirmSellingButton.setDisable(true);
            ConfirmTradeButton.setDisable(true);
            BuyingSlider.setDisable(true);
            SellingSlider.setDisable(true);
            MyTradingSlider.setDisable(true);
            MyAmountDesiredSlider.setDisable(true);
        }
    }

    //Initialize program
    @FXML public void handleInitialize(){
        IntroHBox.setVisible(false);
        IntroHBox.setDisable(true);
        DisplayNameLabel.setVisible(true);
        MyTabPane.setVisible(true);
        MyTabPane.setDisable(false);

        //Initializes all the list views
        ViewingPeopleListView.getItems().addAll("Me", "Trader 1", "Trader 2", "Trader 3");
        SecondPeopleListView.getItems().addAll("Me", "Trader 1", "Trader 2", "Trader 3");
        FirstPeopleListView.getItems().addAll("Me", "Trader 1", "Trader 2", "Trader 3");
        SellingPeopleListView.getItems().addAll("Me", "Trader 1", "Trader 2", "Trader 3");
        BuyingPeopleListView.getItems().addAll("Me", "Trader 1", "Trader 2", "Trader 3");
        ViewingPeopleListView.getSelectionModel().selectFirst();
        SellingPeopleListView.getSelectionModel().selectFirst();
        SecondPeopleListView.getSelectionModel().selectFirst();
        FirstPeopleListView.getSelectionModel().selectFirst();
        BuyingPeopleListView.getSelectionModel().selectFirst();
        BuyingCardsListView.getItems().addAll("Tyrannosaur", "Indominus", "Stegosaur", "Triceratops", "Sarcosuchus", "Velociraptor", "Alanqa", "Carnotaurus", "Dilophosaurus");

        //Initializes all the cards
        Tyrannosaur = new DinosaurReference("Tyrannosaur", "Legendary", "Carnivore",1603, 612);
        Indominus = new DinosaurReference("Indominus", "Legendary", "Carnivore", 5430, 2074);
        Stegosaur = new DinosaurReference("Stegosaur", "Super Rare", "Herbivore", 982, 251);
        Triceratops = new DinosaurReference("Triceratops", "Common", "Herbivore", 274, 70);
        Sarcosuchus = new DinosaurReference("Sarcosuchus", "Legendary", "Amphibian", 1500, 573);
        Velociraptor = new DinosaurReference("Velociraptor", "Super Rare", "Carnivore", 800, 306);
        Alanqa = new DinosaurReference("Alanqa", "Common", "Pterosaur", 217, 83);
        Carnotaurus = new DinosaurReference("Carnotaurus", "Rare", "Carnivore", 419, 160);
        Dilophosaurus = new DinosaurReference("Dilophosaurus", "Rare", "Carnivore", 396, 151);

        //Initializes the people
        final int startingBalance = 1000;
        if(NameTextBox.getText().isEmpty()){Me = new People("Anonymous", startingBalance);}
        else{Me = new People(NameTextBox.getText(), startingBalance);}
        Trader1 = new People("Trader 1", startingBalance);
        Trader2 = new People("Trader 2", startingBalance);
        Trader3 = new People("Trader 3", startingBalance);
        DisplayNameLabel.setText("Hi, " + Me.getUserName());

        resetAll();
    }

    public People getTraderFromListView(ListView<String> listView){//Gets the value of the selected list view and returns the person selected
        switch (listView.getSelectionModel().getSelectedItem()){
            case "Me" : return Me;
            case "Trader 1": return Trader1;
            case "Trader 2": return Trader2;
            case "Trader 3": return Trader3;
            default: return null;
        }
    }

    public DinosaurReference getDinoReference(String dinoReferenced){//Gets the dino reference from the dino name
        switch (dinoReferenced){
            case "Tyrannosaur": return Tyrannosaur;
            case "Indominus": return Indominus;
            case "Stegosaur": return Stegosaur;
            case "Triceratops": return Triceratops;
            case "Alanqa": return Alanqa;
            case "Sarcosuchus": return Sarcosuchus;
            case "Dilophosaurus": return Dilophosaurus;
            case "Carnotaurus": return Carnotaurus;
            case "Velociraptor": return Velociraptor;
            default: return null;
        }
    }

    public void setValueOfListView(ListView<String> listViewToModify, People dataFromPerson){//Sets the values of the dino list views based on the inventory of the selected user
        if(dataFromPerson.getSlot("Slot1") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot1").getDinosaurReference().getDinoType());}
        if(dataFromPerson.getSlot("Slot2") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot2").getDinosaurReference().getDinoType());}
        if(dataFromPerson.getSlot("Slot3") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot3").getDinosaurReference().getDinoType());}
        if(dataFromPerson.getSlot("Slot4") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot4").getDinosaurReference().getDinoType());}
        if(dataFromPerson.getSlot("Slot5") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot5").getDinosaurReference().getDinoType());}
        if(dataFromPerson.getSlot("Slot6") != null){listViewToModify.getItems().add(dataFromPerson.getSlot("Slot6").getDinosaurReference().getDinoType());}
        if(listViewToModify.getItems().toArray().length == 0){listViewToModify.getItems().add("No Cards");}
    }

    private void setImage(ImageView imageView, String value){//Just sets the image of the selected image view based on the dinosaur name selected
        imageView.setVisible(true);
        try{imageView.setImage(new Image(new FileInputStream("src/main/resources/DinoImages/" + value + ".png")));}
        catch(FileNotFoundException e){e.printStackTrace();}
    }

    //Private Variables
    private DinosaurReference Tyrannosaur, Indominus, Stegosaur, Alanqa, Triceratops, Sarcosuchus, Dilophosaurus, Carnotaurus, Velociraptor;
    private People Me, Trader1, Trader2, Trader3;
}