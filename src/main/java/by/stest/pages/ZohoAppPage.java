package by.stest.pages;

import by.stest.base.Page;
import by.stest.crm.CRMHomePage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ZohoAppPage extends Page {

    public CRMHomePage goToCRM() {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='CRM']")));
        click("crmLnk_XPATH");
        return new CRMHomePage();
    }

    public void goToSheet() {
        driver.findElement(By.xpath("//div[text()='Sheet']")).click();
    }

    public void goToWriter() {
        driver.findElement(By.xpath("//div[text()='Writer']")).click();
    }
}
