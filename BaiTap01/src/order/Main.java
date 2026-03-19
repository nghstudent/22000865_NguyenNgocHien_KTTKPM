package order;

public class Main {
    public static void main(String[] args) {
        System.out.println("--- Kịch bản 1: Đơn hàng suôn sẻ ---");
        Order order1 = new Order();
        order1.process(); // Mới tạo -> Đang xử lý
        order1.process(); // Đang xử lý -> Đã giao
        order1.process(); // Đã giao (không làm gì thêm)

        System.out.println("\n--- Kịch bản 2: Đơn hàng bị hủy giữa chừng ---");
        Order order2 = new Order();
        order2.process(); // Mới tạo -> Đang xử lý

        // Khách hàng đổi ý, set trạng thái Hủy
        order2.setState(new CancelledState());
        order2.process(); // Hủy và hoàn tiền
    }
}