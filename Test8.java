/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test8 {
    
    private static RecipeBoxUI rb;
    private static javax.swing.JTextArea ingredientListBox;
    private static javax.swing.JTextArea directionsBox;
    private static javax.swing.JTextField servingsBox;
    private static javax.swing.JComboBox recipeList;
    private static javax.swing.JButton editRecipeButton;
    private static javax.swing.JButton getRecipeButton;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
        ingredientListBox = rb.getIngredientListBox();
        directionsBox = rb.getDirectionsBox();
        servingsBox = rb.getServingsBox();
        recipeList = rb.getRecipeList();
        editRecipeButton = rb.getEditRecipeButton();
        getRecipeButton = rb.getGetRecipeButton();
    }

    private void tearDownClass() {
       recipeList.setSelectedItem("test pie");
       rb.getDeleteRecipeButton().doClick();
       rb.dispose();
    }
    
    // Test to check proper conversion of fractions
    // to standard measuring cup sizes
    public void testConvertQuantity()
    {
        recipeList.addItem("test pie");
        recipeList.setSelectedItem("test pie");

        // 15 tbsp is 15/16 cup, 
        // which should translate to 1 cup when the system
        // converts the unit upward
        ingredientListBox.setText("1/16 bags\n" + 
                                     "2/16 bags\n" +
                                     "3/16 bags\n" + 
                                     "4/16 bags\n" + 
                                     "5/16 bags\n" + 
                                     "6/16 bags\n" + 
                                     "7/16 bags\n" + 
                                     "8/16 bags\n" +
                                     "9/16 bags\n" + 
                                     "10/16 bags\n" + 
                                     "11/16 bags\n" + 
                                     "12/16 bags\n" +
                                     "13/16 bags\n" + 
                                     "14/16 bags\n" +
                                     "15/16 bags\n" + 
                                     "16/16 bags");
        servingsBox.setText("1");
        directionsBox.setText("This is a test recipe.  It isn't supposed " + 
                                 "to make any sense");
        editRecipeButton.doClick();
        recipeList.setSelectedItem("test pie");
        getRecipeButton.doClick();
        assertEquals("< 1/4 bags\n" + 
                     "< 1/4 bags\n" +
                     "1/4 bags\n" + 
                     "1/4 bags\n" + 
                     "1/3 bags\n" + 
                     "1/3 bags\n" + 
                     "1/2 bags\n" + 
                     "1/2 bags\n" +
                     "1/2 bags\n" + 
                     "2/3 bags\n" + 
                     "2/3 bags\n" + 
                     "3/4 bags\n" +
                     "3/4 bags\n" + 
                     "1 bags\n" +
                     "1 bags\n" + 
                     "1 bags\n", 
                     ingredientListBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test8::testMain");
        setUpClass();
        testConvertQuantity();
        tearDownClass();
    }
}
