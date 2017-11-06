package com.alexjck.http.output;

import java.util.Collections;
import java.util.List;

import org.graylog2.plugin.Message;
import org.graylog2.plugin.configuration.Configuration;
import org.graylog2.plugin.configuration.ConfigurationRequest;
import org.graylog2.plugin.configuration.fields.BooleanField;
import org.graylog2.plugin.configuration.fields.ConfigurationField;
import org.graylog2.plugin.configuration.fields.ListField;
import org.graylog2.plugin.configuration.fields.TextField;
import org.graylog2.plugin.inputs.annotations.ConfigClass;
import org.graylog2.plugin.inputs.annotations.FactoryClass;
import org.graylog2.plugin.outputs.MessageOutput;
import org.graylog2.plugin.outputs.MessageOutputConfigurationException;
import org.graylog2.plugin.streams.Stream;

import com.alexjck.http.output.senders.HttpSender;
import com.alexjck.http.output.senders.Sender;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

public class HttpOutput implements MessageOutput {

	private static final String API_HOST = "api_host";
	private static final String FIELD_LIST = "field_list";
	private static final String STREAM_BOOL = "should_send_stream";

	private boolean running = true;

	private final Sender sender;

	@Inject
	public HttpOutput(@Assisted Configuration configuration) throws MessageOutputConfigurationException {
		// Check configuration.
		if (!checkConfiguration(configuration)) {
			throw new MessageOutputConfigurationException("Missing configuration.");
		}

		// Set up sender.
		sender = new HttpSender(configuration.getString(API_HOST), configuration.getList(FIELD_LIST));

		running = true;
	}

	@Override
	public void stop() {
		sender.stop();
		running = false;
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void write(Message message) throws Exception {
		if (message == null || message.getFields() == null || message.getFields().isEmpty()) {
			return;
		}

		if (!sender.isInitialized()) {
			sender.initialize();
		}

		sender.send(message);
	}

	@Override
	public void write(List<Message> list) throws Exception {
		if (list == null) {
			return;
		}

		for (Message m : list) {
			write(m);
		}
	}

	public boolean checkConfiguration(Configuration c) {
		return c.stringIsSet(API_HOST);
	}

	@FactoryClass
	public interface Factory extends MessageOutput.Factory<HttpOutput> {
		@Override
		HttpOutput create(Stream stream, Configuration configuration);

		@Override
		Config getConfig();

		@Override
		Descriptor getDescriptor();
	}

	@ConfigClass
	public static class Config extends MessageOutput.Config {
		@Override
		public ConfigurationRequest getRequestedConfiguration() {
			final ConfigurationRequest configurationRequest = new ConfigurationRequest();

			configurationRequest.addField(new TextField(API_HOST, "HTTP host", "", "Hostname or IP address",
					ConfigurationField.Optional.NOT_OPTIONAL));

			configurationRequest.addField(new BooleanField(STREAM_BOOL, "Send stream in JSON?", false,
					"Add stream field in the JSON request"));

			configurationRequest.addField(new ListField(FIELD_LIST, "Field list", Collections.emptyList(),
					Collections.emptyMap(), "Field list to POST to the host as JSON",
					ConfigurationField.Optional.NOT_OPTIONAL, ListField.Attribute.ALLOW_CREATE));

			return configurationRequest;
		}
	}

	public static class Descriptor extends MessageOutput.Descriptor {
		public Descriptor() {
			super("HTTP Output", false, "", "POST messages to your HOST via HTTP.");
		}
	}

}
