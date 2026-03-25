package com.example.demo.service;

import com.example.demo.dto.CustomerRequest;
import com.example.demo.dto.CustomerResponse;
import com.example.demo.dto.OrderResponse;
import com.example.demo.model.Customer;
import com.example.demo.model.CustomerOrder;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository, OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public CustomerResponse createCustomer(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.getName());
        return toCustomerResponse(customerRepository.save(customer));
    }

    public CustomerResponse getCustomerById(Long id) {
        return toCustomerResponse(getCustomerEntity(id));
    }

    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        return customerRepository.findAll(pageable).map(this::toCustomerResponse);
    }

    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = getCustomerEntity(id);
        customer.setName(request.getName());
        return toCustomerResponse(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        Customer customer = getCustomerEntity(id);
        customerRepository.delete(customer);
    }

    public Page<OrderResponse> getOrdersByCustomer(Long customerId, Pageable pageable) {
        ensureCustomerExists(customerId);
        return orderRepository.findByCustomerId(customerId, pageable).map(this::toOrderResponse);
    }

    private Customer getCustomerEntity(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }

    private void ensureCustomerExists(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found");
        }
    }

    private CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(customer.getId(), customer.getName());
    }

    private OrderResponse toOrderResponse(CustomerOrder order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount(),
                order.getCustomer().getId()
        );
    }
}
