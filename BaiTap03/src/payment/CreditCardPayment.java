package payment;

public class CreditCardPayment implements PaymentStrategy {
    @Override
    public void pay(double amount) {
        System.out.println("=> Thực hiện thanh toán " + amount + " VNĐ bằng Thẻ tín dụng. Thành công!");
    }
}