package com.example.tradingcardgame_9_20_23;

public class DinosaursOwned {
    //get methods
    public DinosaurReference getDinosaurReference() {return dinosaurReference;}
    public int getQuantityOwned(){return this.quantityOwned;}

    //set methods
    public void changeQuantityOwned(int changeInBalance){this.quantityOwned += changeInBalance;}

    //constructor
    public DinosaursOwned(DinosaurReference dinosaurReference, int quantityOwned){
        this.dinosaurReference = dinosaurReference;
        this.quantityOwned = quantityOwned;
    }

    //private attributes
    private final DinosaurReference dinosaurReference;
    private int quantityOwned;
}
