package org.jmeterplugins.repository.logging;

import org.apache.jmeter.gui.LoggerPanel;
import org.apache.log.LogEvent;
import org.jmeterplugins.repository.PluginManager;
import org.jmeterplugins.repository.plugins.PluginSuggester;

public class LoggerPanelWrapping extends LoggerPanel {

    protected PluginSuggester suggester;

    public LoggerPanelWrapping(PluginManager mgr) {
        super();
        this.suggester = new PluginSuggester(mgr);
    }

    public PluginSuggester getSuggester() {
        return suggester;
    }

    public void setSuggester(PluginSuggester suggester) {
        this.suggester = suggester;
    }
}
