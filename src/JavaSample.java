import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import static java.lang.Thread.*;
import javax.swing.JOptionPane;

public class JavaSample {

    public static  WebDriver driver;
    public static final String DRIVER_PATH = "C:\\Program Files\\chromedriver.exe";
    public static final String EMAIL = "admin@phptravels.com";
    public static final String WRONG_EMAIL = "a@phptravels.com";
    public static final String PASSWORD = "demoadmin";
    public static final String WRONG_PASS = "demo";
    public static final String URL = "https://www.phptravels.net/admin";
    public static final String[] CREDENTIALS = {"First", "Last" , "email@gmail.com", "pass123", "0527777777" , "address1", "address2", "Australia", "Enabled", "true"};
    public static boolean[] TESTS = new boolean[6];
    public static final String[] TESTS_NAMES = {"","Successful login functionality",
                                                "Login fail test",
                                                "Add user",
                                                "Delete user",
                                                "Logout"};

    public static void main(String[] args) throws Exception {

        int count = 1;

        // **Successful login functionality**
        login(true, count);
        Thread.sleep(10000);
        check(count++);
        driver.close();

        // **Login fail test**
        login(false, count);
        sleep(10000);
        check(count++);
        if(TESTS[count - 1] == false)
            TESTS[count - 1] = true;
        driver.close();

        // **Add customer**
        login(true, count);
        sleep(10000);
        driver.manage().window().maximize();

        //go to ACCOUNTS -> CUSTOMERS
        System.out.println("navigating to ACCOUNTS -> CUSTOMERS");
        By css = By.cssSelector("a[href='https://www.phptravels.net/admin/accounts/customers/']");
        WebElement element = driver.findElement(css);
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();" , element);

        //add
        System.out.println("adding new customer");
        driver.findElement(By.xpath("//button[@type='submit']")).click();

        //inserting credentials
        System.out.println("inserting credentials");
        //inserting full name
        driver.findElement(By.name("fname")).sendKeys(CREDENTIALS[0]);
        driver.findElement(By.name("lname")).sendKeys(CREDENTIALS[1]);

        //inserting email
        WebElement text= driver.findElement(By.name("email"));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        jsExecutor.executeScript("arguments[0].value='"+ CREDENTIALS[2] +"';", text);

        //inserting password
        driver.findElement(By.name("password")).sendKeys(CREDENTIALS[3]);

        //inserting mobile
        driver.findElement(By.name("mobile")).sendKeys(CREDENTIALS[4]);

        //inserting address1
        driver.findElement(By.name("address1")).sendKeys(CREDENTIALS[5]);

        //inserting address2
        driver.findElement(By.name("address2")).sendKeys(CREDENTIALS[6]);

        //selecting country
        Select dropdown = new Select(driver.findElement(By.name("country")));
        dropdown.selectByVisibleText("Australia");

        //selecting status
        dropdown = new Select(driver.findElement(By.name("status")));
        dropdown.selectByVisibleText(CREDENTIALS[8]);

        //checkbox
        if ( !driver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div[2]/div/div[13]/div/div/label/div/ins")).isSelected() )
            driver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div[2]/div/div[13]/div/div/label/div/ins")).click();

        //submit
        WebElement submit = driver.findElement(By.xpath("//*[@id=\"content\"]/form/div/div[3]/button"));
        submit.click();

        // validate customer's details- by first name, lase name, email
        System.out.println("validating customer's details");
        String first = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[3]")).getText();
        String last = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[4]")).getText();
        String email = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[5]/a")).getText();
        //updating test's status
        TESTS[count++] = first.equals(CREDENTIALS[0]) & last.equals(CREDENTIALS[1]) & email.equals(CREDENTIALS[2]);

        // **Delete customer**
        System.out.println("deleting customer");
        if ( !driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[1]/div/ins")).isSelected() )
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[1]/div/ins")).click();
        }
        driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[1]/a")).click();
        driver.switchTo().alert().accept();

        //validate deletion- by email
        sleep(10000);
        email = driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[2]/div/div/div[1]/div[2]/table/tbody/tr[1]/td[5]/a")).getText();
        TESTS[count++] = !email.equals(CREDENTIALS[2]);

        //Logout
        System.out.println(" *** Testing: " + TESTS_NAMES[count] + " ***");
        css = By.cssSelector("a[href='https://www.phptravels.net/admin/logout']");
        element = driver.findElement(css);
        ((JavascriptExecutor)driver).executeScript("arguments[0].click();" , element);
        //updating test's status
        sleep(5000);
        TESTS[count] = (driver.getTitle()).equals("Administator Login");
        driver.close();
        driver.quit();

        //results
        String info="";
        for(int num = 1; num < TESTS.length; num++)
        {
            if(TESTS[num] == true)
                info+= "Test: " + TESTS_NAMES[num] + " - succeeded" + "\n";
            else
                info+="Test: " + TESTS_NAMES[num] + " - failed" + "\n";
        }
        infoBox(info);
    }

    // receives a type- false if the test should use the wrong credentials and true otherwise.
    // login with the credentials according to the test's type.
    public static void login(boolean type, int testNum)
    {
        System.out.println(" *** Testing: " + TESTS_NAMES[testNum] + " ***");
        System.setProperty("webdriver.chrome.driver",DRIVER_PATH);
        driver = new ChromeDriver();
        driver.get(URL);
        System.out.println("inserting credentials");
        //entering the email
        WebElement text= driver.findElement(By.name("email"));
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
        String email = ((type == true) ? EMAIL : WRONG_EMAIL);
        jsExecutor.executeScript("arguments[0].value='"+ email +"';", text);
        //entering the password
        String password = ((type == true) ? PASSWORD : WRONG_PASS);
        driver.findElement(By.name("password")).sendKeys(password);
        //login
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    // receives a test number and sets its cell in TESTS array to true if the test succeeded and false otherwise.
    public static void check(int testNum)
    {
        System.out.println("checking tests results");
        if(driver.getPageSource().contains("Invalid Login Credentials"))
            TESTS[testNum] = false;
        else if(driver.getPageSource().contains("The Email field must contain a valid email address"))
            TESTS[testNum] = false;
        else
            TESTS[testNum] = true;
    }

    public static void infoBox(String infoMessage)
    {
        JOptionPane.showMessageDialog( null, infoMessage );
        System.exit(0);
    }
}
