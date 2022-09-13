package by.stest.testcases;

import by.stest.base.Page;
import by.stest.crm.accounts.AccountsPage;
import by.stest.crm.accounts.CreateAccountPage;
import by.stest.pages.ZohoAppPage;
import by.stest.utilities.TestUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Hashtable;

public class CreateAccountTest extends BaseTest {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void createAccountTest(Hashtable<String, String> data) {
        ZohoAppPage zap = new ZohoAppPage();
        zap.goToCRM();
        AccountsPage ap = Page.topMenu.goToAccounts();
        CreateAccountPage cap = ap.goToCreateAccount();
        cap.typeAccountName(data.get("accountName"));
        Assert.fail();
    }
}
