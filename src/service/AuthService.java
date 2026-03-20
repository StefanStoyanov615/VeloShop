package service;

import dao.CustomerDAO;
import model.Customer;

public class AuthService {
    private CustomerDAO customerDAO = new CustomerDAO();
    private Customer loggedInCustomer = null;

    
    public Customer login(String username, String password) {
        Customer customer = customerDAO.login(username, password);

        if (customer != null) {
            this.loggedInCustomer = customer;
            System.out.println("Успешен вход! Добре дошли, " + customer.getFirstName() + "!");
            return customer;
        } else {
            System.out.println("Грешно потребителско име или парола.");
            return null;
        }
    }

    
    public boolean register(String fName, String lName, String user, String pass, String email) {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(fName);
        newCustomer.setLastName(lName);
        newCustomer.setUsername(user);
        newCustomer.setPassword(pass);
        newCustomer.setEmail(email);

        boolean success = customerDAO.register(newCustomer);
        if (success) {
            System.out.println("Регистрацията беше успешна! Вече можете да влезете.");
        } else {
            System.out.println("Грешка: Потребителското име или имейлът вече съществуват.");
        }
        return success;
    }

    public void logout() {
        this.loggedInCustomer = null;
        System.out.println("Излязохте от системата.");
    }

    public Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }
}