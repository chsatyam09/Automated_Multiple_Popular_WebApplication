package WEBSITE;



import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PepperFry {

    public static void main(String[] args) throws InterruptedException, IOException {
        // Launch PepperFry
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.get("https://www.pepperfry.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        String title = driver.getTitle();
        if (title.contains("Pepperfry")) {
            System.out.println("Pepperfry launched successfully: " + title);
        } else {
            System.err.println("Pepperfry launch failed");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        Actions builder = new Actions(driver);

        // Close pop-up
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='reg-modal-right-frm-wrap']"))));
        driver.findElement(By.xpath("(//a[@class='popup-close'])[5]")).click();

        // Mouse hover on Furniture and click Office Chairs under Chairs
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[text()='Furniture']"))));
        builder.moveToElement(driver.findElement(By.xpath("//a[text()='Furniture']"))).perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[text()='Office Chairs']"))));
        builder.moveToElement(driver.findElement(By.xpath("//a[text()='Office Chairs']"))).click().build().perform();
        if (driver.getTitle().contains("Office Chairs")) {
            System.out.println("Navigated to Office chairs screen");
        } else {
            System.err.println("Office chairs screen is not loaded");
        }

        // Click Executive Chairs
        Thread.sleep(500);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='cat-wrap-img']/img[@alt='Executive Chairs']"))));
        JavascriptExecutor click = (JavascriptExecutor) driver;
        click.executeScript("arguments[0].click();", driver.findElement(By.xpath("//div[@class='cat-wrap-img']/img[@alt='Executive Chairs']")));
        if (driver.getTitle().contains("Executive Chair")) {
            System.out.println("Navigated to Executive chairs screen");
        } else {
            System.err.println("Executive chairs screen is not loaded");
        }

        // Change the minimum Height as 50 in under Dimensions
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[text()='Height']/parent::li/div[3]/input"))));
        WebElement heightInput = driver.findElement(By.xpath("//div[text()='Height']/parent::li/div[3]/input"));
        heightInput.clear();
        heightInput.sendKeys("50", Keys.ENTER);
        String eleHeightFilter = driver.findElement(By.xpath("//li[contains(@class,'fltrd')]")).getText();
        if (eleHeightFilter.equalsIgnoreCase("Height(50-55)")) {
            System.out.println("Executive chairs filtered with Height of 50-55 inches");
        }

        // Add "Poise Executive Chair in Black Colour" chair to Wishlist
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[text()='Poise Executive Chair in Black Colour']"))));
        driver.findElement(By.xpath("//a[text()='Poise Executive Chair in Black Colour']/ancestor::div[2]/following-sibling::div/div[2]/a")).click();

        // Mouseover on Homeware and Click Pressure Cookers under Cookware
        builder.moveToElement(driver.findElement(By.xpath("//a[text()='Homeware']"))).perform();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//a[text()='Pressure Cookers']"))));
        driver.findElement(By.xpath("//a[text()='Pressure Cookers']")).click();
        if (driver.findElement(By.xpath("//h1[text()='PRESSURE COOKERS']")).isDisplayed()) {
            System.out.println("Pressure cooker products are displayed");
        }

        // Select Prestige as Brand
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//label[text()='Prestige']"))));
        driver.findElement(By.xpath("//label[text()='Prestige']")).click();

        // Select Capacity as 1-3 Ltr
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//label[text()='1 Ltr - 3 Ltr']"))));
        driver.findElement(By.xpath("//label[text()='1 Ltr - 3 Ltr']")).click();
        if (driver.findElement(By.xpath("//li[text()='Prestige']")).isDisplayed() && driver.findElement(By.xpath("//li[text()='1 Ltr - 3 Ltr']")).isDisplayed()) {
            System.out.println("Prestige brand and capacity as 1-3 Ltr is selected");
        }

        // Add "Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr" to Wishlist
        Thread.sleep(500);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']"))));
        driver.findElement(By.xpath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']/ancestor::div[2]/following-sibling::div/div[2]/a")).click();

        // Verify the number of items in Wishlist
        Thread.sleep(1000);
        String wishlistCount = driver.findElement(By.xpath("//a[contains(@class,'wistlist_img')]/following-sibling::span")).getText();
        if (wishlistCount.equalsIgnoreCase("2")) {
            System.out.println("Number of items wishlisted: " + wishlistCount);
        } else {
            System.err.println("Wishlist count does not match as expected");
        }

        // Navigate to Wishlist
        driver.findElement(By.xpath("//a[contains(@class,'wistlist_img')]")).click();

        // Move Pressure Cooker only to Cart from Wishlist
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr']"))));
        driver.findElement(By.xpath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr By...']/parent::p/following-sibling::div/div/a")).click();
        if (driver.findElement(By.xpath("//a[text()='Nakshatra Cute Metallic Red Aluminium Cooker 2 Ltr By Prestige']")).isDisplayed()) {
            System.out.println("Pressure cooker added to cart");
        } else {
            System.err.println("Pressure cooker not present");
        }

        // Check for the availability for Pincode 600128
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[text()='Showing availability at']/following-sibling::input"))));
        driver.findElement(By.xpath("//span[text()='Showing availability at']/following-sibling::input")).sendKeys("600128", Keys.ENTER);
        Thread.sleep(1000);

        // Valid pincode
        if (driver.findElement(By.xpath("//div[@class='item_cta']/p")).getText().contains("Use Coupon ")) {
            System.out.println("Product can be delivered to the pincode specified");
            // Click Proceed to Pay Securely
            driver.findElement(By.xpath("//a[text()='Proceed to pay securely ']")).click();
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[text()='PLACE ORDER']"))));
            // Click Place order
            driver.findElement(By.xpath("//a[text()='PLACE ORDER']")).click();
            wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//span[text()='ORDER SUMMARY']/parent::div/following-sibling::div/span"))));
            driver.findElement(By.xpath("//span[text()='ORDER SUMMARY']/parent::div/following-sibling::div/span")).click();
            // Capture the screenshot of the item under Order Item
            File source = driver.findElement(By.xpath("//div[@class='slick-track']/li")).getScreenshotAs(OutputType.FILE);
            File dest = new File("./snap/product.png");
            FileUtils.copyFile(source, dest);
        }

        // Invalid Pincode or valid pincode where product is not available for delivery
        else {
            System.err.println("Product cannot be delivered to the pincode specified");
        }

        // Close browser
        driver.close();
    }
}

