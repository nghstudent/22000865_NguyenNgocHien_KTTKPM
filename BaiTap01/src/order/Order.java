package order;

public class Order {
    private OrderState currentState;

    public Order() {
        this.currentState = new NewState();
    }

    public void setState(OrderState state) {
        this.currentState = state;
    }

    public void process() {
        currentState.processOrder(this);
    }
}