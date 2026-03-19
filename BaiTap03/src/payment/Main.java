package payment;

public class Main {
    public static void main(String[] args) {
        double orderAmount = 100000; // Đơn hàng gốc: 100k
        System.out.println("--- ĐƠN HÀNG TRỊ GIÁ: " + orderAmount + " VNĐ ---\n");

        System.out.println("Kịch bản 1: Chỉ thanh toán bằng Thẻ tín dụng (Không phí, không giảm giá)");
        PaymentStrategy basicPayment = new CreditCardPayment();
        basicPayment.pay(orderAmount);

        System.out.println("\nKịch bản 2: Thanh toán PayPal + Phí xử lý 5%");
        PaymentStrategy paypal = new PayPalPayment();
        PaymentStrategy paypalWithFee = new ProcessingFeeDecorator(paypal, 0.05);
        paypalWithFee.pay(orderAmount);

        System.out.println("\nKịch bản 3: Thanh toán Thẻ tín dụng + Giảm 20k + Phí xử lý 2%");
        PaymentStrategy creditCard = new CreditCardPayment();
        PaymentStrategy discounted = new DiscountDecorator(creditCard, 20000); // Trừ tiền trước
        PaymentStrategy finalPayment = new ProcessingFeeDecorator(discounted, 0.02); // Tính phí trên số tiền đã trừ

        finalPayment.pay(orderAmount);
    }
}