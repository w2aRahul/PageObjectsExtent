package by.stest.rough;

import by.stest.base.Page;
import by.stest.crm.CRMHomePage;
import by.stest.crm.accounts.AccountsPage;
import by.stest.crm.accounts.CreateAccountPage;
import by.stest.pages.HomePage;
import by.stest.pages.LoginPage;
import by.stest.pages.SignUpPage;
import by.stest.pages.ZohoAppPage;

public class RoughTest {

    public static void main(String[] args) {

        HomePage home = new HomePage();
        LoginPage lp = home.goToLogin();
        lp.navigateBack();
        SignUpPage sup = home.goToSignUp();
        sup.navigateBack();
        home.goToLogin();
        lp.enterEmailAddress("stest.siarhei@gmail.com");
        lp.clickNextButton();
        lp.enterPassword("Test123456__");
        ZohoAppPage zap = lp.clickSignInButton();
        CRMHomePage chp = zap.goToCRM();
        AccountsPage ap = Page.topMenu.goToAccounts();
        CreateAccountPage cap = ap.goToCreateAccount();
        cap.typeAccountName("TestAcc");
    }
}
