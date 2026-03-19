package tax;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- HỆ THỐNG TÍNH THUẾ SẢN PHẨM ---\n");

        // Sản phẩm 1: Nước ngọt - Áp dụng thuế tiêu thụ (5%)
        Product soda = new Product("Nước ngọt có ga", 10000, new ConsumptionTax());
        soda.printInfo();

        // Sản phẩm 2: Điện thoại - Áp dụng thuế VAT (10%)
        Product phone = new Product("Điện thoại thông minh", 15000000, new VATTax());
        phone.printInfo();

        // Sản phẩm 3: Túi xách hàng hiệu - Áp dụng thuế xa xỉ (20%)
        Product luxuryBag = new Product("Túi xách Gucci", 50000000, new LuxuryTax());
        luxuryBag.printInfo();

        // Thử thay đổi chiến lược thuế lúc runtime
        System.out.println("--- CẬP NHẬT CHÍNH SÁCH THUẾ ---");
        System.out.println("Chính phủ giảm thuế điện thoại xuống mức thuế tiêu thụ (5%)!");
        phone.setTaxStrategy(new ConsumptionTax());
        phone.printInfo();
    }
}