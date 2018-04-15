package developer.challenge.survey.rest.service;

import developer.challenge.survey.rest.model.UserSurveyAnswer;
import in.ankushs.browscap4j.domain.Browscap;
import in.ankushs.browscap4j.domain.BrowserCapabilities;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UserAgentParserServiceTest {

    UserAgentParserService service = new UserAgentParserService();

    @Test public void should_do_nothing_when_parser_is_disabled() {
        // Given
        service.optionalParser = Optional.empty();

        // When
        UserSurveyAnswer result = service.parseUserAgent(Optional.of("Safari"));

        // Then
        assertThat(result).isNotNull();
    }

    @Test public void should_do_nothing_when_user_agent_is_absent() {
        // Given
        Browscap browscapMock = mock(Browscap.class);
        service.optionalParser = Optional.of(browscapMock);

        // When
        UserSurveyAnswer result = service.parseUserAgent(Optional.empty());

        // Then
        verify(browscapMock, never()).lookup(anyString());
        assertThat(result).isNotNull();
    }

    @Test public void should_parse_user_agent() {
        // Given
        String userAgent = "Safari";
        Browscap browscapMock = mock(Browscap.class);
        when(browscapMock.lookup(userAgent)).thenReturn(buildBrowserCapabilities());
        service.optionalParser = Optional.of(browscapMock);

        // When
        UserSurveyAnswer result = service.parseUserAgent(Optional.of(userAgent));

        // Then
        verify(browscapMock).lookup(userAgent);

        assertThat(result).isNotNull();
        assertThat(result.getBrowser()).isEqualTo("Safari");
        assertThat(result.getBrowserType()).isEqualTo("Safari Type");
        assertThat(result.getDeviceBrandName()).isEqualTo("Apple");
        assertThat(result.getDeviceCodeName()).isEqualTo("iPhone");
        assertThat(result.getDeviceName()).isEqualTo("iPhone");
        assertThat(result.getDeviceType()).isEqualTo("Mobile Phone");
        assertThat(result.getPlatform()).isEqualTo("iOS");
        assertThat(result.getPlatformMaker()).isEqualTo("Apple Inc");
        assertThat(result.getPlatformVersion()).isEqualTo("6.0");
    }

    @Test public void should_init_with_empty_browscap_when_disabled() throws IOException {
        // Given
        service.isParserEnabled = false;

        // When
        service.init();

        // Then
        assertThat(service.optionalParser).isEmpty();
    }

    @Test public void should_init_with_browscap_when_enabled() throws IOException {
        // Given
        service.isParserEnabled = true;

        UserAgentParserService spy = spy(service);
        // workaround to avoid loading and copying file
        doReturn(null).when(spy).getBrowscap();

        // When
        spy.init();

        // Then
        verify(spy).getBrowscap();
    }

    private BrowserCapabilities buildBrowserCapabilities() {
        return new BrowserCapabilities.Builder().browser("Safari") //
                                                .browserType("Safari Type") //
                                                .deviceBrandName("Apple") //
                                                .deviceCodeName("iPhone") //
                                                .deviceName("iPhone") //
                                                .deviceType("Mobile Phone") //
                                                .platform("iOS") //
                                                .platformMaker("Apple Inc") //
                                                .platformVersion("6.0") //
                                                .build(); //
    }
}