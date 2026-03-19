package payment;

public abstract class PaymentDecorator implements PaymentStrategy {
    protected PaymentStrategy wrappedPayment;

    public PaymentDecorator(PaymentStrategy wrappedPayment) {
        this.wrappedPayment = wrappedPayment;
    }

    @Override
    public void pay(double amount) {
        wrappedPayment.pay(amount);
    }
}