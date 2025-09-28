import java.util.*;

// ===== Creational Pattern 1: Singleton =====
class SingletonLogger {
    private static SingletonLogger instance;
    private SingletonLogger() {}
    public static SingletonLogger getInstance() {
        if (instance == null) instance = new SingletonLogger();
        return instance;
    }
    public void log(String msg) {
        System.out.println("[LOG] " + msg);
    }
}

// ===== Creational Pattern 2: Factory =====
interface Shape { void draw(); }
class Circle implements Shape {
    public void draw() { System.out.println("Drawing Circle"); }
}
class Square implements Shape {
    public void draw() { System.out.println("Drawing Square"); }
}
class ShapeFactory {
    public static Shape getShape(String type) {
        if (type.equalsIgnoreCase("circle")) return new Circle();
        if (type.equalsIgnoreCase("square")) return new Square();
        return null;
    }
}

// ===== Structural Pattern 1: Adapter =====
interface USBPort { void connectWithUsbCable(); }
class OldCharger {
    public void connectWithTwoPin() { System.out.println("Connected with 2-pin charger."); }
}
class ChargerAdapter implements USBPort {
    private OldCharger oldCharger;
    public ChargerAdapter(OldCharger charger) { this.oldCharger = charger; }
    public void connectWithUsbCable() { oldCharger.connectWithTwoPin(); }
}

// ===== Structural Pattern 2: Decorator =====
interface Coffee { String getDescription(); double cost(); }
class BasicCoffee implements Coffee {
    public String getDescription() { return "Basic Coffee"; }
    public double cost() { return 5; }
}
class MilkDecorator implements Coffee {
    private Coffee coffee;
    public MilkDecorator(Coffee coffee) { this.coffee = coffee; }
    public String getDescription() { return coffee.getDescription() + ", Milk"; }
    public double cost() { return coffee.cost() + 2; }
}

// ===== Behavioral Pattern 1: Observer =====
interface Observer {
    void update(String message);
}
class ConcreteObserver implements Observer {
    private String name;
    public ConcreteObserver(String name) { this.name = name; }
    public void update(String message) {
        System.out.println(name + " received: " + message);
    }
}
class Subject {
    private List<Observer> observers = new ArrayList<>();
    public void addObserver(Observer o) { observers.add(o); }
    public void notifyAllObservers(String msg) {
        for (Observer o : observers) o.update(msg);
    }
}

// ===== Behavioral Pattern 2: Strategy =====
interface PaymentStrategy { void pay(int amount); }
class CreditCardPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using Credit Card."); }
}
class UpiPayment implements PaymentStrategy {
    public void pay(int amount) { System.out.println("Paid " + amount + " using UPI."); }
}
class PaymentContext {
    private PaymentStrategy strategy;
    public void setStrategy(PaymentStrategy strategy) { this.strategy = strategy; }
    public void executePayment(int amount) { strategy.pay(amount); }
}

// ===== Main Menu to Demo Patterns =====
public class DesignPatternsDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Design Patterns Demo ---");
            System.out.println("1. Singleton");
            System.out.println("2. Factory");
            System.out.println("3. Adapter");
            System.out.println("4. Decorator");
            System.out.println("5. Observer");
            System.out.println("6. Strategy");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            int ch = sc.nextInt(); sc.nextLine();

            switch (ch) {
                case 1:
                    SingletonLogger.getInstance().log("Singleton works!");
                    break;
                case 2:
                    Shape s = ShapeFactory.getShape("circle");
                    s.draw();
                    break;
                case 3:
                    USBPort adapter = new ChargerAdapter(new OldCharger());
                    adapter.connectWithUsbCable();
                    break;
                case 4:
                    Coffee coffee = new MilkDecorator(new BasicCoffee());
                    System.out.println(coffee.getDescription() + " costs $" + coffee.cost());
                    break;
                case 5:
                    Subject subject = new Subject();
                    subject.addObserver(new ConcreteObserver("Alice"));
                    subject.addObserver(new ConcreteObserver("Bob"));
                    subject.notifyAllObservers("Pattern event triggered!");
                    break;
                case 6:
                    PaymentContext context = new PaymentContext();
                    context.setStrategy(new UpiPayment());
                    context.executePayment(500);
                    break;
                case 0:
                    System.out.println("Exiting demo...");
                    return;
                default:
                    System.out.println("Invalid choice");
            }
        }
    }
}
