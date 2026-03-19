package order;

public class DeliveredState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Trạng thái [Đã giao]: Cập nhật trạng thái đơn hàng là đã giao. Hoàn tất!");
    }
}