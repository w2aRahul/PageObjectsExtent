package by.stest.base;

import by.stest.crm.accounts.AccountsPage;
import org.openqa.selenium.WebDriver;

public class TopMenu {

    WebDriver driver;

    public TopMenu(WebDriver driver) {
        this.driver = driver;
    }

    public void goToHome() {

    }

    public void goToLeads() {

    }

    public void goToContacts() {

    }

    public AccountsPage goToAccounts() {
        Page.click("accountsLnk_XPATH");
        return new AccountsPage();
    }

    public void goToDeals() {

    }

    public void openProfile() {

    }
}
