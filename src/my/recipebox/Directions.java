/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

/**
 *
 * @author davidjansing
 */
public class Directions {
    private int oven_temp;
    private String stove_temp;
    private String text;
    
    public Directions()
    {
        this.oven_temp = 0;
        this.stove_temp = "";
        this.text = "";
    }
    
    public Directions(int o, String s, String t)
    {
        this.oven_temp = 0;
        this.stove_temp = s;
        this.text = t; 
    }
    
    public void setOvenTemp(int t)
    {
        this.oven_temp = t;
    }
    
    public void setStoveTemp(String t)
    {
        this.stove_temp = t;
    }
    
    public void setText(String t)
    {
        this.text = t;
    }
    
    public int getOvenTemp()
    {
        return this.oven_temp;
    }
    
    public String setStoveTemp()
    {
        return this.stove_temp;
    }
    
    public String setText()
    {
        return this.text;
    }
}
