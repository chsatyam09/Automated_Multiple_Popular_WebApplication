package WEBSITE;



import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Airbnb {
    public static WebDriver driver;

    public static void main(String[] args) throws InterruptedException {
        Airbnb obj = new Airbnb();
        
        // Launch Airbnb site
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        System.setProperty("webdriver.chrome.silentOutput", "true");
        driver = new ChromeDriver();
        driver.get("https://www.airbnb.co.in/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        
        String title = driver.getTitle();
        if (title.contains("Airbnb"))
            System.out.println("Airbnb launched successfully: " + title);
        else
            System.err.println("Airbnb launch failed");
        
        Actions build = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, 20);

        // Type Coorg in location and select Coorg, Karnataka
        driver.findElement(By.xpath("//div[text()='Location']/following-sibling::input")).sendKeys("Coorg");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Coorg, Karnataka']")).click();
        System.out.println("Location selected as: " + driver.findElement(By.xpath("//div[text()='Coorg, Karnataka']")).getText());

        // Select the Start Date as June 1st and End Date as June 5th
        LocalDate date = LocalDate.now();
        LocalDate plusMonths = date.plusMonths(1);
        String month = plusMonths.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='" + month + " 2020']/parent::div/following-sibling::table//td[contains(@aria-label,'1')]")).click();
        driver.findElement(By.xpath("//div[text()='" + month + " 2020']/parent::div/following-sibling::table//td[contains(@aria-label,'5')]")).click();
        Thread.sleep(1000);
        String dateSelected = driver.findElement(By.xpath("//div[contains(text(),'Check in')]/following-sibling::div")).getText();
        System.out.println("Check-in and out date selected as: " + dateSelected);

        // Select guests as 6 adults, 3 children and click Search
        int adults = 6;
        int children = 3;
        driver.findElement(By.xpath("//div[text()='Add guests']/parent::button")).click();
        for (int i = 0; i < adults; i++) {
            driver.findElement(By.xpath("//div[@id='stepper-adults']/button[@aria-label='increase value']")).click();
            Thread.sleep(500);
        }
        for (int i = 0; i < children; i++) {
            driver.findElement(By.xpath("//div[@id='stepper-children']/button[@aria-label='increase value']")).click();
            Thread.sleep(500);
        }
        if (driver.findElement(By.xpath("//div[text()='Guests']/following-sibling::div")).getText().contains(Integer.toString(adults + children))) {
            System.out.println("Total guests inclusive of children: " + Integer.toString(adults + children));
        }
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        // Click Cancellation flexibility and enable the filter and Save
        driver.findElement(By.xpath("//span[text()='Cancellation flexibility']/ancestor::button[1]")).click();
        driver.findElement(By.xpath("//button[contains(@aria-labelledby,'flexible_cancellation')]")).click();
        driver.findElement(By.xpath("//button[text()='Save']")).click();
        if (driver.findElement(By.xpath("//span[@aria-label='Cancellation flexibility']/parent::button[@aria-pressed='true']")).isDisplayed()) {
            System.out.println("Cancellation flexibility is enabled");
        }

        // Select Type of Place as Entire Place and Save
        driver.findElement(By.xpath("//span[@aria-label='Type of place']/parent::button")).click();
        driver.findElement(By.xpath("//label[contains(@for,'Entire_home')]/span")).click();
        driver.findElement(By.xpath("//button[text()='Save']")).click();
        if (driver.findElement(By.xpath("//span[@aria-label='Entire place']/parent::button[@aria-pressed='true']")).isDisplayed()) {
            System.out.println("Entire place is selected");
        }

        // Set Min price as 3000 and max price as 5000
        driver.findElement(By.xpath("//span[@aria-label='Price']/parent::button")).click();
        Thread.sleep(1000);
        driver.findElement(By.id("price_filter_min")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "3000");
        driver.findElement(By.id("price_filter_max")).sendKeys(Keys.chord(Keys.CONTROL, "a"), "5000");
        driver.findElement(By.xpath("//button[text()='Save']")).click();
        if (driver.findElement(By.xpath("//span[contains(@aria-label,'3,000')]/parent::button[@aria-pressed='true']")).isDisplayed()) {
            System.out.println("Price range 3000-5000 is selected");
        }

        // Click More Filters and set 3 Bedrooms and 3 Bathrooms
        Thread.sleep(1000);
        driver.findElement(By.xpath("//span[text()='More filters']/parent::button")).click();
        Thread.sleep(1000);
        int beds = 3, baths = 3;
        for (int i = 0; i < beds; i++) {
            driver.findElement(By.xpath("//div[@id='filterItem-stepper-min_bedrooms-0']/button[@aria-label='increase value']")).click();
            Thread.sleep(250);
        }
        for (int i = 0; i < baths; i++) {
            driver.findElement(By.xpath("//div[@id='filterItem-stepper-min_bathrooms-0']/button[@aria-label='increase value']")).click();
            Thread.sleep(250);
        }
        driver.findElement(By.xpath("//div[contains(@class,'accept-cookie')]/button[@aria-label='OK']")).click();

        // Check the Amenities with Kitchen, Facilities with Free parking on premises, Property as House and Host Language as English
        Thread.sleep(1000);
        driver.findElement(By.xpath("//div[text()='Kitchen']")).click();
        driver.findElement(By.xpath("//div[text()='Free parking on premises']")).click();
        driver.findElement(By.xpath("//div[text()='House']")).click();
        driver.findElement(By.xpath("//div[text()='English']")).click();
        Thread.sleep(1000);
        String staysDisplayed = driver.findElement(By.xpath("//button[@type='button' and contains(text(),'Show')]")).getText();
        int staysAvailable = Integer.parseInt(staysDisplayed.replaceAll("\\D", ""));
        if (staysAvailable > 0) {
            System.out.println(staysAvailable + " stay(s) available for the filtered criteria");
            driver.findElement(By.xpath("//button[@type='button' and contains(text(),'Show')]")).click();
        } else {
            System.err.println("No stays available");
        }

        // Click Prahari Nivas, the complete house
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//a[@aria-label='Prahari Nivas, the complete house']"))));
        driver.findElement(By.xpath("//a[@aria-label='Prahari Nivas, the complete house']")).click();
        Set<String> setHandle = driver.getWindowHandles();
        List<String> listHandle = new ArrayList<String>(setHandle);
        driver.switchTo().window(listHandle.get(1));

        // Click on "Show all * amenities"
        Thread.sleep(5000);
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[contains(text(),'amenities')]"))));
        build.moveToElement(driver.findElement(By.xpath("//*[contains(text(),'amenities')]"))).click().build().perform();
        Thread.sleep(2000);
        List<WebElement> eleNAamenities = driver.findElements(By.xpath("//*[text()='Not included']/parent::*/following-sibling::div//del"));
        System.out.println("Amenities not included are as follows:");
        for (int i = 0; i < eleNAamenities.size(); i++) {
            System.out.println(eleNAamenities.get(i).getText());
        }
        Thread.sleep(2000);
        driver.findElement(By.xpath("//*[@aria-label='Amenities']//button")).click();

        // Verify the Check-in date, Check-out date, and Guests
        String checkIn = "";
        String checkOut = "";
        String guests = "";
        try {
            checkIn = driver.findElement(By.xpath("//*[text()='Check-in']/following-sibling::*")).getText();
            checkOut = driver.findElement(By.xpath("//*[text()='Checkout']/following-sibling::*")).getText();
            System.out.println("Check-in date: " + checkIn);
            System.out.println("Check-out date: " + checkOut);
            guests = driver.findElement(By.xpath("//label[@for='GuestPicker-book_it-trigger']//span")).getText();
            System.out.println("No. of guests: " + guests);
        } catch (Exception e) {
            checkIn = driver.findElement(By.xpath("//span[@id='DateInput__screen-reader-message-checkin']/following-sibling::div")).getText();
            checkOut = driver.findElement(By.xpath("//span[@id='DateInput__screen-reader-message-checkout']/following-sibling::div")).getText();
            System.out.println("Check-in date: " + checkIn);
            System.out.println("Check-out date: " + checkOut);
            guests = driver.findElement(By.xpath("//span[@class='guest-label__text guest-label__text-guests']")).getText();
            System.out.println("No. of guests: " + guests);
        }

        // Read all the Sleeping arrangements and Print
        List<WebElement> eleBedrooms = driver.findElements(By.xpath("//div[contains(text(),'Bedroom')]"));
        List<WebElement> eleBeds = driver.findElements(By.xpath("//div[contains(text(),'bed')]"));
        Map<String, String> map = new LinkedHashMap<String, String>();
        Thread.sleep(1000);
        WebElement eleNext = driver.findElement(By.xpath("//div[@data-plugin-in-point-id='SLEEPING_ARRANGEMENT_DEFAULT']//*[contains(@style,'right')]//button"));
        int j = 0;
        try {
            while (j == 0) {
                if (eleNext.isDisplayed()) {
                    for (int i = 0; i < eleBedrooms.size(); i++) {
                        if (eleBedrooms.get(i).getText() != "" && eleBeds.get(i).getText() != "") {
                            map.put(eleBedrooms.get(i).getText(), eleBeds.get(i).getText());
                        }
                    }
                    eleNext.click();
                    Thread.sleep(1000);
                }
            }
        } catch (Exception e) {
            for (int i = 0; i < eleBedrooms.size(); i++) {
                map.put(eleBedrooms.get(i).getText(), eleBeds.get(i).getText());
            }
        }
        for (Entry<String, String> eachEntry : map.entrySet()) {
            System.out.println(eachEntry.getKey() + "-->" + eachEntry.getValue());
        }

        // Close browser
        driver.quit();
    }
}

