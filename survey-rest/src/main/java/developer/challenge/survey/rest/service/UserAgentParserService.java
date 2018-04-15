package developer.challenge.survey.rest.service;

import developer.challenge.survey.rest.model.UserSurveyAnswer;
import in.ankushs.browscap4j.domain.Browscap;
import in.ankushs.browscap4j.domain.BrowserCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class UserAgentParserService {

    @Autowired ResourceLoader resourceLoader;

    @Value("${browscap.enabled:false}") boolean isParserEnabled;

    Optional<Browscap> optionalParser;

    @PostConstruct
    public void init() throws IOException {
        optionalParser = isParserEnabled ? Optional.ofNullable(getBrowscap()) : Optional.empty();
    }

    Browscap getBrowscap() throws IOException {
        // https://github.com/ankushs92/Browscap4j/issues/20
        InputStream is = resourceLoader.getResource("classpath:/browscap.csv").getInputStream();
        String targetFile = resourceLoader.getResource("classpath:browscap.csv").getFilename();
        File file = new File(targetFile);
        if (!file.exists()) {
            Files.copy(is, Paths.get(targetFile));
        }
        return new Browscap(file);
    }

    public UserSurveyAnswer parseUserAgent(Optional<String> optionalUserAgent) {
        UserSurveyAnswer userSurveyAnswer = new UserSurveyAnswer();

        optionalParser.ifPresent(browscap -> optionalUserAgent.ifPresent(userAgent -> {
            BrowserCapabilities browserCapabilities = browscap.lookup(userAgent);
            userSurveyAnswer.setBrowser(browserCapabilities.getBrowser());
            userSurveyAnswer.setBrowserType(browserCapabilities.getBrowserType());
            userSurveyAnswer.setDeviceBrandName(browserCapabilities.getDeviceBrandName());
            userSurveyAnswer.setDeviceCodeName(browserCapabilities.getDeviceCodeName());
            userSurveyAnswer.setDeviceName(browserCapabilities.getDeviceName());
            userSurveyAnswer.setDeviceType(browserCapabilities.getDeviceType());
            userSurveyAnswer.setPlatform(browserCapabilities.getPlatform());
            userSurveyAnswer.setPlatformMaker(browserCapabilities.getPlatformMaker());
            userSurveyAnswer.setPlatformVersion(browserCapabilities.getPlatformVersion());
        }));

        return userSurveyAnswer;
    }
}
