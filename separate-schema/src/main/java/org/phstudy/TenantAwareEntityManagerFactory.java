package org.phstudy;

import org.eclipse.persistence.config.PersistenceUnitProperties;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;
import java.util.HashMap;
import java.util.Map;

public class TenantAwareEntityManagerFactory implements EntityManagerFactory {
    private final EntityManagerFactory delegate;

    public TenantAwareEntityManagerFactory(EntityManagerFactory delegate) {
        this.delegate = delegate;
    }

    @Override
    public EntityManager createEntityManager() {
        Map<String, String> map = new HashMap<>();
        return createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(Map map) {
        if(TenantSchemaHolder.getTenantSchema() == null)
            map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "junk_tenant_in_emf_case1");
        else
            map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, TenantSchemaHolder.getTenantSchema());
        return delegate.createEntityManager(map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType) {
        Map<String, String> map = new HashMap<>();
        return createEntityManager(synchronizationType, map);
    }

    @Override
    public EntityManager createEntityManager(SynchronizationType synchronizationType, Map map) {
        if(TenantSchemaHolder.getTenantSchema() == null)
            map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "junk_tenant_in_emf_case2");
        else
            map.put(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, TenantSchemaHolder.getTenantSchema());
        return delegate.createEntityManager(synchronizationType, map);
    }

    @Override
    public CriteriaBuilder getCriteriaBuilder() {
        return delegate.getCriteriaBuilder();
    }

    @Override
    public Metamodel getMetamodel() {
        return delegate.getMetamodel();
    }

    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public Map<String, Object> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Cache getCache() {
        return delegate.getCache();
    }

    @Override
    public PersistenceUnitUtil getPersistenceUnitUtil() {
        return delegate.getPersistenceUnitUtil();
    }

    @Override
    public void addNamedQuery(String name, Query query) {
        delegate.addNamedQuery(name, query);
    }

    @Override
    public <T> T unwrap(Class<T> cls) {
        return delegate.unwrap(cls);
    }

    @Override
    public <T> void addNamedEntityGraph(String graphName, EntityGraph<T> entityGraph) {
        delegate.addNamedEntityGraph(graphName, entityGraph);
    }
}
