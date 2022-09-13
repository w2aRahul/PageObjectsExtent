package by.stest.pages;

import by.stest.base.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends Page {

    public LoginPage enterEmailAddress(String emailAddress) {
        type("emailFld_ID", emailAddress);
        return new LoginPage();
    }

    public LoginPage clickNextButton() {
        click("nextBtn_ID");
        return new LoginPage();
    }

    public LoginPage enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("password")));
        type("passwdFld_ID", password);
        return new LoginPage();
    }

    public ZohoAppPage clickSignInButton() {
        click("signInBtn_XPATH");
        return new ZohoAppPage();
    }

    public HomePage navigateBack() {
        driver.navigate().back();
        return new HomePage();
    }
}
