package order;

public class CancelledState implements OrderState {
    @Override
    public void processOrder(Order order) {
        System.out.println("Trạng thái [Hủy]: Đã hủy đơn hàng và đang tiến hành hoàn tiền.");
    }
}