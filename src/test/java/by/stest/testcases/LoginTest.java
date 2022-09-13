package by.stest.testcases;

import by.stest.pages.HomePage;
import by.stest.pages.LoginPage;
import by.stest.pages.SignUpPage;
import by.stest.utilities.TestUtil;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Hashtable;

public class LoginTest extends BaseTest {

    @Test(dataProviderClass = TestUtil.class, dataProvider = "dp")
    public void loginTest(Hashtable<String, String> data) {
        HomePage home = new HomePage();
        LoginPage lp = home.goToLogin();
        lp.navigateBack();
        SignUpPage sup = home.goToSignUp();
        sup.navigateBack();
        home.goToLogin();
        lp.enterEmailAddress(data.get("userName"));
        lp.clickNextButton();
        lp.enterPassword(data.get("password"));
        lp.clickSignInButton();
    }
}
