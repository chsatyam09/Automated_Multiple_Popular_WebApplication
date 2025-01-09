package WEBSITE;



import static org.testng.Assert.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class BigBasket {
    public static ChromeDriver driver;

    public static void main(String[] args) throws InterruptedException {
        // Setup WebDriver using WebDriverManager
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        // Launch BigBasket site
        driver.get("https://www.bigbasket.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        String title = driver.getTitle();

        if (title.contains("bigbasket")) {
            System.out.println("BigBasket launched successfully: " + title);
        } else {
            fail("BigBasket launch failed");
        }

        BigBasket bb = new BigBasket();
        WebDriverWait wait = new WebDriverWait(driver, 10);
        Actions builder = new Actions(driver);

        // Set location pincode
        driver.findElement(By.xpath("//span[@class='arrow-marker']")).click();
        driver.findElement(By.id("areaselect")).sendKeys("600026");
        Thread.sleep(500);
        driver.findElement(By.id("areaselect")).sendKeys(Keys.ENTER);
        driver.findElement(By.xpath("//button[text()='Continue']")).click();

        // Mouse hover on Shop by Category
        builder.moveToElement(driver.findElement(By.xpath("//a[text()=' Shop by Category ']"))).perform();

        // Navigate to Foodgrains, Oil & Masala --> Rice & Rice Products
        builder.moveToElement(driver.findElement(By.xpath("(//a[text()='Foodgrains, Oil & Masala'])[2]"))).build().perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("(//a[text()='Rice & Rice Products'])[2]"))));
        builder.moveToElement(driver.findElement(By.xpath("(//a[text()='Rice & Rice Products'])[2]"))).build().perform();

        // Click on Boiled & Steam Rice
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("(//a[text()='Boiled & Steam Rice'])[2]"))));
        builder.moveToElement(driver.findElement(By.xpath("(//a[text()='Boiled & Steam Rice'])[2]"))).click().build().perform();
        if ((driver.getTitle()).contains("Boiled Steam Rice")) {
            System.out.println("Navigated to Boiled & Steam Rice products page");
        } else {
            fail("Navigation to Boiled & Steam Rice page failed");
        }

        // Choose the Brand as bb Royal
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[text()='bb Royal']"))));
        driver.findElement(By.xpath("//span[text()='bb Royal']")).click();
        Thread.sleep(1000);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[contains(@class,'prod-name')]/h6"))));
        int count = 0;
        List<WebElement> eleBrandNames = driver.findElements(By.xpath("//div[contains(@class,'prod-name')]/h6"));
        Thread.sleep(2000);
        for (WebElement eachBrand : eleBrandNames) {
            if ((eachBrand.getText()).equalsIgnoreCase("bb Royal")) {
                count++;
            }
        }
        if (count == eleBrandNames.size()) {
            System.out.println("bb Royal brand products are displayed");
        }

        // Additional code remains unchanged...

        // Close browser
        driver.quit();
    }

    // Reusable method to verify Grand total against sub total of selected items
    public void verifyTotal(List<WebElement> eleProducts) throws InterruptedException {
        int prodCount = eleProducts.size();
        WebDriverWait wt = new WebDriverWait(driver, 10);
        wt.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//p[text()='Sub Total : ']/span/span"))));
        String strTotal = driver.findElement(By.xpath("//p[text()='Sub Total : ']/span/span")).getText();
        double grandTotal = Double.parseDouble(strTotal);
        System.out.println("Grand total: " + grandTotal);
        double splitTotal = 0;
        for (int i = 0; i < prodCount; i++) {
            String strItemPrice = driver.findElements(By.xpath("//div[@class='row mrp']/span")).get(i).getText();
            double itemPrice = Double.parseDouble(strItemPrice);
            System.out.println("Item " + (i + 1) + " price: " + itemPrice);
            Thread.sleep(1000);
            wt.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='product-qty ng-binding']"))));
            String rawstrQty = driver.findElements(By.xpath("//div[@class='product-qty ng-binding']")).get(i).getText();
            String[] split = rawstrQty.split("x");
            double itemQty = Double.parseDouble(split[0]);
            System.out.println("Item " + (i + 1) + " quantity: " + itemQty);
            splitTotal = splitTotal + (itemPrice * itemQty);
        }
        if (splitTotal == grandTotal) {
            System.out.println("Total matches with selected items: " + grandTotal);
        } else {
            fail("Total mismatch");
        }
    }
}
