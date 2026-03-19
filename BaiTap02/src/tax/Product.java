package tax;

public class Product {
    private String name;
    private double price;
    private TaxStrategy taxStrategy; // Lưu trữ chiến lược thuế hiện tại

    public Product(String name, double price, TaxStrategy taxStrategy) {
        this.name = name;
        this.price = price;
        this.taxStrategy = taxStrategy;
    }

    // Cho phép thay đổi loại thuế lúc runtime nếu cần
    public void setTaxStrategy(TaxStrategy taxStrategy) {
        this.taxStrategy = taxStrategy;
    }

    public double getTaxAmount() {
        return taxStrategy.calculateTax(price);
    }

    public double getTotalPrice() {
        return price + getTaxAmount();
    }

    public void printInfo() {
        System.out.println("Sản phẩm: " + name);
        System.out.printf(" - Giá gốc: %,.0f VNĐ\n", price);
        System.out.printf(" - Tiền thuế: %,.0f VNĐ\n", getTaxAmount());
        System.out.printf(" - Tổng thanh toán: %,.0f VNĐ\n\n", getTotalPrice());
    }
}