package net.unicon.cas.library.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sample Spring 4 infrastructure annotation encapsulating the seamless infrastructure/framework beans contribution
 * to the main application context. {@code Slf4jLoggingAuditTrailManager} in this case.
 *
 * @author Dmitriy Kopylenko
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AuditTrailManagerConfig.class)
public @interface EnableDefaultCasAuditTrailManager {
}
