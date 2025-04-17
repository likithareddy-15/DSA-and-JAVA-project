import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

// User class
class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}

// Car class
class Car {
    private String model;
    private String number;
    private boolean isAvailable;
    private double hourlyRate;

    public Car(String model, String number, double hourlyRate) {
        this.model = model;
        this.number = number;
        this.hourlyRate = hourlyRate;
        this.isAvailable = true;
    }

    public String getModel() {
        return model;
    }

    public String getNumber() {
        return number;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void bookCar() {
        isAvailable = false;
    }

    public void returnCar() {
        isAvailable = true;
    }
}

// Booking class
class Booking {
    private String username;
    private String name;
    private String phone;
    private Car car;
    private Date startTime;
    private Date endTime;
    private double cost;

    public Booking(String username, String name, String phone, Car car, Date startTime, Date endTime) {
        this.username = username;
        this.name = name;
        this.phone = phone;
        this.car = car;
        this.startTime = startTime;
        this.endTime = endTime;
        this.cost = calculateCost();
    }

    private double calculateCost() {
        long duration = endTime.getTime() - startTime.getTime();
        long hours = Math.max(1, TimeUnit.MILLISECONDS.toHours(duration));
        return hours * car.getHourlyRate();
    }

    public String getBookingDetails() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return "User: " + username + " | Name: " + name + " | Phone: " + phone +
                "\nCar: " + car.getModel() + " ( " + car.getNumber() + " )\n" +
                "Start Time: " + sdf.format(startTime) + " | End Time: " + sdf.format(endTime) + "\n" +
                "Total Cost: $" + cost;
    }
}

// Main Application GUI
public class CarRentalSystem extends JFrame {
    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Car> cars = new ArrayList<>();
    private ArrayList<Booking> bookings = new ArrayList<>();
    private User loggedInUser = null;

    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

    public CarRentalSystem() {
        setTitle("Car Rental System");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeCars();
        initializeGUI();
    }

    private void initializeCars() {
        cars.add(new Car("Toyota Camry", "TN-1234", 10));
        cars.add(new Car("Honda Civic", "KA-5678", 12));
        cars.add(new Car("Ford Mustang", "MH-4321", 20));
        cars.add(new Car("Tesla Model S", "DL-9876", 25));
        cars.add(new Car("BMW X5", "WB-6543", 30));
        cars.add(new Car("Audi A6", "GJ-2233", 28));
        cars.add(new Car("Hyundai Creta", "KL-1212", 15));
        cars.add(new Car("Maruti Swift", "RJ-7878", 9));
        cars.add(new Car("Nissan Altima", "UP-0909", 13));
        cars.add(new Car("Kia Seltos", "BR-4545", 16));
        cars.add(new Car("Mahindra Thar", "HP-3030", 18));
        cars.add(new Car("Tata Nexon", "CH-6666", 14));
        cars.add(new Car("Volkswagen Polo", "AS-1122", 11));
        cars.add(new Car("Skoda Octavia", "OR-7788", 22));
        cars.add(new Car("Jeep Compass", "CG-9999", 26));
        cars.add(new Car("MG Hector", "PB-1230", 19));
        cars.add(new Car("Renault Duster", "JK-0011", 17));
        cars.add(new Car("Toyota Fortuner", "AP-2323", 27));
        cars.add(new Car("Honda City", "GA-7870", 14));
        cars.add(new Car("Suzuki Baleno", "TS-6060", 12));
    }

    private void initializeGUI() {
        // Login Panel
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton signupButton = new JButton("Sign Up");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(signupButton);

        mainPanel.add(loginPanel, "Login");

        // Dashboard Panel
        JPanel dashboardPanel = new JPanel(new GridLayout(4, 1));
        JButton viewCarsButton = new JButton("View Available Cars");
        JButton bookCarButton = new JButton("Book a Car");
        JButton viewBookingsButton = new JButton("View Bookings");
        JButton logoutButton = new JButton("Logout");

        dashboardPanel.add(viewCarsButton);
        dashboardPanel.add(bookCarButton);
        dashboardPanel.add(viewBookingsButton);
        dashboardPanel.add(logoutButton);

        mainPanel.add(dashboardPanel, "Dashboard");

        // Action Listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            for (User user : users) {
                if (user.getUsername().equals(username) && user.checkPassword(password)) {
                    loggedInUser = user;
                    cardLayout.show(mainPanel, "Dashboard");
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "Invalid credentials");
        });

        signupButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            users.add(new User(username, password));
            JOptionPane.showMessageDialog(this, "Sign up successful!");
        });

        viewCarsButton.addActionListener(e -> showAvailableCars());
        bookCarButton.addActionListener(e -> bookCar());
        viewBookingsButton.addActionListener(e -> showBookings());
        logoutButton.addActionListener(e -> {
            loggedInUser = null;
            cardLayout.show(mainPanel, "Login");
        });

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    private void showAvailableCars() {
        StringBuilder availableCars = new StringBuilder("Available Cars:\n");
        for (Car car : cars) {
            if (car.isAvailable()) {
                availableCars.append(car.getModel()).append(" ( ").append(car.getNumber()).append(" ) - $").append(car.getHourlyRate()).append(" per hour\n");
            }
        }
        JOptionPane.showMessageDialog(this, availableCars.toString());
    }

    private void bookCar() {
        if (loggedInUser == null) {
            JOptionPane.showMessageDialog(this, "Please log in to book a car.");
            return;
        }

        String[] availableCars = cars.stream()
                .filter(Car::isAvailable)
                .map(car -> car.getModel() + " (" + car.getNumber() + ")")
                .toArray(String[]::new);

        if (availableCars.length == 0) {
            JOptionPane.showMessageDialog(this, "No cars are currently available.");
            return;
        }

        String selectedCarStr = (String) JOptionPane.showInputDialog(
                this,
                "Select a car:",
                "Book a Car",
                JOptionPane.PLAIN_MESSAGE,
                null,
                availableCars,
                availableCars[0]);

        if (selectedCarStr == null) return;

        String name = JOptionPane.showInputDialog(this, "Enter your full name:");
        if (name == null || name.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.");
            return;
        }

        String phone = JOptionPane.showInputDialog(this, "Enter your phone number:");
        if (phone == null || phone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phone number is required.");
            return;
        }

        String[] carDetails = selectedCarStr.split("\\(");
        String selectedCarNumber = carDetails[1].substring(0, carDetails[1].length() - 1).trim();

        Car selectedCar = null;
        for (Car car : cars) {
            if (car.getNumber().equals(selectedCarNumber)) {
                selectedCar = car;
                break;
            }
        }

        if (selectedCar == null) {
            JOptionPane.showMessageDialog(this, "Invalid car selection.");
            return;
        }

        Date startTime = new Date();
        Date endTime = new Date(startTime.getTime() + TimeUnit.HOURS.toMillis(1));

        Booking booking = new Booking(loggedInUser.getUsername(), name, phone, selectedCar, startTime, endTime);
        bookings.add(booking);
        selectedCar.bookCar();

        JOptionPane.showMessageDialog(this, "Car booked successfully!\n" + booking.getBookingDetails());
    }

    private void showBookings() {
        StringBuilder bookingDetails = new StringBuilder("Bookings:\n");
        if (bookings.isEmpty()) {
            bookingDetails.append("No bookings found.");
        } else {
            for (Booking booking : bookings) {
                bookingDetails.append(booking.getBookingDetails()).append("\n\n");
            }
        }
        JOptionPane.showMessageDialog(this, bookingDetails.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CarRentalSystem().setVisible(true));
    }
}
