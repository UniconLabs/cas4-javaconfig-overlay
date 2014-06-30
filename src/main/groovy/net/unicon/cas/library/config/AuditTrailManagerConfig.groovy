package net.unicon.cas.library.config

import com.github.inspektr.audit.AuditTrailManager
import com.github.inspektr.audit.support.Slf4jLoggingAuditTrailManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Sample separate config class simulating framework/library config features that would be encapsulated in its own jar.
 *
 * @author Dmitriy Kopylenko
 */
@Configuration
class AuditTrailManagerConfig {

    @Bean
    AuditTrailManager auditTrailManager() {
        new Slf4jLoggingAuditTrailManager()
    }
}
