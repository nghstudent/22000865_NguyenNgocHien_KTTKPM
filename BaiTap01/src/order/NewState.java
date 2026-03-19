package order;

public class NewState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Trạng thái [Mới tạo]: Đang kiểm tra thông tin đơn hàng...");
        order.setState(new ProcessingState());
    }
}