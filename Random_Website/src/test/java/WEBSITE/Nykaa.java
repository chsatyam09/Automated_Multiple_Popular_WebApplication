package WEBSITE;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Nykaa {
    public static ChromeDriver driver;

    // Window Handling
    public List<String> windowHandling() {
        Set<String> setHandle = driver.getWindowHandles();
        return new ArrayList<>(setHandle);
    }

    // Launch Nykaa e-commerce site in Chrome Browser and get Title
    public void launchBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://www.nykaa.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        String title = driver.getTitle();
        if (title.contains("Nykaa"))
            System.out.println("Nykaa launched successfully: " + title);
        else
            System.err.println("Nykaa launch failed");
    }

    public static void main(String[] args) throws InterruptedException {
        // Setting up objects
    	Nykaa obj = new Nykaa();
        obj.launchBrowser();
        Actions builder = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Mouse hover on Brands and Popular
        WebElement brands = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='brands']")));
        builder.moveToElement(brands).perform();
        WebElement popular = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Popular']")));
        builder.moveToElement(popular).perform();
        System.out.println("Brand and Popular selected");

        // Click on L'Oreal Paris
        WebElement lorealParis = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//a[contains(@href,'loreal-paris')])[3]")));
        lorealParis.click();
        System.out.println("Suceefully Clicked on L'Oreal Paris");
        
        // Click sort By and select customer top rated
        WebElement sortBy = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@class='sort-name']")));
        sortBy.click();
        WebElement topRated = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[normalize-space()='customer top rated']")));
        topRated.click();
        System.out.println("Sorted by customer top rated");
//----------------------------------------------------------------------------------------------------------------------------------------------------
        // Click Category and filter Shampoo
        WebElement category = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"filters-strip\"]/div/div/div[2]/div")));
        category.click();
        WebElement shampooFilter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='chk_Shampoo_undefined']")));
        if (!shampooFilter.isSelected()) {
            shampooFilter.click();
            System.out.println("Shampoo is filtered now");
        } else {
            System.out.println("Shampoo is filtered by default");
        }

        // Click on L'Oreal Paris Colour Protect Shampoo
        WebElement product = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'paris-color-protect')]//img")));
        product.click();

        // Navigate to the product window
        driver.switchTo().window(obj.windowHandling().get(2));
        if (driver.getTitle().contains("L'Oreal Paris Colour Protect")) {
            System.out.println("Navigated to the product");
        } else {
            System.err.println("Product Navigation failed");
        }

        // Select size as 175ml
        WebElement size = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='175ml']")));
        size.click();
        System.out.println("Size selected as 175ml");

        // Print price of the product
        String productPrice = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//div[@class='price-info']//span)[4]"))).getText();
        System.out.println("Product price: " + productPrice);

        // Add Product to bag and confirm
        WebElement addToBag = driver.findElement(By.xpath("//span[contains(@class,'Shopping-Bag')]/parent::button"));
        addToBag.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text()='Product has been added to bag.']")));
        System.out.println("Product has been added to bag message is displayed");

        // Go to Shopping bag
        WebElement shoppingBag = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='AddToBagbox']")));
        shoppingBag.click();

        // Print the Grand Total amount
        String grandTotal = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='payment-tbl-data']/div[4]/div[2]"))).getText();
        System.out.println("Grand total: " + grandTotal);

        // Click to Proceed
        WebElement proceed = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(@class,'proceed')]")));
        proceed.click();

        // Continue as Guest
        WebElement continueAsGuest = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='CONTINUE AS GUEST']")));
        continueAsGuest.click();

        // Print warning message
        String warningMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'lockdown')]"))).getText();
        System.out.println("Warning message: " + warningMsg);

        // Close all windows
        List<String> handles = obj.windowHandling();
        for (String handle : handles) {
            driver.switchTo().window(handle).close();
        }
        System.out.println("Closed all windows");
    }
}
