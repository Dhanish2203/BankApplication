package BankProject;

import java.util.HashMap;
import java.util.Map;

public class Customer {
    private String custName;
    private String password;

    public Customer(String custName, String password) {
        this.custName = custName;
        this.password = password;
    }

    public String getCustName() {
        return custName;
    }

    public String getPassword() {
        return password;
    }
}

