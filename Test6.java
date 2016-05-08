/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import java.io.File;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author davidjansing
 */
public class Test6 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
       rb.deleteRecipeButton.doClick();
       rb.dispose();
    }
    
    // When the decimals-to-fractions functionality is implemented,
    // this test may fail and require maintenance.
    public void testRecipeCoverage()
    {
        // Create a minimal recipe
        rb.recipeList.addItem("test_cake.xml");
        rb.recipeList.setSelectedItem("test_cake.xml");
        rb.ingredientListBox.setText("2 1/2 cups four token flour\n" +
                                     "2 tbsp four token salt\n" +
                                     "1 cup threeTokenSugar\n" +
                                     "1 whole threeTokenChicken\n" +
                                     "1/2 twoTokenSpiceRubMix\n" + 
                                     "2 twoTokenEggs");
        
        rb.directionsBox.setText("This is a test recipe.  It isn't supposed " + 
                                 "to make any sense");
        rb.servingsBox.setText("1");
        rb.editRecipeButton.doClick();
        // Select another recipe
        rb.recipeList.setSelectedItem("bobs_burgers.xml");
        rb.getRecipeButton.doClick();
        // Select the new recipe
        rb.recipeList.setSelectedItem("test_cake.xml");
        rb.getRecipeButton.doClick();
        assertEquals("2 1/2 cups four token flour\n" +
                     "2 tbsp four tokenSalt\n" +
                     "1 cup threeTokenSugar\n" +
                     "1 whole threeTokenChicken\n" +
                     "1/2 twoTokenSpiceRubMix\n" + 
                     "2 twoTokenEggs\n",
                     rb.ingredientListBox.getText());
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testRecipeCoverage();
        tearDownClass();
    }
}
