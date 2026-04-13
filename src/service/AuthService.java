package service;

import connection.Customer;

public class AuthService {
    private final Customer customerDAO = new Customer();
    private model.Customer loggedInCustomer = null;


    public model.Customer login(String username, String password) {
        model.Customer customer = customerDAO.login(username, password);

        if (customer != null) {
            this.loggedInCustomer = customer;
            System.out.println("Успешен вход! Добре дошли, " + customer.getFirstName() + "!");
            return customer;
        } else {
            System.out.println("Грешно потребителско име или парола.");
            return null;
        }
    }


    public boolean deleteAccount() {
        if (loggedInCustomer != null) {
            boolean success = customerDAO.deleteCustomer(loggedInCustomer.getId());
            if (success) {
                loggedInCustomer = null;
                return true;
            }
        }
        return false;
    }

    public boolean register(String fName, String lName, String user, String pass, String email, String phone, String address) {
        model.Customer c = new model.Customer();
        c.setFirstName(fName);
        c.setLastName(lName);
        c.setUsername(user);
        c.setPassword(pass);
        c.setEmail(email);
        c.setPhone(phone);
        c.setAddress(address);
        return customerDAO.register(c);
    }

    public void logout() {
        this.loggedInCustomer = null;
        System.out.println("Излязохте от системата.");
    }

    public model.Customer getLoggedInCustomer() {
        return loggedInCustomer;
    }
}