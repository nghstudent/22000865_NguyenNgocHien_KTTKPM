package payment;

public class PayPalPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("=> Thực hiện thanh toán " + amount + " VNĐ qua hệ thống PayPal. Thành công!");
    }
}