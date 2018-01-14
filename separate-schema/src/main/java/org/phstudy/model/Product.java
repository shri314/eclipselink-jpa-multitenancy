package org.phstudy.model;

import org.eclipse.persistence.annotations.Multitenant;
import org.eclipse.persistence.annotations.TenantTableDiscriminator;
import org.eclipse.persistence.config.PersistenceUnitProperties;

import javax.persistence.Entity;
import javax.persistence.Id;

import java.util.Objects;

import static org.eclipse.persistence.annotations.MultitenantType.TABLE_PER_TENANT;
import static org.eclipse.persistence.annotations.TenantTableDiscriminatorType.SCHEMA;

/**
 * Created by study on 11/14/14.
 */
@Entity
@Multitenant(value = TABLE_PER_TENANT)
@TenantTableDiscriminator(type = SCHEMA, contextProperty = PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT)
public class Product {
    @Id
    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Product { id = " + id + ", name = " + name + " }";
    }
}
