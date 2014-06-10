package org.umlg.bankfrauddetection;

import org.umlg.*;
import org.umlg.runtime.adaptor.DefaultDataCreator;
import org.umlg.runtime.adaptor.UMLG;

/**
 * Date: 2014/06/07
 * Time: 7:00 PM
 */
public class BankFraudDetectionDefaultData implements DefaultDataCreator {
    @Override
    public void createData() {
        if (AccountHolder.allInstances().isEmpty()) {

            // Create account holders
            AccountHolder accountHolder1 = new AccountHolder();
            accountHolder1.setFirstName("John");
            accountHolder1.setLastName("Doe");
            accountHolder1.setUniqueId("JohnDoe");

            AccountHolder accountHolder2 = new AccountHolder();
            accountHolder2.setFirstName("Jane");
            accountHolder2.setLastName("Appleseed");
            accountHolder2.setUniqueId("JaneAppleseed");

            AccountHolder accountHolder3 = new AccountHolder();
            accountHolder3.setFirstName("Matt");
            accountHolder3.setLastName("Smith");
            accountHolder3.setUniqueId("MattSmith");

            // Create Address
            Address address = new Address();
            address.setStreet("123 NW 1st Street");
            address.setCity("San Francisco");
            address.setState("California");
            address.setZipCode("94101");

            // Connect 3 account holders to 1 address
            accountHolder1.addToAddress(address);
            accountHolder2.addToAddress(address);
            accountHolder3.addToAddress(address);

            // Create Phone Number
            PhoneNumber phoneNumber1 = new PhoneNumber();
            phoneNumber1.setPhoneNumber("555-555-5555");

            // Connect 2 account holders to 1 phone number
            accountHolder1.addToPhoneNumber(phoneNumber1);
            accountHolder2.addToPhoneNumber(phoneNumber1);

            // Create SSN
            SSN ssn1 = new SSN();
            ssn1.setSSN("241-23-1234");

            // Connect 2 account holders to 1 SSN
            accountHolder2.addToSSN(ssn1);
            accountHolder3.addToSSN(ssn1);

            // Create SSN and connect 1 account holder
            SSN ssn2 = new SSN();
            ssn2.setSSN("241-23-4567");
            accountHolder1.addToSSN(ssn2);

            // Create Credit Card and connect 1 account holder
            CreditCard creditCard1 = new CreditCard();
            creditCard1.setAccountNumber("1234567890123456");
            creditCard1.setLimit(5000D);
            creditCard1.setBalance(1442.23D);
            creditCard1.setExpirationDate("01-20");
            creditCard1.setSecurityCode("123");
            accountHolder1.addToCreditCard(creditCard1);

            // Create Bank Account and connect 1 account holder
            BankAccount bankAccount1 = new BankAccount();
            bankAccount1.setAccountNumber("2345678901234567");
            bankAccount1.setBalance(7054.43D);
            accountHolder1.addToBankAccount(bankAccount1);

            // Create Credit Card and connect 1 account holder
            CreditCard creditCard2 = new CreditCard();
            creditCard2.setAccountNumber("1234567890123456");
            creditCard2.setLimit(4000D);
            creditCard2.setBalance(2345.56D);
            creditCard2.setExpirationDate("02-20");
            creditCard2.setSecurityCode("456");
            accountHolder2.addToCreditCard(creditCard2);

            // Create Bank Account and connect 1 account holder
            BankAccount bankAccount2 = new BankAccount();
            bankAccount2.setAccountNumber("3456789012345678");
            bankAccount2.setBalance(4231.12D);
            accountHolder2.addToBankAccount(bankAccount2);

            // Create Unsecured Loan and connect 1 account holder
            UnsecuredLoan unsecuredLoan1 = new UnsecuredLoan();
            unsecuredLoan1.setAccountNumber("4567890123456789-0");
            unsecuredLoan1.setBalance(9045.53D);
            unsecuredLoan1.setAPR(.0541D);
            unsecuredLoan1.setLoanAmount(12000.00D);
            accountHolder2.addToUnsecuredLoan(unsecuredLoan1);

            // Create Bank Account and connect 1 account holder
            BankAccount bankAccount3  = new BankAccount();
            bankAccount3.setAccountNumber("4567890123456789");
            bankAccount3.setBalance(12345.45D);
            accountHolder3.addToBankAccount(bankAccount3);

            // Create Unsecured Loan and connect 1 account holder
            UnsecuredLoan unsecuredLoan2 = new UnsecuredLoan();
            unsecuredLoan2.setAccountNumber("5678901234567890-0");
            unsecuredLoan2.setBalance(16341.95D);
            unsecuredLoan2.setAPR(.0341D);
            unsecuredLoan2.setLoanAmount(22000.00D);
            accountHolder3.addToUnsecuredLoan(unsecuredLoan2);

            // Create Phone Number and connect 1 account holder
            PhoneNumber phoneNumber2 = new PhoneNumber();
            phoneNumber2.setPhoneNumber("555-555-1234");
            accountHolder3.addToPhoneNumber(phoneNumber2);

            UMLG.get().commit();
        }
    }
}
