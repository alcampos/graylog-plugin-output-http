package com.alexjck.http.output;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class HttpOutputMetaData implements PluginMetaData {
	
	private static final String PLUGIN_PROPERTIES = "com.alexjck.http.output.graylog-plugin-ouput-http/graylog-plugin.properties";
	
    @Override
    public String getUniqueId() {
        return "com.alexjck.http.output.HttpOutputPlugin";
    }

    @Override
    public String getName() {
        return "Http Output";
    }

    @Override
    public String getAuthor() {
    	 return "Alexander Campos <alexjck@gmail.com>";
    }

    @Override
    public URI getURL() {
    	return URI.create("https://github.com/alcampos/graylog-plugin-output-http");
    }

    @Override
    public Version getVersion() {
    	return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "version", Version.from(0, 0, 1, "unknown"));
    }

    @Override
    public String getDescription() {
        return "Writes messages to your API via HTTP.";
    }

    @Override
    public Version getRequiredVersion() {
    	return Version.fromPluginProperties(getClass(), PLUGIN_PROPERTIES, "graylog.version", Version.from(2, 3, 0));
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.singleton(ServerStatus.Capability.SERVER);
    }
}
