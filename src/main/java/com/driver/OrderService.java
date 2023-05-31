package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public void addOrder(Order order) {
        orderRepository.addOrder(order);
    }

    public void addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        orderRepository.addPartner(partner);
    }

    public void addOrderPartnerPair(String orderId, String partnerId) throws RuntimeException{
        Optional<Order> orderOptional = orderRepository.getOrderById(orderId);
        Optional<DeliveryPartner> partnerOptional = orderRepository.getPartnerById(partnerId);

        if(orderOptional.isEmpty()){
            throw new RuntimeException("Order id not present : "+orderId);
        }
        if(partnerOptional.isEmpty()){
            throw new RuntimeException("Partner id not present : "+partnerId);
        }

        DeliveryPartner partner = partnerOptional.get();
        partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
        orderRepository.addPartner(partner);
        orderRepository.addOrderPartnerPair(orderId, partnerId);
    }

    public Order getOrderById(String orderId)throws RuntimeException{
        Optional<Order> orderOptional = orderRepository.getOrderById(orderId);

        if(orderOptional.isEmpty()){
            throw new RuntimeException("Order not found with id : "+orderId);
        }

        return orderOptional.get();
    }

    public DeliveryPartner getPartnerById(String partnerId) throws RuntimeException{
        Optional<DeliveryPartner> partnerOptional = orderRepository.getPartnerById(partnerId);
        if(partnerOptional.isEmpty()){
            throw new RuntimeException("Partner not found with id : "+partnerId);
        }

        return partnerOptional.get();
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        Optional<DeliveryPartner> partnerOptional = orderRepository.getPartnerById(partnerId);
        if(partnerOptional.isEmpty()){
            return 0;
        }
        return partnerOptional.get().getNumberOfOrders();
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = orderRepository.getOrdersByPartnerId(partnerId);
        return orders;
    }

    public List<String> getAllOrders() {
        List<String> orders = orderRepository.getAllOrders();
        return orders;
    }

    public Integer getCountOfUnassignedOrders() {
        return orderRepository.getAllOrders().size() - orderRepository.assignedOrders().size();
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        List<String> ordersIds = orderRepository.getOrdersByPartnerId(partnerId);
        List<Order> orders = new ArrayList<>();

        for(String orderId : ordersIds){
            Order order = orderRepository.getOrderById(orderId).get();
            if(order.getDeliveryTime() > TimeUtils.convertDeliveryTime(time)){
                orders.add(order);
            }
        }

        return orders.size();
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        int max = 0;

        for(String orderId : orderIds){
            Order order = orderRepository.getOrderById(orderId).get();
            if(max < order.getDeliveryTime()){
                max = order.getDeliveryTime();
            }
        }

        return TimeUtils.convertDeliveryTime(max);
    }

    public void deletePartnerId(String partnerId) {
        List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
        orderRepository.deletePartnerId(partnerId);

        for(String id : orderIds){
            orderRepository.unassignedOrders(id);
        }
    }

    public void deleteOrderById(String orderId) {
        String partnerId = orderRepository.getPartnerIdByOrderId(orderId);
        orderRepository.deleteOrderFromOrders(orderId);

        if(Objects.nonNull(partnerId)) {
            List<String> orderIds = orderRepository.getOrdersByPartnerId(partnerId);
            orderIds.remove(orderId);
        }
    }
}
