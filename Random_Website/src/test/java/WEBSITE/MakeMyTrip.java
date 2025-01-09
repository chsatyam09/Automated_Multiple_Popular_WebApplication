package WEBSITE;


import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MakeMyTrip {
    public static ChromeDriver driver;

    // Launch MakeMyTrip website in Chrome Browser and get Title
    public void launchBrowser() {
        System.setProperty("webdriver.chrome.driver", "./Drivers/chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("https://www.makemytrip.com/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        String title = driver.getTitle();
        if (title.contains("MakeMyTrip"))
            System.out.println("MakeMyTrip launched successfully: " + title);
        else
            System.err.println("MakeMyTrip launch failed");
    }

    public static void main(String[] args) throws InterruptedException {

        // Setting up objects
        MakeMyTrip obj = new MakeMyTrip();
        obj.launchBrowser();
        Actions builder = new Actions(driver);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Click Hotels
        driver.findElement(By.xpath("(//a[contains(@href,'hotels')])[1]")).click();
        System.out.println("Navigated to Search Hotels screen");

        // Enter city as Goa, and choose Goa, India
        WebElement eleCity = driver.findElement(By.id("city"));
        builder.moveToElement(eleCity).click().perform();
        driver.findElement(By.xpath("//input[contains(@class,'react-autosuggest')]")).sendKeys("Goa");
        Thread.sleep(500);
        driver.findElement(By.xpath("//input[contains(@class,'react-autosuggest')]")).sendKeys(Keys.TAB);

        // Enter Check-in date as May 15 and add 5 for end date
        driver.findElement(By.id("checkin")).click();
        String startDate = driver.findElement(By.xpath("(//div[text()='15'])[2]")).getText();
        driver.findElement(By.xpath("(//div[text()='15'])[2]")).click();
        int endDate = Integer.parseInt(startDate) + 5;
        Thread.sleep(500);
        driver.findElement(By.xpath("(//div[text()='" + endDate + "'])[2]")).click();

        // Print selected dates
        String checkinDate = driver.findElement(By.xpath("//p[@data-cy='checkInDate']")).getText();
        String checkoutDate = driver.findElement(By.xpath("//p[@data-cy='checkOutDate']")).getText();
        System.out.println("Check-in date selected :" + checkinDate);
        System.out.println("Check-out date selected :" + checkoutDate);

        // Click on ROOMS & GUESTS and click 2 Adults and one Child (age 12). Click Apply Button
        driver.findElement(By.id("guest")).click();
        Thread.sleep(500);
        WebElement eleAdults = driver.findElement(By.xpath("(//li[@class='selected'])[1]"));
        if (!eleAdults.isSelected()) {
            driver.findElement(By.xpath("(//li[@class='selected'])[1]")).click();
            System.out.println("Adults selected as 2");
        } else
            System.out.println("Adults defaulted as 2");
        driver.findElement(By.xpath("//li[@data-cy='children-1']")).click();
        WebElement eleChildAge = driver.findElement(By.xpath("//label[@class='lblAge']/select"));
        eleChildAge.click();
        Select selectAge = new Select(eleChildAge);
        selectAge.selectByVisibleText("12");
        System.out.println("1 child added");
        driver.findElement(By.xpath("//button[text()='APPLY']")).click();
        String roomGuestCount = driver.findElement(By.xpath("//p[@data-cy='roomGuestCount']")).getText();
        System.out.println(roomGuestCount);

        // Click Search button
        driver.findElement(By.xpath("//button[text()='Search']")).click();

        // Search Locality as Baga and 5-star in star category
        driver.findElement(By.xpath("//body[contains(@class,'overlayWholeBlack')]")).click();
        driver.findElement(By.xpath("//div[contains(@id,'locality')]//ul/li[4]//label")).click();
        WebElement eleStar = driver.findElement(By.xpath("//div[contains(@id,'star_category')]//li//label[1]"));
        JavascriptExecutor click = (JavascriptExecutor) driver;
        click.executeScript("arguments[0].click();", eleStar);
        String confirmFilter = driver.findElement(By.xpath("//ul[@class='appliedFilters']")).getText();
        System.out.println("Star category and Locality selected are: " + confirmFilter);

        // Click on the first resulting hotel, go to the new window and Print hotel name
        driver.findElement(By.id("Listing_hotel_0")).click();
        Set<String> setHandles = driver.getWindowHandles();
        List<String> listHandles = new ArrayList<>(setHandles);
        driver.switchTo().window(listHandles.get(1));
        String hotelTitle = driver.getTitle();
        if (hotelTitle.contains("Acron Waterfront")) {
            System.out.println("Navigated to Acron Waterfront hotel page");
        } else
            System.err.println("Hotel navigation failed");
        String hotelName = driver.findElement(By.id("detpg_hotel_name")).getText();
        System.out.println("Hotel name: " + hotelName);

        // Click MORE OPTIONS link and Select 3-Month plan and close
        driver.findElement(By.xpath("//span[text()='MORE OPTIONS']")).click();
        driver.findElement(By.xpath("//table[@class='tblEmiOption']//tr[2]/td[@class='textRight']/span")).click();
        driver.findElement(By.className("close")).click();

        // Click on BOOK THIS NOW
        Thread.sleep(500);
        driver.findElement(By.xpath("//a[text()='BOOK THIS NOW']")).click();
        System.out.println("Hotel booking initiated");

        // Print the Total Payable amount
        String totalPayable = driver.findElement(By.id("revpg_total_payable_amt")).getText();
        System.out.println("Payable amount: " + totalPayable);

        // Close browser
        driver.quit();
    }
}
