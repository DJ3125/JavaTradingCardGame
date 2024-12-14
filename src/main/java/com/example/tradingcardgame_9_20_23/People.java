package com.example.tradingcardgame_9_20_23;

public class People {
    //get methods
    public String getUserName(){return this.userName;}
    public int getMoneyInAccount(){return this.moneyInAccount;}
    public DinosaursOwned getSlot(String Slot){
        switch(Slot){//Returns a slot based on the string inputted
            case "Slot1": return slot1;
            case "Slot2": return slot2;
            case "Slot3": return slot3;
            case "Slot4": return slot4;
            case "Slot5": return slot5;
            case "Slot6": return slot6;
            default: return null;
        }
    }

    //set methods
    public void setMoneyInAccount(int newBalance){this.moneyInAccount = newBalance;}
    public void changeMoneyInAccount(int changeInBalance){this.moneyInAccount += changeInBalance;}
    public boolean addDinoCard(DinosaurReference dinoToAdd, int quantityToAdd){
        //returning true means the addition was successful. Returning false means otherwise
        if(slot1 != null && slot1.getDinosaurReference() == dinoToAdd){slot1.changeQuantityOwned(quantityToAdd); return true;}
        if(slot2 != null && slot2.getDinosaurReference() == dinoToAdd){slot2.changeQuantityOwned(quantityToAdd); return true;}
        if(slot3 != null && slot3.getDinosaurReference() == dinoToAdd){slot3.changeQuantityOwned(quantityToAdd); return true;}
        if(slot4 != null && slot4.getDinosaurReference() == dinoToAdd){slot4.changeQuantityOwned(quantityToAdd); return true;}
        if(slot5 != null && slot5.getDinosaurReference() == dinoToAdd){slot5.changeQuantityOwned(quantityToAdd); return true;}
        if(slot6 != null && slot6.getDinosaurReference() == dinoToAdd){slot6.changeQuantityOwned(quantityToAdd); return true;}
        if(slot1 == null){slot1 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        if(slot2 == null){slot2 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        if(slot3 == null){slot3 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        if(slot4 == null){slot4 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        if(slot5 == null){slot5 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        if(slot6 == null){slot6 = new DinosaursOwned(dinoToAdd, quantityToAdd); return true;}
        return false;
    }

    public void deleteEmptySlots(){//Simply clears the slots which have 0 dinosaurs in them
        if(slot1 != null && slot1.getQuantityOwned() == 0){slot1 = null;}
        if(slot2 != null && slot2.getQuantityOwned() == 0){slot2 = null;}
        if(slot3 != null && slot3.getQuantityOwned() == 0){slot3 = null;}
        if(slot4 != null && slot4.getQuantityOwned() == 0){slot4 = null;}
        if(slot5 != null && slot5.getQuantityOwned() == 0){slot5 = null;}
        if(slot6 != null && slot6.getQuantityOwned() == 0){slot6 = null;}
    }
    public boolean performTrade(DinosaurReference cardGained, DinosaurReference cardLost, int quantityGained, int quantityLost){
        this.addDinoCard(cardLost, -quantityLost);//Gives the cards away
        this.deleteEmptySlots();
        if(this.addDinoCard(cardGained, quantityGained)){return true;}//checks if any slots are open and places them in if there are
        else{//Restores the previous value by undoing the first line if trade cannot be made
            this.deleteEmptySlots();
            this.addDinoCard(cardLost, quantityLost);
            return false;
        }
    }

    public DinosaursOwned getDinoWithSlot(String dinoName){//Based on the dinosaur name, it returns the slot with the specified dino
        if(slot1 != null && slot1.getDinosaurReference().getDinoType().equals(dinoName)){return slot1;}
        if(slot2 != null && slot2.getDinosaurReference().getDinoType().equals(dinoName)){return slot2;}
        if(slot3 != null && slot3.getDinosaurReference().getDinoType().equals(dinoName)){return slot3;}
        if(slot4 != null && slot4.getDinosaurReference().getDinoType().equals(dinoName)){return slot4;}
        if(slot5 != null && slot5.getDinosaurReference().getDinoType().equals(dinoName)){return slot5;}
        if(slot6 != null && slot6.getDinosaurReference().getDinoType().equals(dinoName)){return slot6;}
        return null;
    }

    //constructor
    public People(String newName, int newAccountBalance){
        this.slot1 = this.slot2 = this.slot3 = this.slot4 = this.slot5 = this.slot6 = null;
        this.moneyInAccount = newAccountBalance;
        this.userName = newName;
    }

    //private attributes
    private final String userName;
    private DinosaursOwned slot1, slot2, slot3, slot4, slot5, slot6;
    private int moneyInAccount;
}
