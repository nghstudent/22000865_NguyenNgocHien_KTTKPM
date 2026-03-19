package payment;

public class DiscountDecorator extends PaymentDecorator {
    private double discountAmount;

    public DiscountDecorator(PaymentStrategy wrappedPayment, double discountAmount) {
        super(wrappedPayment);
        this.discountAmount = discountAmount;
    }

    @Override
    public void pay(double amount) {
        System.out.println(" [*] Áp dụng mã giảm giá: -" + discountAmount + " VNĐ");
        double newAmount = amount - discountAmount;
        if (newAmount < 0) newAmount = 0;
        super.pay(newAmount);
    }
}