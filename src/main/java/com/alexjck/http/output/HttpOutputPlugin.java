package com.alexjck.http.output;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;

public class HttpOutputPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new HttpOutputMetaData();
    }

    @Override
    public Collection<PluginModule> modules() {
        return Collections.singleton(new HttpOutputModule());
    }
}
