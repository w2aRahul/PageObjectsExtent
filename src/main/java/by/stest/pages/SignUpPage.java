package by.stest.pages;

import by.stest.base.Page;

public class SignUpPage extends Page {

    public HomePage navigateBack() {
        driver.navigate().back();
        return new HomePage();
    }
}
