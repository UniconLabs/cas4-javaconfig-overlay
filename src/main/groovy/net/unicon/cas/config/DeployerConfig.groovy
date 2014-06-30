package net.unicon.cas.config

import net.unicon.cas.library.config.EnableDefaultCasAuditTrailManager
import org.jasig.cas.authentication.AcceptUsersAuthenticationHandler
import org.jasig.cas.authentication.AnyAuthenticationPolicy
import org.jasig.cas.authentication.AuthenticationManager
import org.jasig.cas.authentication.PolicyBasedAuthenticationManager
import org.jasig.cas.authentication.handler.support.HttpBasedServiceCredentialsAuthenticationHandler
import org.jasig.cas.authentication.principal.BasicPrincipalResolver
import org.jasig.cas.authentication.principal.PersonDirectoryPrincipalResolver
import org.jasig.cas.monitor.HealthCheckMonitor
import org.jasig.cas.monitor.MemoryMonitor
import org.jasig.cas.monitor.SessionMonitor
import org.jasig.cas.services.InMemoryServiceRegistryDaoImpl
import org.jasig.cas.services.RegexRegisteredService
import org.jasig.cas.services.ServiceRegistryDao
import org.jasig.cas.ticket.registry.TicketRegistry
import org.jasig.cas.util.HttpClient
import org.jasig.services.persondir.IPersonAttributeDao
import org.jasig.services.persondir.support.StubPersonAttributeDao
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Spring Javaconfig class for various beans found in deployerConfigContext.xml
 *
 * This class acts as a regular Spring-managed bean picked up by Spring (with a component-scan directive) as
 * {@code @Configuration} is a meta-annotation on top of regular {@code @Component} annotation.
 *
 * Once picked up by Spring, the great magic happens within {@code @Bean} methods where the instances created and returned
 * from those methods will be registered and made available in the overall application context.
 *
 * By default the name of {@code @Bean} methods results in the same bean id within application context.
 *
 * {@link EnableDefaultCasAuditTrailManager} is the custom Spring meta annotation that imports a separate
 * config class for {@link com.github.inspektr.audit.support.Slf4jLoggingAuditTrailManager} - to simulate/demonstrate
 * capability of defining and seamlessly plugging these low-level plumbing configurations via external jars, etc.
 * This would allow for 'micro-service' style configuration management and easy to manage 3-rd party plugin development for CAS.
 *
 * @author Dmitriy Kopylenko
 * @see <a href="http://docs.spring.io/spring/docs/4.0.5.RELEASE/spring-framework-reference/htmlsingle/#beans-java">Spring Javaconfig</a>
 */
@Configuration
@EnableDefaultCasAuditTrailManager
class DeployerConfig {

    @Bean
    AuthenticationManager authenticationManager(HttpClient httpClient, IPersonAttributeDao attributeRepository) {

        new PolicyBasedAuthenticationManager([
                (new HttpBasedServiceCredentialsAuthenticationHandler(httpClient: httpClient, requireSecure: false)): new BasicPrincipalResolver(),
                (new AcceptUsersAuthenticationHandler(users: [casuser:'Mellon'])): new PersonDirectoryPrincipalResolver(attributeRepository: attributeRepository)

        ]).with {
            it.authenticationPolicy = new AnyAuthenticationPolicy()
            return it
        }
    }


    @Bean
    IPersonAttributeDao attributeRepository() {
        new StubPersonAttributeDao().with {
            it.backingMap = [uid: ['uid']]
            return it
        }
    }

    @Bean
    ServiceRegistryDao serviceRegistryDao() {

        new InMemoryServiceRegistryDaoImpl().with {
            it.registeredServices = [
                    new RegexRegisteredService().with {
                        it.id = 0L
                        it.name = 'front'
                        it.description = 'Allows only front services'
                        it.serviceId = '^https?://front.*'
                        it.evaluationOrder = 10000001
                        it.logoutType = 'FRONT_CHANNEL'
                        it.anonymousAccess = true
                        return it
                    },
                    new RegexRegisteredService().with {
                        it.id = 1L
                        it.name = 'back'
                        it.description = 'Allows only back services'
                        it.serviceId = '^https?://back.*'
                        it.evaluationOrder = 10000002
                        return it
                    },
                    new RegexRegisteredService().with {
                        it.id = 2L
                        it.name = 'google'
                        it.description = 'Allows only gogle services'
                        it.serviceId = '^https?://www.google.*'
                        it.evaluationOrder = 10000003
                        return it
                    }
            ]
            return it
        }
    }

    @Bean
    HealthCheckMonitor healthCheckMonitor(TicketRegistry ticketRegistry) {
        new HealthCheckMonitor().with {
            it.monitors = [
                    new MemoryMonitor(freeMemoryWarnThreshold: 10),
                    new SessionMonitor(ticketRegistry: ticketRegistry, serviceTicketCountWarnThreshold: 5_000, sessionCountWarnThreshold: 100_000)
            ]
            return it
        }
    }
}
