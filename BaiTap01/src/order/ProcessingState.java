package order;

public class ProcessingState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Trạng thái [Đang xử lý]: Đang đóng gói và vận chuyển...");
        order.setState(new DeliveredState());
    }
}