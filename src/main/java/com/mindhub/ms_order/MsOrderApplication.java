package com.mindhub.ms_order;

import com.mindhub.ms_order.models.OrderEntity;
import com.mindhub.ms_order.models.OrderItem;
import com.mindhub.ms_order.models.OrderStatus;
import com.mindhub.ms_order.repositories.OrderItemRepository;
import com.mindhub.ms_order.repositories.OrderEntityRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MsOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsOrderApplication.class, args);
	}

	@Bean
	CommandLineRunner initData(OrderItemRepository orderItemRepository, OrderEntityRepository orderEntityRepository) {
		return args -> {

			OrderEntity order = new OrderEntity(1L, OrderStatus.COMPLETED);
			OrderEntity order1 = new OrderEntity(2L, OrderStatus.PENDING);

			orderEntityRepository.save(order);
			orderEntityRepository.save(order1);

			OrderItem orderItem = new OrderItem(order, 2L, 7);
			OrderItem orderItem1 = new OrderItem(order, 3L, 6);
			OrderItem orderItem2 = new OrderItem(order1, 4L, 12);

			orderItemRepository.save(orderItem);
			orderItemRepository.save(orderItem1);
			orderItemRepository.save(orderItem2);

		};
	}

	// Task manyToOne -> user
	// OrderItem manyToOne -> Order

	// user OneToMany -> tasks
	// Order OneToMany -> products
}
