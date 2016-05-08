/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.internal.matchers.StringContains.containsString;
import static org.junit.matchers.JUnitMatchers.both;
/**
 *
 * @author davidjansing
 */
public class Test1 {
    
    private static RecipeBoxUI rb;
    // components under test
    private static javax.swing.JTextArea ingredientListBox;
    private static javax.swing.JTextArea directionsBox;
    private static javax.swing.JTextField servingsBox;
    
    private void setUpClass() {
        rb = new RecipeBoxUI();
        ingredientListBox = rb.getIngredientListBox();
        directionsBox = rb.getDirectionsBox();
        servingsBox = rb.getServingsBox();
    }

    private void tearDownClass() {
       rb.dispose();
    }
    
    // There really isn't anything out there that compares
    // a string to a regular expression, so you pretty much
    // have to write your own.  Here's one that checks a 
    // string to see if it's a number or a fraction
    private boolean isNumber (String num)
    {
        // if it's a fraction, it will have a slash in it
        if (num.indexOf("/") != -1)
        {
            String[] numberParts = num.split("/");
            for (String s : numberParts)
            {
                // try to turn it into an integer
                // if it's not possible, catch the exception
                // and return false
                try
                {
                    int i = Integer.parseInt(s);
                }
                catch(NumberFormatException e)
                {
                    return false;
                }
            }
        }
        else // it's a whole number
        {
            try
            {
                int i = Integer.parseInt(num);
            }
            catch(Exception e)
            {
                return false;
            }
        }
        return true;
    }
    
    // This test simply pulls a recipe and makes sure 
    // everything is there.
    public void testSetIngredients()
    {
        // Test for null - the ingredient list, servings, 
        // and directions boxes should all be empty
        // at the start of the test.
        assertNull(ingredientListBox.getText(), null);
        assertNull(directionsBox.getText(), null);
        assertNull(servingsBox.getText(), null);
        // Select a recipe that is assumed to exist
        rb.getRecipeList().setSelectedItem("Pasta e fagioli");
        rb.getGetRecipeButton().doClick();
        // assertEquals tests for an exact match of what you're checking
        // equivalent to String.equals()
        assertEquals("2 slices bacon\n" + 
                    "1 cups frozen chopped mixed vegetables\n" + 
                    "1 can Hunt's Garlic & Herb Pasta Sauce\n" + 
                    "2 cups water\n" + 
                    "1 can red kidney beans\n" + 
                    "1/2 cups dry large elbow macaroni, uncooked\n" +
                    "1/4 cups grated Parmesan cheese\n",
                    ingredientListBox.getText());

        assertEquals("6",servingsBox.getText());
        // running assertTrue on the method I wrote to test
        // whether a string represents a number
        assertTrue(isNumber(servingsBox.getText()));
        // I probably could have avoided some headache had I known
        // to use this to do the equivalent of String.contains() rather
        // than trying to match the entire text.
        assertThat(directionsBox.getText(), 
                   both(containsString("Cook bacon in saucepan 5 minutes or until crisp")).
                   and(containsString("Remove bacon, leaving drippings in pan.")));
        assertThat(directionsBox.getText(), 
                   containsString("Add vegetables in pan, cook and stir 2 minutes or until tender."));
        // Here's the original assertion that had to be dead-on exact
        assertEquals("Cook bacon in saucepan 5 minutes or until crisp\n"+
                    "Remove bacon, leaving drippings in pan.\n"+
                    "Add vegetables in pan, cook and stir 2 minutes or until tender.\n"+
                    "Add pasta sauce, water, beans, and macaroni.\n"+
                    "Bring to a boil, reduce heat and simmer 10 minutes or until macaroni is tender.\n"+
                    "Stir crumbled bacon into soup.  Top each serving with cheese.", 
                    directionsBox.getText());                   
    }
    
    /**
     * Test of main method, of class recipeBoxUI.
     */
    
    // You can set timeouts right there in the test
    @Test(timeout=10000)
    public void testMain() {
        System.out.println("Test1::testMain");
        setUpClass();
        testSetIngredients();
        tearDownClass();
    }
}
