package WEBSITE;



import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Snapdeal {

    public static void main(String[] args) throws InterruptedException {

        // Launch Snapdeal site
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.snapdeal.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        String title = driver.getTitle();
        if (title.contains("Snapdeal"))
            System.out.println("Snapdeal launched successfully: " + title);
        else
            System.err.println("Snapdeal launch failed");

        // Mouse over on Toys, Kids' Fashion & more and click on Toys
        Actions builder = new Actions(driver);
        builder.moveToElement(driver.findElement(By.linkText("Toys, Kids' Fashion & more"))).perform();
        builder.moveToElement(driver.findElement(By.xpath("//span[text()='Toys']/parent::a"))).click().build().perform();

        // Click Educational Toys in Toys & Games
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Toys & Games']/parent::a/following-sibling::ul//div[text()='Educational Toys']")).click();
        if (driver.getTitle().contains("Educational Toys")) {
            System.out.println("Navigated to Educational Toys page");
        }

        // Click the Customer Rating 4 star and Up
        driver.findElement(By.xpath("//label[@for='avgRating-4.0']")).click();
        Thread.sleep(3000);
        if (driver.findElement(By.xpath("//a[text()='4.0']")).isDisplayed()) {
            System.out.println("Products listed based on customer rating of 4.0 and above");
        }

        // Click the offer as 40-50
        driver.findElement(By.xpath("//label[@for='discount-40%20-%2050']")).click();
        Thread.sleep(2000);
        if (driver.findElement(By.xpath("//a[text()='40 - 50']")).isDisplayed()) {
            System.out.println("Products listed based on discount of 40-50%");
        }

        // Check the availability for the pincode
        driver.findElement(By.xpath("//input[@placeholder='Enter your pincode']")).sendKeys("600026");
        driver.findElement(By.xpath("//button[text()='Check']")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        String pincodeCheck = driver.findElement(By.id("myPincode")).getText();
        if (pincodeCheck.equalsIgnoreCase("600026")) {
            System.out.println("Delivery available for the pincode");
        }

        // Click the Quick View of the first product
        builder.moveToElement(driver.findElement(By.xpath("(//section[@data-dpwlbl='Product Grid'])[1]/div[1]"))).perform();
        Thread.sleep(1000);
        driver.findElement(By.xpath("(//section[@data-dpwlbl='Product Grid'])[1]/div[1]//div[@class='clearfix row-disc']/div")).click();

        // Click on View Details
        Thread.sleep(1000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[contains(text(),'view details')]"))));
        driver.findElement(By.xpath("//a[contains(text(),'view details')]")).click();

        // Capture the Price of the Product and Delivery Charge
        Thread.sleep(2000);
        String rawToyPrice = driver.findElement(By.xpath("//span[@class='pdp-final-price']/span")).getText();
        String rawToyDelCharge = driver.findElement(By.xpath("//span[contains(text(),'Delivery')]/parent::span//span[@class='availCharges']")).getText();
        System.out.println("Price of the toy: " + rawToyPrice);
        System.out.println("Delivery charge of the toy: " + rawToyDelCharge);
        driver.findElement(By.xpath("//span[text()='add to cart']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//span[contains(text(),'added to your cart')]"))));
        if (driver.findElement(By.xpath("//span[@class='mess-text']")).getText().contains("added to your cart")) {
            System.out.println("Toy added to cart");
        }

        // Validate the You Pay amount matches the sum of (price + delivery charge)
        String rawToyTotalPrice = driver.findElement(By.xpath("//div[@class='you-pay']/span[@class='price']")).getText();
        int toyPrice = Integer.parseInt(rawToyPrice);
        int toyDelCharge = Integer.parseInt(rawToyDelCharge.replaceAll("\\D", ""));
        int toyTotalPrice = Integer.parseInt(rawToyTotalPrice.replaceAll("\\D", ""));
        if (toyTotalPrice == (toyPrice + toyDelCharge)) {
            System.out.println("Total price matches with subtotal of Toy: " + toyTotalPrice);
        } else {
            System.err.println("Price mismatch for Toy");
        }

        // Search for Sanitizer
        driver.findElement(By.id("inputValEnter")).sendKeys("Sanitizer");
        Thread.sleep(500);
        driver.findElement(By.xpath("//a[@data-labelname='sanitizer']")).click();

        // Click on Product "BioAyurveda Neem Power Hand Sanitizer"
        builder.moveToElement(driver.findElement(By.xpath("//p[contains(text(),'BioAyurveda Neem Power  Hand Sanitizer')]/ancestor::div[2]/preceding-sibling::div[1]"))).perform();
        Thread.sleep(1000);
        driver.findElement(By.xpath("//p[contains(text(),'BioAyurveda Neem Power  Hand Sanitizer')]/ancestor::div[3]//div[@class='clearfix row-disc']//div")).click();

        // Capture the Price and Delivery Charge
        Thread.sleep(2000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[contains(text(),'view details')]"))));
        driver.findElement(By.xpath("//a[contains(text(),'view details')]")).click();
        Thread.sleep(2000);
        String rawSanitizerPrice = driver.findElement(By.xpath("//span[@class='pdp-final-price']/span")).getText();
        String rawSanitizerDeliveryCharge = driver.findElement(By.xpath("//span[contains(text(),'Delivery')]/parent::span//span[@class='availCharges']")).getText();
        System.out.println("Price of the sanitizer: " + rawSanitizerPrice);
        System.out.println("Delivery charge of the sanitizer: " + rawSanitizerDeliveryCharge);

        // Click on Add to Cart
        driver.findElement(By.xpath("//span[text()='ADD TO CART']")).click();

        // Click on Cart
        Thread.sleep(2000);
        driver.findElement(By.xpath("//span[text()='Cart']")).click();
        Thread.sleep(2000);
        String strSanitizerTotalPrice = driver.findElement(By.xpath("//a[text()='BioAyurveda Neem Power  Hand Sanitizer 500 mL Pack of 1']/ancestor::div[3]/following-sibling::div[contains(@class,'item-details')]//span")).getText();
        int sanitizerTotalPrice = Integer.parseInt(strSanitizerTotalPrice.replaceAll("\\D", ""));

        // Validate the Proceed to Pay matches the total amount of both the products
        Thread.sleep(2000);
        String strGrandTotal = driver.findElement(By.xpath("//input[contains(@value,'PROCEED TO PAY')]")).getAttribute("value");
        int grandTotal = Integer.parseInt(strGrandTotal.replaceAll("\\D", ""));
        if (grandTotal == (toyTotalPrice + sanitizerTotalPrice)) {
            System.out.println("Grand total matches with subtotal of products added: " + grandTotal);
        } else {
            System.err.println("Total mismatch");
        }

        // Close browser
        driver.close();
    }
}
