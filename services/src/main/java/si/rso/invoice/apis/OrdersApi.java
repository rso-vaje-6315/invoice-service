package si.rso.invoice.apis;


import si.rso.orders.lib.Order;

public interface OrdersApi {
    
    Order getOrder(String orderId);
    
}
