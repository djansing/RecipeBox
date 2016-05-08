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
public class Test4 {
    
    protected static RecipeBoxUI rb;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
    }

    private void tearDownClass() {
       rb.dispose();
    }
    
    // When the decimals-to-fractions functionality is implemented,
    // this test may fail and require maintenance.
    public void testSetServings()
    {
        rb.recipeList.setSelectedItem("bobs_burgers.xml");
        rb.getRecipeButton.doClick();
        assertEquals("bobs_burgers.xml",
                    rb.recipeList.getSelectedItem());  
        rb.servingsBox.setText("6");
        rb.servingsButton.doClick();
        assertEquals("60 dollars cash\n",
                     rb.ingredientListBox.getText());
    }
    
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    @Test
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testSetServings();
        tearDownClass();
    }
}
