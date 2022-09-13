package by.stest.pages;

import by.stest.base.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends Page {

    public LoginPage goToLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".zh-login")));
        click("signInLnk_CSS");
        return new LoginPage();
    }

    public SignUpPage goToSignUp() {
        wait.until(ExpectedConditions.elementToBeClickable(By.className("zh-signup")));
        click("signUpLnk_CSS");
        return new SignUpPage();
    }
}
