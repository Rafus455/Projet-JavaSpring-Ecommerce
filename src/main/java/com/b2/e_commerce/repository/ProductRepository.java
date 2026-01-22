package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
	
public interface ProductRepository extends JpaRepository<Product, Long> {
	List<Product> findByStock(int stock);
}
