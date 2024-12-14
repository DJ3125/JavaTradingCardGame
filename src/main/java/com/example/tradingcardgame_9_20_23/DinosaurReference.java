package com.example.tradingcardgame_9_20_23;

public class DinosaurReference {
    //get methods
    public String getDinoType(){return this.dinoType;}
    public String getRarity(){return this.rarity;}
    public int getAttack(){return this.attack;}
    public int getHealth(){return this.health;}
    public String getFamily(){return this.family;}
    public int calculateValue(){//Calculates the value based on the attack, health, and rarity of the card
        int tempValue;
        switch (this.rarity){
            case "Legendary": tempValue = 20; break;
            case "Super Rare": tempValue = 10; break;
            case "Rare": tempValue = 5; break;
            case "Common": tempValue = 1; break;
            default: tempValue = 0;
        }
        tempValue *= 25;
        tempValue += this.attack/10 + this.health/20;
        return tempValue;
    }

    //Constructor. Initialize attributes
    public DinosaurReference(String dinoType, String rarity, String family, int attack, int health){
        this.dinoType = dinoType;
        this.rarity = rarity;
        this.family = family;
        this.attack = attack;
        this.health = health;
    }

    //private attributes
    private final int attack, health;
    private final String dinoType, rarity, family;
}
