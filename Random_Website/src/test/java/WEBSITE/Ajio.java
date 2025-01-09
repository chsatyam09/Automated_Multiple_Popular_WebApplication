package WEBSITE;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Ajio {
    public static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {
        // Set up Chrome driver with options
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");
        driver = new ChromeDriver(options);

        // Launch AJIO website
        driver.get("https://www.ajio.com/shop/sale");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        String title = driver.getTitle();
        if (title.contains("AJIO")) {
            System.out.println("AJIO launched successfully: " + title);
        } else {
            System.err.println("AJIO launch failed");
        }

        Actions build = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        // Search for "Bags" and select "Women Handbags"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Search AJIO']"))).sendKeys("Bags");
        Thread.sleep(1000);
    
        
     
        
        
        
        
        System.out.println(driver.findElement(By.xpath("//h1/div[2]")).getText());

        // Change to 5-grid view and sort by "What's New"
        driver.findElement(By.xpath("//div[@class='five-grid']/parent::div")).click();
        WebElement eleSort = driver.findElement(By.xpath("//div[@class='filter-dropdown']/select"));
        Select sortFilter = new Select(eleSort);
        sortFilter.selectByVisibleText("What's New");
        System.out.println("Sorted by 'What's New' and switched to 5-grid view");

        // Set price range to 2000 - 5000
        driver.findElement(By.xpath("//span[text()='price']")).click();
        driver.findElement(By.id("minPrice")).sendKeys("2000");
        driver.findElement(By.id("maxPrice")).sendKeys("5000");
        driver.findElement(By.xpath("//div[@class='facet-min-price-filter']/button")).click();
        System.out.println("Price range set: 2000 - 5000");

        // Select "Puma Ferrari LS Shoulder Bag"
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Ferrari LS Shoulder Bag']/ancestor::div[2]"))).click();
        Set<String> setHandle = driver.getWindowHandles();
        List<String> listHandle = new ArrayList<>(setHandle);
        driver.switchTo().window(listHandle.get(1));

        // Verify coupon applicability and calculate discounted price
        String brandName = driver.findElement(By.xpath("//div[@class='prod-content']/h2")).getText();
        String prodName = driver.findElement(By.xpath("//div[@class='prod-content']/h1")).getText();
        System.out.println("Brand: " + brandName + ", Product: " + prodName);

        String rawPrice = driver.findElement(By.xpath("//div[@class='prod-price-section']/div[@class='prod-sp']")).getText();
        int prodPrice = Integer.parseInt(rawPrice.replaceAll("\\D", ""));
        System.out.println("Product Price: " + prodPrice);

        if (prodPrice > 2690) {
            WebElement promoDesc = driver.findElement(By.xpath("//div[@class='promo-desc']"));
            if (promoDesc.getText().contains("Extra Upto 28% Off on 2690 and Above")) {
                String couponCode = driver.findElement(By.xpath("//div[@class='promo-title']")).getText();
                System.out.println("Applicable Coupon Code: " + couponCode);
            } else {
                System.err.println("No coupon available for this product.");
            }
        }

        String discountedPrice = driver.findElement(By.xpath("//div[text()='Get it for ']/span")).getText();
        int discPrice = Integer.parseInt(discountedPrice.replaceAll("\\D", ""));
        int couponAmount = prodPrice - discPrice;
        System.out.println("Discounted Price: " + discPrice + ", Coupon Savings: " + couponAmount);

        // Check product availability for a specific pincode
        driver.findElement(By.xpath("//span[contains(text(),'Enter pin-code')]")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='pincode']"))).sendKeys("682001");
        driver.findElement(By.xpath("//button[text()='CONFIRM PINCODE']")).click();
        String pincodeMsg = driver.findElement(By.xpath("//ul[@class='edd-message-success-details']/li")).getText();
        if (pincodeMsg.contains("Expected Delivery")) {
            System.out.println("Product available for specified pincode.");
        } else {
            System.err.println("Product not available for specified pincode.");
        }

        // Retrieve customer care details
        build.moveToElement(driver.findElement(By.xpath("//div[text()='Other information']"))).click().perform();
        Thread.sleep(500);
        String customerCareDetails = driver.findElement(By.xpath("//span[text()='Customer Care Address']/parent::li/span[@class='other-info']")).getText();
        System.out.println("Customer Care Details: " + customerCareDetails);

        // Add to Bag and navigate to Bag
        driver.findElement(By.xpath("//span[text()='ADD TO BAG']")).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='GO TO BAG']"))).click();
        System.out.println("Navigated to Shopping Bag");

        // Check Order Total and apply coupon
        String strOrderTotal = driver.findElement(By.xpath("//span[text()='Order Total']/following-sibling::span")).getText();
        int orderTotal = Integer.parseInt(strOrderTotal.replaceAll("\\D", ""));
        System.out.println("Order Total before applying coupon: " + orderTotal);

        driver.findElement(By.id("couponCodeInput")).sendKeys("EPIC");
        driver.findElement(By.xpath("//button[text()='Apply']")).click();
        System.out.println("Coupon applied");

        // Verify coupon savings
        String rawPriceAfterCoupon = driver.findElement(By.xpath("//span[@class='applied-coupon-section']/p")).getText();
        int priceAfterCoupon = Integer.parseInt(rawPriceAfterCoupon.replaceAll("\\D", ""));
        if (priceAfterCoupon == couponAmount) {
            System.out.println("Coupon savings verified: " + priceAfterCoupon);
        }

        // Delete item from Bag
        driver.findElement(By.xpath("//div[@class='product-delete']/div")).click();
        driver.findElement(By.xpath("//div[text()='DELETE']")).click();
        System.out.println("Item deleted from Bag");

        // Close browser
        driver.quit();
    }
}
