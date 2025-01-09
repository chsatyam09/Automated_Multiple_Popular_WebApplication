package WEBSITE;


import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

public class Myntra {
    public static ChromeDriver driver;

    // Convert String to Integer
    public int stringToInt(String str) {
        String strValue = str.replaceAll("\\D", "");
        return Integer.parseInt(strValue);
    }

    // Launch Myntra e-commerce site in Chrome Browser and get Title
    public void launchBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        driver.get("https://www.myntra.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        String title = driver.getTitle();
        System.out.println("Myntra launched successfully: " + title);
    }

    public static void main(String[] args) {
        Myntra obj = new Myntra();
        try {
            // Launch the browser
            obj.launchBrowser();

            // Object creation for Actions class
            Actions builder = new Actions(driver);

            // Mouse over on Women category
            WebElement ele1 = driver.findElement(By.xpath("//a[text()='Women']"));
            builder.moveToElement(ele1).perform();

            // Click Jackets & Coats
            driver.findElement(By.xpath("//a[text()='Jackets & Coats']")).click();

            // Fetch the total count of items
            String strTotal = driver.findElement(By.xpath("//span[@class='title-count']")).getText();
            int totalItems = obj.stringToInt(strTotal);

            // Ensure the total items count matches with the categories split
            String strJacket = driver.findElement(By.xpath("//span[@class='categories-num']")).getText();
            String strCoat = driver.findElement(By.xpath("(//span[@class='categories-num'])[2]")).getText();
            int totalOfJacketsAndCoats = obj.stringToInt(strJacket) + obj.stringToInt(strCoat);

            if (totalItems == totalOfJacketsAndCoats) {
                System.out.println("Total count matches with the split-up: " + totalItems);
            }

            // Select Coats Category
            driver.findElement(By.xpath("(//span[@class='categories-num'])[2]")).click();

            // Click for More options under Brand section
            driver.findElement(By.xpath("//div[@class='brand-more']")).click();

            // Search and select MANGO brand
            driver.findElement(By.className("FilterDirectory-searchInput")).sendKeys("MANGO");
            driver.findElement(By.xpath("(//input[@value='MANGO']/parent::label)[2]")).click();

            // Close the pop-up box after search
            driver.findElement(By.xpath("//span[contains(@class,'FilterDirectory-close')]")).click();
            Thread.sleep(1000);

            // Confirm all coats are from brand MANGO
            List<WebElement> brandList = driver.findElements(By.xpath("//h3[@class='product-brand']"));
            boolean flag = true;

            for (WebElement eachBrand : brandList) {
                String brand = eachBrand.getText();
                if (!brand.equalsIgnoreCase("MANGO")) {
                    flag = false;
                    break;
                }
            }

            if (flag) {
                System.out.println("All coats are from brand MANGO");
            } else {
                System.out.println("Some coats are not from brand MANGO");
            }

            // Sort by Better Discount
            WebElement ele2 = driver.findElement(By.xpath("//div[@class='sort-sortBy']"));
            builder.moveToElement(ele2).perform();
            driver.findElement(By.xpath("//ul[@class='sort-list']/li[3]/label")).click();
            Thread.sleep(500);

            // Find the price of the first displayed item
            List<WebElement> allItemsPrice = driver.findElements(By.xpath("//span[@class='product-discountedPrice']"));
            String firstItemPrice = allItemsPrice.get(0).getText();
            System.out.println("Price of first displayed coat is: " + firstItemPrice);

            // Mouse over on size of the first item
            WebElement ele3 = allItemsPrice.get(0);
            builder.moveToElement(ele3).perform();

            // Click on WishList Now and ensure being redirected to login
            driver.findElement(By.xpath("//div[contains(@class,'product-actions')]/span/span")).click();
            String loginTitle = driver.getTitle();
            System.out.println("Back to login page: " + loginTitle);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close Browser
            driver.quit();
        }
    }
}
