package org.jboss.weld.tests.alternatives.weld930;

import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Named;

/**
 * @author Marko Luksa
 */
@Alternative
public class AlternativeProducer {

    @Produces
    @Named("product")
    public Product produce() throws Exception {
        return new Product("Alternative");
    }
}
