package com.lunapps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.StringTemplateResolver;

import java.util.Collections;

/**
 * Created by i4790k on 02.09.2017.
 */
@Configuration
public class ThymeleafConfig {
    private static final String EMAIL_TEMPLATE_ENCODING = "UTF-8";

    public static final String TEMPLATES_MAIL_BASE_PACKAGE = "/templates/mail/";

    /*
    *  Message externalization/internationalization for emails.
    *
    *  NOTE we are avoiding the use of the name 'messageSource' for this bean because that
    *       would make the MessageSource defined in SpringWebConfig (and made available for the
    *       web-side template engine) delegate to this one, and thus effectively merge email
    *       messages into web messages and make both types available at the web side, which could
    *       bring undesired collisions.
    *
    *  NOTE also that given we want this specific message source to be used by our
    *       SpringTemplateEngines for emails (and not by the web one), we will set it explicitly
    *       into each of the TemplateEngine objects with 'setTemplateEngineMessageSource(...)'
    */
    @Bean
    public ResourceBundleMessageSource emailMessageSource() {
        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("templates/mail/msg/MailMessages");
        return messageSource;
    }

    /* ******************************************************************** */
    /*  THYMELEAF-SPECIFIC ARTIFACTS FOR EMAIL                              */
    /*  TemplateResolver(3) <- TemplateEngine                               */
    /* ******************************************************************** */

    @Bean
    public TemplateEngine emailTemplateEngine() {
        final SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        // Resolver for TEXT emails
        templateEngine.addTemplateResolver(textTemplateResolver());
        // Resolver for HTML emails (except the editable one)
        templateEngine.addTemplateResolver(htmlTemplateResolver());
        // Resolver for HTML editable emails (which will be treated as a String)
        templateEngine.addTemplateResolver(stringTemplateResolver());
        // Message source, internationalization specific to emails
        templateEngine.setTemplateEngineMessageSource(emailMessageSource());
        return templateEngine;
    }

    //        todo think about it. delete or not?
    private ITemplateResolver textTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(1);
        templateResolver.setResolvablePatterns(Collections.singleton("text/*"));
        templateResolver.setPrefix(TEMPLATES_MAIL_BASE_PACKAGE);
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private ITemplateResolver htmlTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(2);
        templateResolver.setResolvablePatterns(Collections.singleton("html/*"));
        templateResolver.setPrefix(TEMPLATES_MAIL_BASE_PACKAGE);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(EMAIL_TEMPLATE_ENCODING);
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    private ITemplateResolver stringTemplateResolver() {
        final StringTemplateResolver templateResolver = new StringTemplateResolver();
        templateResolver.setOrder(3);
        // No resolvable pattern, will simply process as a String template everything not previously matched
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }
}
