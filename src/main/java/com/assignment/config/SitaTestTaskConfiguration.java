package com.assignment.config;

import java.io.File;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.endpoint.SourcePollingChannelAdapter;
import org.springframework.integration.file.FileNameGenerator;
import org.springframework.integration.file.FileReadingMessageSource;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.filters.SimplePatternFileListFilter;
import org.springframework.integration.handler.ServiceActivatingHandler;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;

import com.assignment.util.FileProcessor;

@Configuration
@EnableIntegration
public class SitaTestTaskConfiguration {

	@Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @Bean
    public PollableChannel outputChannel() {
        return new QueueChannel();
    }

    @Bean
    public FileProcessor fileProcessor() {
        return new FileProcessor();
    }

    @Bean
    public FileReadingMessageSource fileReadingMessageSource() {
        FileReadingMessageSource source = new FileReadingMessageSource();
        source.setDirectory(new File("src/main/resources"));
        source.setFilter(new SimplePatternFileListFilter("*.txt"));
        source.setUseWatchService(true);
        return source;
    }

    @Bean
    public FileWritingMessageHandler fileWritingMessageHandler() {
        FileWritingMessageHandler handler = new FileWritingMessageHandler(new File("src/main/resources/output"));
        handler.setDeleteSourceFiles(true); // delete the original file after processing
        handler.setFileNameGenerator(new FileNameGenerator() {
            public String generateFileName(Message<?> message) {
                return "processed_" + UUID.randomUUID() + ".txt";
            }
        });
        return handler;
    }

    @Bean
    public ServiceActivatingHandler serviceActivatingHandler() {
        ServiceActivatingHandler handler = new ServiceActivatingHandler(fileProcessor());
        handler.setOutputChannel(outputChannel());
        return handler;
    }

    @Bean
    public SourcePollingChannelAdapter sourcePollingChannelAdapter() {
        SourcePollingChannelAdapter adapter = new SourcePollingChannelAdapter();
        adapter.setSource(fileReadingMessageSource());
        adapter.setOutputChannel(inputChannel());
        adapter.setTaskExecutor(new SimpleAsyncTaskExecutor());
        return adapter;
    }

    @Bean
    public IntegrationFlow integrationFlow() {
        return IntegrationFlow.from(fileReadingMessageSource(), c -> c.poller(Pollers.fixedDelay(1000)))
                .handle(serviceActivatingHandler())
                .channel(outputChannel())
                .handle(fileWritingMessageHandler())
                .channel("nullChannel")
                .get();
    }
}