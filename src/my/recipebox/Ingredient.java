/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;


public class Ingredient extends RecipeBoxUI{
    private double quantity;
    private String unit;
    private String item;
    
    public Ingredient()
    {
        // default ctor
        this.quantity = 0.0;
        this.unit = "";
        this.item = "";
    }
    
        // copy an ingredient
    public Ingredient(Ingredient i)
    {
        this.quantity = i.getQuantity();
        this.unit = i.getUnit();
        this.item = i.getItem();
    }
    
    public Ingredient(double q, String u, String i)
    {
        this.quantity = q;
        this.unit = u;
        this.item = i;
    }
    
    public double getQuantity()
    {
        return quantity;
    }
    
    public String getUnit()
    {
        return unit;
    }
    
    public String getItem()
    {
        return item;
    }
    
    public void setQuantity(double q)
    {
        this.quantity = q;
    }
    
    public void setUnit(String u)
    {
        this.unit = u;
    }
        
    public void setItem(String i)
    {
        this.item = i;
    }
}
