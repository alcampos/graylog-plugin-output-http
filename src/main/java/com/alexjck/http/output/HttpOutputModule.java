package com.alexjck.http.output;

import org.graylog2.plugin.PluginModule;

public class HttpOutputModule extends PluginModule {
    @Override
    protected void configure() {
        addMessageOutput(HttpOutput.class);
        addConfigBeans();
    }
}
