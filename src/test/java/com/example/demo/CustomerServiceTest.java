package com.example.demo;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerOrder;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldCreateGetUpdateAndDeleteCustomer() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        CustomerRequest createRequest = new CustomerRequest();
        createRequest.setName("Alice");

        CustomerResponse created = customerService.createCustomer(createRequest);
        assertEquals("Alice", created.getName());

        CustomerResponse fetched = customerService.getCustomerById(created.getId());
        assertEquals(created.getId(), fetched.getId());

        CustomerRequest updateRequest = new CustomerRequest();
        updateRequest.setName("Alice Updated");
        CustomerResponse updated = customerService.updateCustomer(created.getId(), updateRequest);
        assertEquals("Alice Updated", updated.getName());

        customerService.deleteCustomer(created.getId());
        assertFalse(customerRepository.existsById(created.getId()));
    }

    @Test
    void shouldReturnPaginatedCustomers() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        Customer first = new Customer();
        first.setName("First");
        customerRepository.save(first);

        Customer second = new Customer();
        second.setName("Second");
        customerRepository.save(second);

        Page<CustomerResponse> page = customerService.getAllCustomers(PageRequest.of(0, 1));

        assertEquals(1, page.getContent().size());
        assertEquals(2, page.getTotalElements());
    }

    @Test
    void shouldReturnPaginatedOrdersByCustomer() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();

        Customer customer = new Customer();
        customer.setName("Orders Owner");
        Customer savedCustomer = customerRepository.save(customer);

        CustomerOrder order = new CustomerOrder();
        order.setCustomer(savedCustomer);
        order.setOrderDate(LocalDate.of(2026, 2, 24));
        order.setTotalAmount(new BigDecimal("125.50"));
        orderRepository.save(order);

        Page<OrderResponse> page = customerService.getOrdersByCustomer(savedCustomer.getId(), PageRequest.of(0, 10));

        assertEquals(1, page.getTotalElements());
        assertFalse(page.getContent().isEmpty());
        assertEquals(savedCustomer.getId(), page.getContent().get(0).getCustomerId());
        assertTrue(page.getContent().get(0).getTotalAmount().compareTo(new BigDecimal("125.50")) == 0);
    }
}
