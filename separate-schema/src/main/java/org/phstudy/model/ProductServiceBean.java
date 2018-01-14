package org.phstudy.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class ProductServiceBean {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Product getMyProduct(int id) {
        Product p = productRepository.getOne(id);
        return p;
    }
}
