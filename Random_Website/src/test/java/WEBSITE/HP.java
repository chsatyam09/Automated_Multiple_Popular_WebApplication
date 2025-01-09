package WEBSITE;



import static org.testng.Assert.fail;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HP {
    public static WebDriver driver;
    
    // Reusable method to handle random pop-up
    public void handlePopup() throws InterruptedException {
        System.out.println("Pop handling initiated");
        try {
            WebElement closeButton = driver.findElement(By.xpath("//div[contains(@class,'closeButton')]"));
            if (closeButton.isDisplayed()) {
                closeButton.click();
                System.out.println("Pop up closed");
            } else {
                System.out.println("Pop up did not occur");
            }
        } catch (Exception e) {
            System.out.println("No pop-up found.");
        }
    }
    
    // Launch HP site in Chrome Browser and get Title
    public void launchBrowser() {
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://store.hp.com/in-en/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        String title = driver.getTitle();
        if (title.contains("HP")) {
            System.out.println("HP launched successfully: " + title);
        } else {
            fail("HP launch failed");
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        HP hp = new HP();
        hp.launchBrowser();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions builder = new Actions(driver);
        
        // Mouse over on Laptops menu and click on Pavilion
        builder.moveToElement(driver.findElement(By.xpath("//span[text()='Laptops']"))).perform();
        builder.moveToElement(driver.findElement(By.xpath("//span[text()='Pavilion']"))).click().build().perform();
        if (driver.findElement(By.xpath("//h1[text()='Pavilion Laptops']")).isDisplayed()) {
            System.out.println("Navigated to Pavilion laptop products screen");
        }
        
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@aria-label='Close']")));
        driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
        
        // Under SHOPPING OPTIONS --> Processor --> Select Intel Core i7
        driver.findElement(By.xpath("(//span[text()='Processor'])[2]")).click();
        driver.findElement(By.xpath("//span[text()='Intel Core i7']")).click();
        if (driver.findElement(By.xpath("//div[@class='filter-current']//span[2]")).isDisplayed()) {
            System.out.println("Intel Core i7 Processor selected");
        }
        
        // Call popup handle method
        hp.handlePopup();
        
        // Hard Drive Capacity --> More than 1TB
        driver.findElement(By.xpath("//span[text()='More than 1 TB']")).click();
        if (driver.findElement(By.xpath("//div[@class='filter-current']//span[text()='More than 1 TB']")).isDisplayed()) {
            System.out.println("More than 1 TB is selected");
        }
        
        // Call popup handle method
        hp.handlePopup();
        
        // Select Sort By: Price: Low to High
        WebElement eleSelect = driver.findElement(By.id("sorter"));
        Select sortByPrice = new Select(eleSelect);
        sortByPrice.selectByValue("price_asc");
        if (driver.findElement(By.xpath("//option[@value='price_asc' and @selected='selected']")).isSelected()) {
            System.out.println("Sorted by price low to high");
        }
        Thread.sleep(500);
        
        // Print the First resulting Product Name and Price
        String productName = driver.findElement(By.xpath("//div[contains(@class,'product-item-details')]//a")).getText();
        System.out.println("Product name: " + productName);
        String productPrice = driver.findElement(By.xpath("//span[contains(@class,'final_price')]//span/span")).getText();
        System.out.println("Price: " + productPrice);
        
        // Add to Cart
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Add To Cart']/parent::button")));
        driver.findElement(By.xpath("//span[text()='Add To Cart']/parent::button")).click();
        
        // Click on Shopping cart icon and view cart
        WebElement eleClickCart = driver.findElement(By.xpath("//a[contains(@class,'showcart')]"));
        JavascriptExecutor click = (JavascriptExecutor) driver;
        click.executeScript("arguments[0].click();", eleClickCart);
        Thread.sleep(500);
        driver.findElement(By.xpath("//a[contains(@class,'viewcart')]")).click();
        if ((driver.getTitle()).contains("Shopping Cart")) {
            System.out.println("Landed on shopping cart screen");
        }
        
        // Check the Shipping Option --> Check availability at Pincode
        driver.findElement(By.name("pincode")).sendKeys("600078");
        Thread.sleep(500);
        driver.findElement(By.xpath("//button[text()='check']")).click();
        Thread.sleep(500);
        
        // If dispatch mode is available at valid pincode
        try {
            if (driver.findElement(By.xpath("//span[@class='available']")).isDisplayed()) {
                System.out.println("Product available for delivery at selected pincode");
                
                // Verify the order Total against the product price
                if ((driver.findElement(By.xpath("//tr[@class='grand totals']/td//span")).getText())
                        .equalsIgnoreCase(productPrice)) {
                    System.out.println("Product and price in cart matches with the one selected");
                    System.out.println("Grand total: " + productPrice);
                }
                // Proceed to Checkout if Order Total and Product Price matches
                driver.findElement(By.xpath("//span[text()='Proceed to Checkout']/parent::button")).click();
                Thread.sleep(500);
                
                // Click on Place Order
                driver.findElement(By.xpath("(//span[text()='Place Order'])[4]/parent::button")).click();
                
                // Capture error message
                String errorMsg = driver.findElement(By.xpath("//div[@class='message notice']/span")).getText();
                System.err.println("Mandatory details are not filled in: " + errorMsg);
            }
        } catch (Exception e) {
            System.err.println("Dispatch cannot happen at the pincode specified");
        }
        
        // Close browser
        driver.quit();
    }
}
