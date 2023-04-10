package com.example.system.service;

import com.example.system.exception.ResourceNotFoundException;
import com.example.system.model.Category;
import com.example.system.model.Product;
import com.example.system.model.dto.BookedProduct;
import com.example.system.model.enums.Status;
import com.example.system.repository.jpa.CategoryJpaRepository;
import com.example.system.repository.jpa.OrderJpaRepository;
import com.example.system.repository.jpa.ProductJpaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.system.service.constants.Constants.CANNOT_BE_CREATED;
import static com.example.system.service.constants.Constants.DOES_NOT_EXIST;
import static com.example.system.service.constants.Constants.THE_PRODUCT_WITH_ID;
import static org.springframework.util.ObjectUtils.isEmpty;

@Service("productService")
@AllArgsConstructor
@EnableScheduling
@Slf4j
public class ProductServiceImpl implements ProductService {
    private static final long BOOKED_TIME_IN_MINUTES = 1;
    private static final String PRODUCT_ID = "product_id";
    private static final String AMOUNT = "amount";
    private static final String NAME = "name";

    private ProductJpaRepository productRepository;
    private CategoryJpaRepository categoryJpaRepository;
    private OrderJpaRepository orderJpaRepository;

    @Override
    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    @Override
    public List<Product> getAvailableProducts() {
        return productRepository.getAvailableProducts();
    }

    @Override
    public List<Product> getUnAvailableProducts() {
        return productRepository.getUnAvailableProducts();
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 */5 * * * *")
    public List<BookedProduct> unbookProducts() {
        try {
            List<BookedProduct> unbookedProducts = new ArrayList<>();
            List<Map<String, Object>> bookedProducts = productRepository.getBookedProducts(BOOKED_TIME_IN_MINUTES);

            if (isEmpty(bookedProducts)) {
                log.info("There are no products available to be unbooked.");
                return unbookedProducts;
            }

            boolean productsUnbooked = bookedProducts.stream()
                    .allMatch(row -> {
                        Integer productId = (Integer) row.get(PRODUCT_ID);
                        Integer amount = (Integer) row.get(AMOUNT);
                        return productRepository.unbookProducts(productId, amount) == 1;
                    });

            boolean ordersUpdated = bookedProducts.stream()
                    .mapToInt(row -> (Integer) row.get("order_id"))
                    .distinct().allMatch(orderId ->
                            orderJpaRepository.changeOrderStatus(Status.UNPAID.toString(), orderId) == 1 &&
                                    productRepository.unbookProductsInOrderDetails(orderId) > 0);
            if (productsUnbooked && ordersUpdated) {
                log.info("The booked products are now available again.");
                unbookedProducts = bookedProducts.stream().map(row ->
                        BookedProduct.builder()
                                .id((Integer) row.get(PRODUCT_ID))
                                .name((String) row.get(NAME))
                                .quantity((Integer) row.get(AMOUNT))
                                .build()
                ).collect(Collectors.toList());
            }
            return unbookedProducts;
        } catch (Exception e) {
            log.error("Something happened, and the products were not unbooked.");
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @Override
    public Product createProduct(Map<String, Object> product) {
        String name = (String) product.get(NAME);
        Integer quantity = (Integer) product.get("quantity");
        try {
            List<Integer> categoryIds = getCategoryIds(product);
            if (allCategoriesExist(categoryIds)) {
                Integer id = productRepository.createProduct(name, quantity);
                categoryIds.forEach(categoryId -> productRepository.updateProductCategory(id, categoryId));
                return getProduct(id);
            } else throw new ResourceNotFoundException("Something happened.");
        } catch (Exception e) {
            throw new ResourceNotFoundException("The product " + name + CANNOT_BE_CREATED);
        }
    }

    @Override
    public Product getProduct(Integer id) {
        Product product = productRepository.getProduct(id);
        if (null == product) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        } else {
            return product;
        }
    }

    @Override
    public Map<Product, List<Category>> updateProduct(Integer id, Map<String, Object> product) {
        String name = (String) product.get(NAME);
        List<Integer> categoryIds = getCategoryIds(product);

        if (!categoryIds.isEmpty() && allCategoriesExist(categoryIds)) {
            productRepository.deleteFromProductCategory(id);
            categoryIds.forEach(categoryId -> productRepository.updateProductCategory(id, categoryId));
        }

        if (productRepository.updateProduct(id, name) != 1) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        return getProductWithCategories(id);
    }


    @Override
    public Map<Product, List<Category>> getProductWithCategories(Integer id) {
        List<Map<String, Object>> productWithCategories = productRepository.getProductWithCategories(id);
        if (productWithCategories.isEmpty()) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        Map<String, Object> productMap = productWithCategories.get(0);
        Product product = Product.builder()
                .id((Integer) productMap.get(PRODUCT_ID))
                .name((String) productMap.get("product_name"))
                .build();
        List<Category> categories = productWithCategories.stream().map(row -> Category.builder()
                .id((Integer) row.get("category_id"))
                .name((String) row.get("category_name"))
                .build()
        ).collect(Collectors.toList());

        return Map.of(product, categories);
    }

    @Override
    public String deleteProduct(Integer id) {
        if (productRepository.deleteProduct(id) != 1) {
            throw new ResourceNotFoundException(THE_PRODUCT_WITH_ID + id + DOES_NOT_EXIST);
        }
        return THE_PRODUCT_WITH_ID + id + " has been deleted.";
    }

    private static List<Integer> getCategoryIds(Map<String, Object> product) {
        return ((List<Map<String, Object>>) product.get("categories")).stream()
                .map(item -> (Integer) item.get("id")).collect(Collectors.toList());
    }

    private boolean allCategoriesExist(List<Integer> categoryIds) {
        return categoryIds.stream().allMatch(categoryId -> null != categoryJpaRepository.getCategory(categoryId));
    }
}
