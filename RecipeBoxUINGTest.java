/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package my.recipebox;

import java.util.ArrayList;
import static org.testng.Assert.*;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author davidjansing
 */
public class RecipeBoxUINGTest {
    
    public RecipeBoxUINGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * Test of readXmlFile method, of class RecipeBoxUI.
     */
    @Test
    public void testReadXmlFile() {
        System.out.println("readXmlFile");
        String fileLocation = "xml/bobs_burgers";
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.readXmlFile(fileLocation);
        assert 30.0 == instance.ingredients.get(0).getQuantity();
        assert "".equals(instance.ingredients.get(0).getUnit());
        assert "dollars cash".equals(instance.ingredients.get(0).getItem());
        instance.dispose();
    }

    /**
     * Test of isUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testIsUnit() {
        System.out.println("isUnit");
        String u = "cups";
        RecipeBoxUI instance = new RecipeBoxUI();
        boolean expResult = true;
        boolean result = instance.isUnit(u);
        assertEquals(result, expResult);
        String v = "barleycorns";
        expResult = false;
        result = instance.isUnit(v);
        assertEquals(result, expResult);
        instance.dispose();
    }


    /**
     * Test of getServings method, of class RecipeBoxUI.
     * I combined these because they're a pair of setter/getter methods
     */
    @Test
    public void testGetSetServings() {
        System.out.println("getServings");
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.setServings(5);
        int expResult = 5;
        int result = instance.getServings();
        assertEquals(result, expResult);
        instance.dispose();
    }


    /**
     * Test of getImage, setImage method, of class RecipeBoxUI.
     * I combined these because they're a pair of setter/getter methods
     */
    @Test
    public void testGetSetImage() {
        System.out.println("getImage");
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.setImage("Pasta.jpg");
        String expResult = "Pasta.jpg";
        String result = instance.getImage();
        assertEquals(result, expResult);
        instance.dispose();
    }

    /**
     * Test of setDirections method, of class RecipeBoxUI.
     */
    @Test
    public void testSetGetDirections() {
        System.out.println("setDirections");
        String s = "directions";
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.setDirections(s);
        assert "directions".equals(instance.getDirections());
        instance.dispose();
    }

    /**
     * Test of setIngredients method, of class RecipeBoxUI.
     */
    @Test
    public void testSetIngredients_0args() {
        System.out.println("setIngredients");
        RecipeBoxUI instance = new RecipeBoxUI();
        Ingredient ing = new Ingredient(3, "cups", "water");
        instance.ingredients.add(ing);
        instance.setIngredients();
        assert 3.0 == instance.ingredients.get(0).getQuantity();
        assert "cups".equals(instance.ingredients.get(0).getUnit());
        assert "water".equals(instance.ingredients.get(0).getItem());
        instance.dispose();
    }

    /**
     * Test of setIngredients method, of class RecipeBoxUI.
     */
    @Test
    public void testSetIngredients_ArrayList() {
        System.out.println("setIngredients");
        ArrayList<Ingredient> ing = new ArrayList();
        RecipeBoxUI instance = new RecipeBoxUI();
        Ingredient i = new Ingredient(3, "cups", "something or other");
        ing.add(i);
        instance.setIngredients(ing);
        ArrayList<Ingredient> result = instance.getIngredients();
        assert 3.0 == result.get(0).getQuantity();
        assert "cups".equals(result.get(0).getUnit());
        assert "something or other".equals(result.get(0).getItem());
        instance.dispose();
    }
    /**
     * Test of addItemSafely method, of class RecipeBoxUI.
     */
    @Test
    public void testAddItemSafely() {
        System.out.println("addItemSafely");
        String item = "new item";
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.setServings(8);
        instance.setDirections("directions");
        ArrayList<Ingredient> ingList = new ArrayList();
        Ingredient ing = new Ingredient(1.0, "cup", "water");
        ingList.add(ing);
        instance.setIngredients(ingList);
        instance.addItemSafely(item);
        instance.getRecipeList().setSelectedItem(item);
        assert "new item".equals(instance.getRecipeList().getSelectedItem());
        instance.dispose();
    }

    /**
     * Test of getIngredients method, of class RecipeBoxUI.
     */
    @Test
    public void testGetIngredients() {
        System.out.println("getIngredients");
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.getRecipeList().setSelectedItem("bobs_burgers");
        instance.getGetRecipeButton().doClick();
        ArrayList<Ingredient> expResult = new ArrayList();
        Ingredient ing = new Ingredient(30, "", "dollars cash");
        expResult.add(ing);
        ArrayList<Ingredient> result = instance.getIngredients();
        assertEquals(result.get(0).getQuantity(), 
                     expResult.get(0).getQuantity());
        assertEquals(result.get(0).getUnit(), 
                     expResult.get(0).getUnit());
        assertEquals(result.get(0).getItem(), 
                     expResult.get(0).getItem());
        instance.dispose();
    }

    /**
     * Test of convertLargerUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertLargerUnit() {
        System.out.println("convertLargerUnit");
        Ingredient i = new Ingredient();
        i.setQuantity(16);
        i.setUnit("tablespoons");
        i.setItem("some ingredient");
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.convertLargerUnit(i);
        assert i.getQuantity() == 1;
        assert "cups".equals(i.getUnit());
        instance.dispose();
    }

    /**
     * Test of convertSmallerUnit method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertSmallerUnit() {
        System.out.println("convertSmallerUnit");
        Ingredient i = new Ingredient();
        i.setQuantity(0.125);
        i.setUnit("cups");
        i.setItem("some ingredient");
        RecipeBoxUI instance = new RecipeBoxUI();
        instance.convertSmallerUnit(i);
        assert i.getQuantity() == 2;
        assert "tablespoons".equals(i.getUnit());
        instance.dispose();
    }

    /**
     * Test of convertToFractional method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertToFractional() {
        System.out.println("convertToFractional");
        double dec = 2.5;
        RecipeBoxUI instance = new RecipeBoxUI();
        String expResult = "2 1/2";
        String result = instance.convertToFractional(dec);
        assertEquals(result, expResult);
        instance.dispose();
    }

    /**
     * Test of convertToDecimal method, of class RecipeBoxUI.
     */
    @Test
    public void testConvertToDecimal() {
        System.out.println("convertToDecimal");
        String fraction = "1/2";
        RecipeBoxUI instance = new RecipeBoxUI();
        String expResult = "5";
        String result = instance.convertToDecimal(fraction);
        assertEquals(result, expResult);
        instance.dispose();
    }
    
}
