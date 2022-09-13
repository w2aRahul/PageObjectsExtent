package by.stest.crm.accounts;

import by.stest.base.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AccountsPage extends Page {

    public CreateAccountPage goToCreateAccount() {
        wait.until(ExpectedConditions
                .elementToBeClickable(By.xpath("//button /*[text()='Create an Account']")));
        click("createAccountBtn_XPATH");
        return new CreateAccountPage();
    }

    public void goToImportAccounts() {

    }
}
