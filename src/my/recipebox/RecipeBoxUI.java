package my.recipebox;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMException;

// for xml stuff
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
 *
 * @author davidjansing
 */
public class RecipeBoxUI extends javax.swing.JFrame {
    private ArrayList<Ingredient> ingredients;
    private String directions = "";
    private String substitutions;
    private int servings;
    private final String dataPath;
    private final String imgPath;
    private final String[] units;
    private String image = null;
    /**
     * Creates new form recipeBoxUI
     */
    public RecipeBoxUI() {
        this.units = new String[]{"tsp", "teaspoon", "teaspoons", "tbsp", "tablespoon", 
            "tablespoons", "cup", "cups", "pint", "pints", "pt", "quart", "quarts", "qt", 
            "gallon", "gallons", "gal", "dl", "cl", "liter", "liters", "l", "ml", "oz", 
            "ounce", "ounces", "lb", "pound", "pounds", "g", "gram", "grams", "kg",
            "kilo", "kilos", "kilogram", "kilograms"};
        initComponents();
        ingredients = new ArrayList();
        dataPath = "./xml";
        imgPath = "./photos";
        File recipeFiles = new File(dataPath);
        String[] filenames = recipeFiles.list();
        recipeList.setSize(270,recipeList.getHeight());
        for (String filename : filenames) {
            recipeList.addItem(filename);
        }
        recipeList.setSelectedItem("none"); 
        
        File imageFiles = new File(imgPath);
        String[] images = imageFiles.list();

        for (String img : images) {
            imageList.addItem(img);
        }
        imageList.setSelectedIndex(0); 
    }
    
    public void readXmlFile(String fileLocation)
    {
        try 
        {
            ingredients.clear();
            File xmlFile = new File(fileLocation);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            
            // Get the ingredient list
            NodeList ingList = doc.getElementsByTagName("ingredient");
            for (int i=0; i<ingList.getLength(); i++)
            {
                Ingredient ing = new Ingredient();
                Node ingNode = ingList.item(i);
                Element ingElem = (Element) ingNode.getChildNodes();
                Node qNode = ingElem.getElementsByTagName("quantity").item(0);
                ing.setQuantity(Double.parseDouble(qNode.getTextContent()));
                Node uNode = ingElem.getElementsByTagName("unit").item(0);
                ing.setUnit(uNode.getTextContent());
                Node iNode = ingElem.getElementsByTagName("item").item(0);
                ing.setItem(iNode.getTextContent());
                ingredients.add(ing);
            }  
            
            // Get the servings
            Node servNode = doc.getElementsByTagName("servings").item(0);
            servings = Integer.parseInt(servNode.getTextContent());
            
            // Get the directions
            Node dirNode = doc.getElementsByTagName("directions").item(0);
            directions = dirNode.getTextContent();

            // Get the photo, if any
            Node imgNode = doc.getElementsByTagName("image").item(0);
            image = imgNode.getTextContent();
        }
        catch (ParserConfigurationException | SAXException | IOException ex) 
        {
            Logger.getLogger(RecipeBoxUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean isUnit(String u)
    {
        for(String unitString: units)
        {
            if (u.equals(unitString))
            {
            return true;
            }
        }
        return false;
    }
    
    private void updateIngredients(String ing) 
    {
     // Convert fractional quantities to decimal for storage
        String[] lines = ing.split("\n");
        ingredients.clear();
        for (String line: lines)
        {
            Ingredient in = new Ingredient();
            if (!line.equals("") && !line.equals("\n"))
            {
                String[] part = line.split(" ", 4);
                
                if (part[1].contains("/"))
                {
                    String decimalPart = convertToDecimal(part[1]);
                    String quant = "";
                    
                    if (decimalPart.equals("1"))
                    {
                        int newVal = Integer.parseInt(part[0]) + 1;
                        quant += newVal;
                    }
                    else
                    {
                        // combine parts 0 and 1 and convert
                        quant = part[0] + "." + decimalPart;
                    }
                    in.setQuantity(Double.parseDouble(quant)); 
                    if (isUnit(part[2]))
                    {
                        in.setUnit(part[2].trim());          
                        if (part.length > 3)
                        {
                            in.setItem(part[3].trim());
                        }
                    }
                }
                else if (part[0].contains("/"))
                {
                    String decimalPart = convertToDecimal(part[0]);
                    String quant = "";
                    
                    if (decimalPart.equals("1"))
                    {
                        quant = "1";
                    }
                    else
                    {
                        // combine parts 0 and 1 and convert
                        quant = "0." + decimalPart;
                    }
                    // send the first part only to the converter
                    in.setQuantity(Double.parseDouble(quant)); 
                    if (isUnit(part[1]))
                    {
                        in.setUnit(part[1].trim());
                        String it = "";
                        for(int i = 2; i < part.length; i++)
                        {
                            it += " " + part[i];
                        }
                        in.setItem(it.trim());
                    }
                    else //there is no unit
                    {
                        String it = "";
                        for(int i = 1; i < part.length; i++)
                        {
                            it += " " + part[i];
                        }
                        in.setItem(it.trim());
                    }
                }
                else // quantity is a whole number
                {
                    in.setQuantity(Double.parseDouble(part[0]));
                    // Unit is of the form "2 ounces vodka"
                    if (isUnit(part[1]))
                    {
                        in.setUnit(part[1].trim());
                        String it = "";
                        for(int i = 2; i < part.length; i++)
                        {
                            it += " " + part[i];
                        }
                        in.setItem(it.trim());
                    }
                    else
                    {
                        String it = "";
                        for(int i = 1; i < part.length; i++)
                        {
                            it += " " + part[i];
                        }
                        in.setItem(it.trim());   
                    }
                }
                
            ingredients.add(in);  
            }
        }
    }
    
    private void createXml(String filename) 
    {
        try 
        {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
            // root elements
            Document doc = docBuilder.newDocument();
            Element recipe = doc.createElement("recipe");
            recipe.setAttribute("title", recipeList.getSelectedItem().toString());
            doc.appendChild(recipe);
            Element ingredientList = doc.createElement("ingredientList");
            recipe.appendChild(ingredientList);

            int i=0;

            for(Ingredient ingred : ingredients)
            {
                // ingredient elements
                Element ingredientElem = doc.createElement("ingredient");
                ingredientList.appendChild(ingredientElem);
 
                // quantity elements
                Element quantity = doc.createElement("quantity");
                String quant = "";
                quant += ingred.getQuantity();
                quantity.appendChild(doc.createTextNode(quant));
                ingredientElem.appendChild(quantity);
 
                // unit elements
                Element unitElem = doc.createElement("unit");
                unitElem.appendChild(doc.createTextNode(ingred.getUnit()));
                ingredientElem.appendChild(unitElem);
 
                // item elements
                Element itemElem = doc.createElement("item");
                itemElem.appendChild(doc.createTextNode(ingred.getItem()));
                ingredientElem.appendChild(itemElem);
 
                i++;
            }
            
            Element servingsElem = doc.createElement("servings");
            String svg = "";
            svg += servings;
            servingsElem.appendChild(doc.createTextNode(svg));
            recipe.appendChild(servingsElem);
            
            Element directionsElem = doc.createElement("directions");
            directionsElem.appendChild(doc.createTextNode(directions));
            recipe.appendChild(directionsElem);
            
            Element imgElem = doc.createElement("image");
            if (!image.equals(""))
                imgElem.appendChild(doc.createTextNode(image));
            recipe.appendChild(imgElem);
                
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filename));

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty
                    ("{http://xml.apache.org/xslt}indent-amount","3");
            transformer.transform(source, result);
 
            System.out.println("File saved to " + filename);
 
	} catch (ParserConfigurationException | DOMException | IllegalArgumentException | TransformerException e) {
        }
    }
    
        
    // Setters and getters
    public void setServings(int s)
    {
        this.servings = s;
        servingsBox.setText(Integer.toString(servings));
    }

    public int getServings()
    {
        return this.servings;
    }  
    
        // Setters and getters
    public void setImage(String img)
    {
        if (!img.equals("none"))
            this.image = img;
        else
            this.image = "";
    }

    public String getImage()
    {
        return this.image;
    }  
    
    public void setDirections(String s)
    {
        this.directions = s;
        directionsBox.setText(directions);
    }

    
    public String getDirections()
    {
        return this.directions;
    }  
    
    public void setIngredients()
    {
        setIngredients(ingredients);
    }
   
    public void setIngredients(ArrayList<Ingredient> ing)
    {
        String ingredientList = "";
        this.ingredients = ing;
        for(Ingredient i : ing)
        {
            if ((i.getQuantity() <= 0.125  && (i.getUnit().equals("cup") ||
                                               i.getUnit().equals("cups") ||
                                               i.getUnit().equals("pounds") ||
                                               i.getUnit().equals("pound") ||
                                               i.getUnit().equals("lb")))
                   
             || (i.getQuantity() <= 0.25 && (i.getUnit().equals("quart") ||
                                            i.getUnit().equals("quarts") ||
                                            i.getUnit().equals("qt") || 
                                            i.getUnit().equals("gallon") ||
                                            i.getUnit().equals("gallons") ||
                                            i.getUnit().equals("gal")))
                    
             || (i.getQuantity() <= 0.34 && (i.getUnit().equals("tablespoon") ||
                                             i.getUnit().equals("tablespoons")||
                                             i.getUnit().equals("tbsp"))) )
            {
                convertSmallerUnit(i);
            }
            
            else if ( (i.getQuantity() >= 3 && (i.getUnit().equals("teaspoon") ||
                                                i.getUnit().equals("teaspoons") ||
                                                i.getUnit().equals("tsp"))) 
                    
             || (i.getQuantity() >= 4 && ( i.getUnit().equals("quart") ||
                                           i.getUnit().equals("quarts") ||
                                           i.getUnit().equals("qt") ||
                                           i.getUnit().equals("cups") ||
                                           i.getUnit().equals("cup"))) 
                    
             || (i.getQuantity() >= 8  && (i.getUnit().equals("tablespoons") ||
                                           i.getUnit().equals("tablespoon") ||
                                           i.getUnit().equals("tbsp") ||
                                           i.getUnit().equals("ounces") ||
                                           i.getUnit().equals("ounce") || 
                                           i.getUnit().equals("oz"))) )
                
            {
                convertLargerUnit(i);
            }
            
            ingredientList += convertToFractional(i.getQuantity());
            
            if (i.getUnit().length() > 0)
            {
                ingredientList += " " + i.getUnit();
            }
            ingredientList += " " + i.getItem() + "\n";
        }
        ingredientListBox.setText(ingredientList);
    }

        public void addItemSafely(String item)
    {
        boolean itemExists = false;
        for (int i=0; i < recipeList.getItemCount(); i++)
        {
            if (recipeList.getItemAt(i).equals(item))
            {
                itemExists = true;
                break;
            }
        }
        if (!itemExists)
        {
            recipeList.addItem(item);
        }
    }
    
    public ArrayList<Ingredient> getIngredients()
    {
        return this.ingredients;
    } 
    
    public void convertLargerUnit(Ingredient i)
    {
        String unit = i.getUnit();
        switch(unit)
        {
            case("teaspoons"):
            case("teaspoon"): 
            case("tsp"):
            {
            i.setQuantity(i.getQuantity()/3);
            i.setUnit("tablespoons");
            break;
            }
            case ("tablespoons"):
            case ("tablespoon"):
            case ("tbsp"):
            {
            i.setQuantity(i.getQuantity()/16);
            i.setUnit("cups"); 
            break;
            }
            case("cups"):
            case("cup"):
            {
            i.setQuantity(i.getQuantity()/4);
            i.setUnit("quarts");
            break;
            }
            case("quarts"):
            case("quart"):
            case("qt"):
            {
            i.setQuantity(i.getQuantity()/4);
            i.setUnit("gallons");
            break;
            }
            case("ounces"):
            case("ounce"):
            case("oz"):
            {
            i.setQuantity(i.getQuantity()/16);
            i.setUnit("pounds");
            break;
            }
            default:
            {
            //do nothing
            }  
        }
    }
    
    public void convertSmallerUnit(Ingredient i)
    {
        String unit = i.getUnit();
        switch(unit)
        {
            case ("tablespoons"):
            case ("tablespoon"):
            case ("tbsp"):
            {
            // There are 3 teaspoons in one tablespoon
            i.setQuantity(i.getQuantity()*3);
            i.setUnit("teaspoons"); 
            break;
            }
            case("cups"):
            case("cup"):
            {
            // There are 16 tablespoons in one cup
            i.setQuantity(i.getQuantity()*16);
            i.setUnit("tablespoons");
            break;
            }
            case("quarts"):
            case("quart"):
            case("qt"):
            {
            // There are 4 cups in one quart
            i.setQuantity(i.getQuantity()*4);
            i.setUnit("cups");
            break;
            }
            case("gallons"):
            case("gallon"): 
            case("gal"):
            {
            // There are 4 quarts in one gallon
            i.setQuantity(i.getQuantity()*4);
            i.setUnit("quarts");
            break;
            }
            case("pounds"):
            case("pound"):
            case("lb"):
            {
            // There are 16 ounces in one pound
            i.setQuantity(i.getQuantity()*16);
            i.setUnit("ounces");
            break;
            }
            default:
            {
            //do nothing
            }  
        }
    }
    
    public String convertToFractional(double dec)
    {
        double d = dec - (int)dec;
        String retval = "";
        if ((int)dec > 0)
        {
            if (d > 0.0)
            {
                // if there's a fraction, allow a space for it
                retval += (int)dec + " ";
            }
            else
            {
                // otherwise, leave it out
                retval += (int)dec;
            }
        }   
        
        // The boundaries are kind of arbitrary, I admit,
        // but I did eyeball them on the number line and
        // they're close enough for the customer (me)
        if (d >= 0.0 && d < 0.15 && (int)dec == 0)
        {
            retval += "< 1/4";     
        }
        else if (d >= 0.15 && d < 0.3)
        {
             retval += "1/4";
        }
        else if (d >= 0.3 && d < 0.4)
        {
             retval += "1/3";
        }
        else if (d >= 0.4 && d < 0.6)
        {
             retval += "1/2";
        }
        else if (d >= 0.6 && d < 0.7)
        {
             retval += "2/3";
        }
        else if (d >= 0.7 && d < 0.85)
        {
             retval += "3/4";
        }
        else if (d >= 0.85) //close enough to 1 to just increment the value
        {
            int nextVal = (int)dec + 1;
            retval = "";
            retval += nextVal;
        }
        return retval;
    }
    
    public String convertToDecimal(String fraction)
    {
        String retval = "";
        String[] numbers = fraction.split("/");
        if (numbers[0].equals(numbers[1]))
        {
            return "1";
        }
        double numerator= Double.parseDouble(numbers[0]);
        double denominator = Double.parseDouble(numbers[1]);
        double decimalFraction = numerator/denominator;
        retval += decimalFraction;
        return retval.substring(retval.indexOf(".")+1); 
    }
    
    private Object makeObj(final String str) {
        return new Object() {
        @Override
        public String toString() {
            return str;}};
        }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel = new javax.swing.JPanel();
        imageLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        recipeList = new javax.swing.JComboBox();
        ingredientListPane = new javax.swing.JScrollPane();
        ingredientListBox = new javax.swing.JTextArea();
        getRecipeButton = new javax.swing.JButton();
        editRecipeButton = new javax.swing.JButton();
        deleteRecipeButton = new javax.swing.JButton();
        ingredientListLabel = new javax.swing.JLabel();
        servingsBox = new javax.swing.JTextField();
        servingsButton = new javax.swing.JButton();
        imageListLabel = new javax.swing.JLabel();
        imageList = new javax.swing.JComboBox();
        directionsLabel = new javax.swing.JLabel();
        directionsPane = new javax.swing.JScrollPane();
        directionsBox = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        convertUnits = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Dave's Recipe Box");

        org.jdesktop.layout.GroupLayout imagePanelLayout = new org.jdesktop.layout.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(imagePanelLayout.createSequentialGroup()
                .add(45, 45, 45)
                .add(imageLabel)
                .addContainerGap(351, Short.MAX_VALUE))
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(imagePanelLayout.createSequentialGroup()
                .add(35, 35, 35)
                .add(imageLabel)
                .addContainerGap(503, Short.MAX_VALUE))
        );

        recipeList.setEditable(true);
        recipeList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recipeListActionPerformed(evt);
            }
        });

        ingredientListBox.setColumns(20);
        ingredientListBox.setRows(5);
        ingredientListBox.setMinimumSize(new java.awt.Dimension(240, 80));
        ingredientListPane.setViewportView(ingredientListBox);

        getRecipeButton.setText("get");
        getRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                getRecipeButtonActionPerformed(evt);
            }
        });

        editRecipeButton.setText("save");
        editRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editRecipeButtonActionPerformed(evt);
            }
        });

        deleteRecipeButton.setText("delete");
        deleteRecipeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRecipeButtonActionPerformed(evt);
            }
        });

        ingredientListLabel.setText("Ingredient list");

        servingsBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servingsBoxActionPerformed(evt);
            }
        });

        servingsButton.setText("change # of servings");
        servingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servingsButtonActionPerformed(evt);
            }
        });

        imageListLabel.setText("Add or change image");

        imageList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "none" }));
        imageList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imageListActionPerformed(evt);
            }
        });

        directionsLabel.setText("Directions");

        directionsBox.setColumns(20);
        directionsBox.setRows(5);
        directionsBox.setMinimumSize(new java.awt.Dimension(240, 80));
        directionsPane.setViewportView(directionsBox);

        convertUnits.setText("US/Metric");
        convertUnits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                convertUnitsActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, recipeList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, ingredientListPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 393, Short.MAX_VALUE)
                    .add(directionsPane)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(6, 6, 6)
                                .add(directionsLabel))
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(getRecipeButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(editRecipeButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(deleteRecipeButton))
                            .add(ingredientListLabel)
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(imageListLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(imageList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(mainPanelLayout.createSequentialGroup()
                                .add(servingsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(servingsButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(convertUnits)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .add(15, 15, 15)
                .add(recipeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(getRecipeButton)
                    .add(editRecipeButton)
                    .add(deleteRecipeButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ingredientListLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(ingredientListPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(servingsBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(servingsButton)
                    .add(convertUnits))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(imageListLabel)
                    .add(imageList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(directionsLabel)
                .add(3, 3, 3)
                .add(directionsPane)
                .addContainerGap())
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1)
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(18, 18, 18))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void servingsBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servingsBoxActionPerformed

    }//GEN-LAST:event_servingsBoxActionPerformed

    private void recipeListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recipeListActionPerformed

    }//GEN-LAST:event_recipeListActionPerformed

    private void servingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servingsButtonActionPerformed
        String servingsStr = (String) servingsBox.getText();
        // Shouldn't have zero servings of anything
        int newServings = servings;
        
        // Nor should we have half-servings of anything            
        try
        {
            if (servingsStr.contains("."))
            {
                // drop the fractional part
                newServings = Integer.parseInt(servingsStr.substring(0, servingsStr.indexOf(".")));
            }
            // I said no zeroes, and I meant it.
            else if (Integer.parseInt(servingsStr) == 0)
            {
                // do nothing
            }
            else
            {
                newServings = Integer.parseInt(servingsStr);
            }
            if (newServings != servings)
            {
                for (Ingredient i : ingredients)
                {
                    double newQuant = i.getQuantity();
                    newQuant*=newServings;
                    newQuant/=servings;
                    i.setQuantity(newQuant);
                }
            setIngredients();
            }
        }
        catch (NumberFormatException nfe)
        {
            // Basically don't change anything
        }
        setServings(newServings);
    }//GEN-LAST:event_servingsButtonActionPerformed

    private void getRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_getRecipeButtonActionPerformed
        // Get the selected recipe from the database
        String filename = (String) recipeList.getSelectedItem();
        readXmlFile(dataPath + "/" + filename);
        setServings(servings);
        setIngredients(ingredients);
        setDirections(directions);
        setImage(image);
        imagePanel.setVisible(true);
        if (!image.equals(""))
        {
            ImageIcon icon = new ImageIcon("./photos/" + image);
            imageLabel.setIcon(icon);
            imageLabel.setText("");
            imageLabel.setVisible(true);
            imageList.setSelectedItem(image);
        }
        else
        {
            imageLabel.setIcon(null);
            imageLabel.setText("");
            imageLabel.setVisible(false);
        }
    }//GEN-LAST:event_getRecipeButtonActionPerformed

    private void editRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editRecipeButtonActionPerformed
        String title = (String) recipeList.getSelectedItem();
        directions = directionsBox.getText();
        servings = Integer.parseInt(servingsBox.getText());
        String ing = ingredientListBox.getText();
        updateIngredients(ing);
        createXml(dataPath + "/" + title);
        addItemSafely(title);
    }//GEN-LAST:event_editRecipeButtonActionPerformed

    private void deleteRecipeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRecipeButtonActionPerformed
       
        String title = (String) recipeList.getSelectedItem();
        File file = new File(dataPath + "/" + title);

        if(file.exists())
        {
            file.delete();
            recipeList.removeItem(title);
            recipeList.setSelectedIndex(0);
            ingredientListBox.setText("");
            servingsBox.setText("");
            directionsBox.setText("");
        }
    }//GEN-LAST:event_deleteRecipeButtonActionPerformed

    private void imageListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imageListActionPerformed
        setImage((String) imageList.getSelectedItem());
        imagePanel.setVisible(true);
        ImageIcon icon = new ImageIcon("./photos/" + image);
        imageLabel.setIcon(icon);
        imageLabel.setText("");
        imageLabel.setVisible(true); 
    }//GEN-LAST:event_imageListActionPerformed

    private void convertUnitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_convertUnitsActionPerformed
        String str = "";
        for (int i=0; i < ingredients.size(); i++)
        {
            NumberFormat f= new DecimalFormat("#0");
            convertIngredient(i);
            String theQuantity = f.format(ingredients.get(i).getQuantity());
            
            if (isEnglish(ingredients.get(i).getUnit()) ||
                !isMetric(ingredients.get(i).getUnit()))
            {
                theQuantity = convertToFractional(ingredients.get(i).getQuantity());
            }
            
            str += theQuantity + " " + 
                    ingredients.get(i).getUnit() + " " + 
                    ingredients.get(i).getItem() + "\n";
            
        }
        ingredientListBox.setText(str);
    }//GEN-LAST:event_convertUnitsActionPerformed

    // Returns true if the unit is some English/US unit.
    private boolean isEnglish(String u)
    {
        if (u.equals("oz") ||
            u.contains("ounce") ||
            u.contains("lb") ||
            u.contains("pound") ||
            u.equals("tbs") ||
            u.equals("tablespoons") ||
            u.equals("tsp") ||
            u.equals("teaspoons") ||
            u.contains("cup") ||
            u.contains("pint") ||
            u.contains("quart") ||
            u.contains("gallon"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    // We need this one to run in conjunction with isEnglish
    // to determine if the unit of measurement is some weird
    // unit like "pinch" or "can" or "scoop" or whatever.
    private boolean isMetric(String u)
    {
        if (u.equals("ml") ||
            u.equals("cl") ||
            u.equals("dl") ||
            u.equals("l") ||
            u.contains("liter") ||
            u.equals("g") ||
            u.contains("gram") ||
            u.equals("kg") ||
            u.contains("kilogram") )
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton convertUnits;
    private javax.swing.JButton deleteRecipeButton;
    private javax.swing.JTextArea directionsBox;
    private javax.swing.JLabel directionsLabel;
    private javax.swing.JScrollPane directionsPane;
    private javax.swing.JButton editRecipeButton;
    private javax.swing.JButton getRecipeButton;
    private javax.swing.JLabel imageLabel;
    private javax.swing.JComboBox imageList;
    private javax.swing.JLabel imageListLabel;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JTextArea ingredientListBox;
    private javax.swing.JLabel ingredientListLabel;
    private javax.swing.JScrollPane ingredientListPane;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JComboBox recipeList;
    private javax.swing.JTextField servingsBox;
    private javax.swing.JButton servingsButton;
    // End of variables declaration//GEN-END:variables

    //Accessor methods for the various GUI components
    //We need these so that the regression tests can use them
    public javax.swing.JButton getDeleteRecipeButton()
    {
        return deleteRecipeButton;
    }
        
    public javax.swing.JTextArea getDirectionsBox()
    {
        return directionsBox;
    }
    
    public javax.swing.JButton getEditRecipeButton()
    {
        return editRecipeButton;
    }
    
    public javax.swing.JButton getGetRecipeButton()
    {
        return getRecipeButton;
    }
        
    public javax.swing.JComboBox getImageList()
    {
        return imageList;
    }   
    
    public javax.swing.JTextArea getIngredientListBox()
    {
        return ingredientListBox;
    }
        
    public javax.swing.JComboBox getRecipeList()
    {
        return recipeList;
    }  
    
    public javax.swing.JTextField getServingsBox()
    {
        return servingsBox;
    } 
        
    public javax.swing.JButton getServingsButton()
    {
        return servingsButton;
    }  

    private void convertIngredient(int index) {
        switch (ingredients.get(index).getUnit()) {
            // SOLID MEASURE
            // ounces - grams
            case("oz"):
            case("ounce"):
            case("ounces"):
                ingredients.get(index).setUnit("grams");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*28.35);
                break;
            case("g"):
            case("gram"):
            case("grams"):
                ingredients.get(index).setUnit("oz");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()/28.35);
                break;
            case("lb"):
            case("lbs"):
            case("pound"):
            case("pounds"):
                ingredients.get(index).setUnit("kg");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*0.453);
                break;
            case("kg"):
            case("kilogram"):
            case("kilograms"):
                ingredients.get(index).setUnit("lbs");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()/0.453);
                break;   
            // LIQUID MEASURE
            // We'll ignore such units as tablespoons, teaspoons, etc.,
            // and assume those are more or less equivalent worldwide.
            // cups - milliliters
            case("cup"):
            case("cups"):
                ingredients.get(index).setUnit("ml");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*236.5);
                break;
            // convert to ml, then convert to cups
            case("dl"):
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*10);
            case("cl"):
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*10);
            case("ml"):
                ingredients.get(index).setUnit("cups");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()/236.5);
                break;
            // liters - quarts
            // convert gallons to quarts, then to liters
            case("gal"):
            case("gallon"):
            case("gallons"):
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*4);
            case("quart"):
            case("quarts"):
                ingredients.get(index).setUnit("liters");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()*0.946);  
                break;
            case("l"):
            case("liter"):
            case("liters"):
                ingredients.get(index).setUnit("quarts");
                ingredients.get(index).setQuantity
                    (ingredients.get(index).getQuantity()/0.946);
                break;
            default:
                break;
        }
    }
}
