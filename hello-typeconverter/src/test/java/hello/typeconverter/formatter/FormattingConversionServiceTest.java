package hello.typeconverter.formatter;

import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.type.IpPort;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import static org.assertj.core.api.Assertions.assertThat;

class FormattingConversionServiceTest {
    @Test
    void formattingConversionService() {
        // 스프링부트 내부에서는 DefaultFormattingConversionService 를 상속받은 WebConversionService 를 사용한다.
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService();

        // 컨버터 등록
        conversionService.addConverter(new StringToIpPortConverter());
        conversionService.addConverter(new IpPortToStringConverter());

        // 포매터 등록
        conversionService.addFormatter(new MyNumberFormatter());

        // 컨버터 사용
        IpPort ipPort = conversionService.convert("127.0.0.1:8080", IpPort.class);
        assertThat(ipPort).isEqualTo(new IpPort("127.0.0.1", 8080));

        // 포매터 사용
        assertThat(conversionService.convert(1000, String.class))
                .isEqualTo("1,000");
        assertThat(conversionService.convert("1,000", Number.class)) // Long.class 가능
                .isEqualTo(1000L);
    }
}
