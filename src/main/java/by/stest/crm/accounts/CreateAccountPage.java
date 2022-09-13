package by.stest.crm.accounts;

import by.stest.base.Page;

public class CreateAccountPage extends Page {

    public void typeAccountName(String accountName) {
        type("accountNameFld_ID", accountName);
    }
}
