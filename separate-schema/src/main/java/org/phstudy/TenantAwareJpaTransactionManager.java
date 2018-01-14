package org.phstudy;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TenantAwareJpaTransactionManager extends JpaTransactionManager {

    @Override
    protected void doBegin(final Object transaction, final TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        EntityManagerFactory factory = getEntityManagerFactory();
        final EntityManagerHolder emHolder = (EntityManagerHolder) TransactionSynchronizationManager.getResource(factory);
        final EntityManager em = emHolder.getEntityManager();

        final String schema = TenantSchemaHolder.getTenantSchema();
        if(schema != null) {
            em.setProperty(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, TenantSchemaHolder.getTenantSchema());
        }
        else {
            em.setProperty(PersistenceUnitProperties.MULTITENANT_PROPERTY_DEFAULT, "junk_tenant_in_jpa_tx_mgr");
        }
    }
}
