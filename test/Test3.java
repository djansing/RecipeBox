/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test3 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
        rb.recipeList.setSelectedItem("bobs_burgers");
        rb.getRecipeButton.doClick();
        assertEquals("bobs_burgers",
                    rb.recipeList.getSelectedItem());
        rb.ingredientListBox.setText("30 dollars cash");
        rb.directionsBox.setText("go to Bob's Burgers.  \n" +
                                "Order the burger of the day.\n" +
                                "Be sure to comment on how funny the name is.\n" +
                                "Ignore Gene's gross noises.");
        rb.servingsBox.setText("3");
        rb.editRecipeButton.doClick();
        rb.dispose();
    }
    

    public void testEditRecipe()
    {
        rb.recipeList.setSelectedItem("bobs_burgers");
        rb.getRecipeButton.doClick();
        assertEquals("bobs_burgers",
                    rb.recipeList.getSelectedItem());
        rb.ingredientListBox.setText("12 euros cash");
        rb.directionsBox.setText("Dial 555-5555 and ask for Gene.");
        rb.editRecipeButton.doClick();
        rb.recipeList.setSelectedItem("Pasta_e_fagioli");
        rb.getRecipeButton.doClick();
        rb.recipeList.setSelectedItem("bobs_burgers");
        rb.getRecipeButton.doClick();
        assertEquals("12 euros cash\n",
                    rb.ingredientListBox.getText());
        assertEquals("Dial 555-5555 and ask for Gene.",
                    rb.directionsBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testEditRecipe();
        tearDownClass();
    }
}
