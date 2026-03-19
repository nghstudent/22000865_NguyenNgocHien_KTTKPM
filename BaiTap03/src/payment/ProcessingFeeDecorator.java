package payment;

public class ProcessingFeeDecorator extends PaymentDecorator {
    private double feePercentage;

    public ProcessingFeeDecorator(PaymentStrategy wrappedPayment, double feePercentage) {
        super(wrappedPayment);
        this.feePercentage = feePercentage;
    }

    @Override
    public void pay(double amount) {
        double fee = amount * feePercentage;
        System.out.println(" [!] Phụ thu phí xử lý (" + (feePercentage * 100) + "%): +" + fee + " VNĐ");
        super.pay(amount + fee);
    }
}