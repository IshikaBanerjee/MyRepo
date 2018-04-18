package com.community.querybuilder.sql;

/**
 * Created by ishbaner1 on 4/4/18.
 */
public interface CustomerService {
    public int injestCustData(String firstName, String lastName, String address, String desc);

    /*
     * Retrieves customer data from the Customer table and returns all customer
     *The filter argument specifies one of the following values:
     *
     *Customer - retrieves all customer data
     *Active Customer- retrieves current customers
     *Past Customer - retrieves old customers no longer current customers
     */
    public String getCustomerData(String filter);
}
