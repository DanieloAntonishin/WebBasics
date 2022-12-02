package step.learning.ioc;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import step.learning.services.EmailService;
import step.learning.services.GmailService;
import step.learning.services.hash.HashService;
import step.learning.services.hash.MD5HashService;
import step.learning.services.hash.Sha1HashService;
import step.learning.services.DataService;
import step.learning.services.MysqlDataServices;

import javax.inject.Named;

@Singleton
public class ConfigModule extends AbstractModule {
    @Override
    protected void configure() {
        // Конфигурация служб-поставщиков
       bind(DataService.class).to(MysqlDataServices.class);
       bind(EmailService.class).to(GmailService.class);
    }

    // Внедрения разных сервисов хеша
    @Provides
    @Named("Sha-1")
    HashService getHashProviderSha1(){return new Sha1HashService();}

    @Provides
    @Named("MD-5")
    HashService getHashProviderMd5(){return new MD5HashService();}
}

