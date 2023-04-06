package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Order;
import com.example.system.model.User;
import com.example.system.model.dto.BookedProduct;
import com.example.system.model.dto.FullOrder;
import com.example.system.model.enums.Status;
import com.example.system.repository.jpa.OrderJpaRepository;
import com.example.system.repository.jpa.ProductJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.system.service.constants.Constants.AN_ORDER_WITH_ID;
import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;

@Service("orderService")
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderJpaRepository orderJpaRepository;
    private ProductJpaRepository productJpaRepository;

    @Override
    public List<Order> getOrders() {
        return orderJpaRepository.getOrders(Sort.by("id"));
    }

    @Override
    public Order getOrder(Integer id) {
        Order order = orderJpaRepository.getOrder(id);
        if (null == order) {
            throw new ResourceNotFoundException(AN_ORDER_WITH_ID + id + DOES_NOT_EXIST);
        } else {
            return order;
        }
    }

    @Override
    @Transactional
    public Order createOrder(Integer userId, FullOrder order) {
        try {
            Integer totalAmount = order.getProducts().stream().mapToInt(BookedProduct::getQuantity).sum();
            if (!allProductsAreAvailable(order)) {
                throw new ResourceNotFoundException("The required products are not available.");
            }

            Integer orderId = orderJpaRepository.createOrder(totalAmount, order.getStatus().name());
            boolean updateOrderDetailsResult = order.getProducts()
                    .stream()
                    .allMatch(product -> updateProductAndOrderDetails(orderId, product, order.getStatus()));
            boolean updateOrdersHistoryResult = orderJpaRepository.updateOrdersHistory(userId, orderId) == 1;

            if (updateOrdersHistoryResult && updateOrderDetailsResult) {
                return getOrder(orderId);
            } else {
                throw new ResourceNotFoundException(AN_ORDER_WITH_ID + orderId + CANNOT_BE_CREATED);
            }
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException(e.getMessage());
        } catch (Exception e) {
            throw new ResourceNotFoundException("The order for user " + userId + CANNOT_BE_CREATED);
        }
    }


    @Override
    public Order deleteOrder(Integer id) {
        if (orderJpaRepository.deleteOrder(id) != 1) {
            throw new ResourceNotFoundException(AN_ORDER_WITH_ID + id + DOES_NOT_EXIST);
        }
        return getOrder(id);
    }

    @Override
    public List<Order> getValidSortedOrdersByUserId(Integer id) {
        return orderJpaRepository.getValidSortedOrdersByUserId(id);
    }

    @Override
    public Integer getSumOfAllOrdersByUserId(Integer id) {
        return orderJpaRepository.getSumOfAllOrdersByUserId(id);
    }

    @Override
    public Map<User, List<FullOrder>> getUsersWithOrders() {
        Map<User, List<FullOrder>> result = new LinkedHashMap<>();
        orderJpaRepository.getUsersWithOrders().forEach(row -> getUsersWithOrders(result, row));
        return result;
    }

    @Override
    public Map<User, List<FullOrder>> getUsersWithOrdersWithStatus(String status) {
        Map<User, List<FullOrder>> result = new LinkedHashMap<>();
        try {
            List<Map<String, Object>> usersWithOrdersWithStatus = orderJpaRepository.getUsersWithOrdersWithStatus(status);
            usersWithOrdersWithStatus.forEach(row -> getUsersWithOrders(result, row));
            return result;
        } catch (Exception e) {
            throw new ResourceNotFoundException("The status " + status + DOES_NOT_EXIST);
        }
    }

    @Override
    public Map<User, List<FullOrder>> getUsersWithOrdersWithProductsFromCategory(String category) {
        Map<User, List<FullOrder>> result = new LinkedHashMap<>();
        try {
            List<Map<String, Object>> usersWithProductsFromCategory = orderJpaRepository.getUsersWithOrdersWithProductsFromCategory(category);
            usersWithProductsFromCategory.forEach(row -> getUsersWithOrders(result, row));
            return result;
        } catch (Exception e) {
            throw new ResourceNotFoundException("The category " + category + DOES_NOT_EXIST);
        }
    }

    private boolean allProductsAreAvailable(FullOrder order) {
        return order.getProducts().stream()
                .allMatch(product ->
                        productJpaRepository.getProductsAvailableQuantity(product.getId()) >= product.getQuantity()
                );
    }

    private boolean updateProductAndOrderDetails(Integer orderId, BookedProduct product, Status status) {
        Integer productId = product.getId();
        Integer quantity = product.getQuantity();
        Integer updateProductResult = Status.BOOKED.equals(status) ?
                orderJpaRepository.bookProduct(productId, quantity)
                : orderJpaRepository.buyProduct(productId, quantity);

        Integer updateDetailsResult = orderJpaRepository.updateOrderDetails(orderId, productId, quantity);
        return updateDetailsResult == 1 && updateProductResult == 1;
    }

    private static void getUsersWithOrders(Map<User, List<FullOrder>> result, Map<String, Object> row) {
        User user = User.builder()
                .id((Integer) row.get("user_id"))
                .name((String) row.get("name"))
                .build();

        BookedProduct product = BookedProduct.builder()
                .id((Integer) row.get("product_id"))
                .name((String) row.get("product"))
                .quantity((Integer) row.get("quantity"))
                .build();

        Integer orderId = (Integer) row.get("order_id");
        FullOrder order = FullOrder.builder()
                .orderId(orderId)
                .tradeDate((Timestamp) row.get("trade_date"))
                .totalAmount((Integer) row.get("amount"))
                .status(Status.valueOf(row.get("status").toString()))
                .products(List.of(product))
                .build();

        if (result.containsKey(user)) {
            List<FullOrder> orders = new ArrayList<>(result.get(user));
            Optional<FullOrder> existingOrder = orders.stream().filter(fullOrder -> fullOrder.getOrderId().equals(orderId)).findFirst();
            if (existingOrder.isPresent()) {
                List<BookedProduct> products = new ArrayList<>(existingOrder.get().getProducts());
                products.add(product);
                existingOrder.get().setProducts(products);
            } else {
                orders.add(order);
                result.put(user, orders);
            }
        } else {
            result.putIfAbsent(user, List.of(order));
        }
    }
}
