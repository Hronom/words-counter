package com.github.hronom.words.counter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan
public class WordsCounterApp {
    private static class AppShutdownHook extends Thread {
        private final ConfigurableApplicationContext ctx;

        public AppShutdownHook(ConfigurableApplicationContext ctxArg) {
            ctx = ctxArg;
        }

        @Override
        public void run() {
            ctx.close();
            System.out.println("Shutdown Log4j2...");
            if (LogManager.getContext() instanceof LoggerContext) {
                System.out.println("Shutdown Log4j2 context...");
                Configurator.shutdown((LoggerContext) LogManager.getContext());
            }
            System.out.println("Shutdown complete...");
        }
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(WordsCounterApp.class, args);
        Runtime.getRuntime().addShutdownHook(new AppShutdownHook(ctx));
    }
}
