package lk.ijse.pos.views.TM;

public class OrderTM {
    private String orderId;
    private String orderDate;
    private String customerId;
    private double TotalCost;

    public OrderTM() {
    }

    public OrderTM(String orderId, String orderDate, String customerId, double totalCost) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customerId = customerId;
        TotalCost = totalCost;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public double getTotalCost() {
        return TotalCost;
    }

    public void setTotalCost(double totalCost) {
        TotalCost = totalCost;
    }
}
