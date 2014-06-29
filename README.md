cas4-javaconfig-overlay-demo
============================

CAS4 overlay demo showing the modern way to configure CAS' Spring Application Context beans
with the modern Spring `@Configuration` style.

The `WEB-INF/deployerConfigContext.xml` is modified to erase all the XML bean definitions and instead
component scan `net.unicon.cas.config` which contains the configuration class for the required beans
which will be picked up by Spring, namely `DeployerConfig.groovy`.

For arguments against this style of Spring application context config, I say: nope, wrong. Java (and a plethora of other great JVM langs) is actually
really good at... ah, well, creating and configuring Java objects. XML is not. XML in Spring had a good run
for the past decade, but now its time to move over to the new, modern Spring configuration options,
and Javaconfig is one of them! Of course, this is a subjective opinion of mine (@dima767), but nevertheless,
this configuration style is my preferred one and will be used whenever is possible. ;-)

See `src/main/groovy/DeployerConfig.groovy` for all the details.

# Versions
```xml
<cas.version>4.1.0-SNAPSHOT</cas.version>
```

# Minimum Requirements
* JDK 1.7+
* Apache Maven 3+
* Servlet container supporting Servlet 3+ spec (e.g. Apache Tomcat 7+)

# Configuration
The `etc` directory contains the sample configuration files that would need to be copied to an external file system location (`/etc/cas` by default) and configured to satisfy local CAS installation needs.

# Deployment

* Execute `mvn clean package`
* Deploy resultant `target/cas.war` to a Servlet3 container of choice
