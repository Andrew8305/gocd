package com.thoughtworks.go.config;

import com.thoughtworks.go.metrics.service.MetricsProbeService;
import com.thoughtworks.go.plugin.access.configrepo.ConfigRepoExtension;
import com.thoughtworks.go.plugin.access.configrepo.contract.CRParseResult;
import com.thoughtworks.go.plugin.access.configrepo.contract.CRPartialConfig;
import com.thoughtworks.go.util.ConfigElementImplementationRegistryMother;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Collection;

import static junit.framework.TestCase.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GoConfigPluginServiceTest {

    private ConfigRepoExtension extension;
    private GoConfigPluginService service;
    private CRParseResult parseResult;
    private CRPartialConfig partialConfig;

    @Before
    public void SetUp()
    {
        extension = mock(ConfigRepoExtension.class);
        service = new GoConfigPluginService(extension,mock(ConfigCache.class), ConfigElementImplementationRegistryMother.withNoPlugins(),
                mock(MetricsProbeService.class));
        partialConfig = new CRPartialConfig();
        parseResult = new CRParseResult(partialConfig);

        when(extension.parseDirectory(any(String.class), any(String.class), any(Collection.class)))
        .thenReturn(parseResult);
    }

    @Test
    public void shouldAskExtensionForPluginImplementationWhenPluginIdSpecified() throws Exception {
        PartialConfigProvider plugin = service.partialConfigProviderFor("plugin-id");
        assertThat(plugin instanceof ConfigRepoPlugin,is(true));
        CRPartialConfig loaded = ((ConfigRepoPlugin)plugin).parseDirectory(new File("dir"), mock(Collection.class));
        assertSame(loaded, parseResult.getPartialConfig());
    }
}
