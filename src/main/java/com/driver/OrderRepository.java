package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepository {
    private HashMap<String, Order> orderMap = new HashMap<>();
    private HashMap<String, DeliveryPartner> partnerMap = new HashMap<>();
    private HashMap<String, ArrayList<String>> partnerOrderPair = new HashMap<>();
    private HashMap<String, String> orderPartnerMap = new HashMap<>();

    public void addOrder(Order order) {
        orderMap.put(order.getId(), order);
    }

    public void addPartner(DeliveryPartner partner) {
        partnerMap.put(partner.getId(), partner);
    }

    public Optional<Order> getOrderById(String orderId) {
        if(orderMap.containsKey(orderId)){
            return Optional.of(orderMap.get(orderId));
        }
        return Optional.empty();
    }

    public Optional<DeliveryPartner> getPartnerById(String partnerId) {
        if(partnerMap.containsKey(partnerId)){
            return Optional.of(partnerMap.get(partnerId));
        }
        return Optional.empty();
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {
        ArrayList<String> orders = partnerOrderPair.get(partnerId);
        orders.add(orderId);

        partnerOrderPair.put(partnerId, orders);

        orderPartnerMap.put(orderId, partnerId);
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        return partnerOrderPair.getOrDefault(partnerId, new ArrayList<>());
    }

    public List<String> getAllOrders() {
        return new ArrayList<>(orderMap.keySet());
    }

    public List<String> assignedOrders() {
        return new ArrayList<>(orderPartnerMap.keySet());
    }

    public void deletePartnerId(String partnerId) {
        partnerMap.remove(partnerId);
        partnerOrderPair.remove(partnerId);
    }

    public void unassignedOrders(String id) {
        orderPartnerMap.remove(id);
    }

    public void deleteOrderFromOrders(String orderId) {
        orderMap.remove(orderId);
        orderPartnerMap.remove(orderId);
    }

    public String getPartnerIdByOrderId(String orderId) {
        return orderPartnerMap.get(orderId);
    }
}
