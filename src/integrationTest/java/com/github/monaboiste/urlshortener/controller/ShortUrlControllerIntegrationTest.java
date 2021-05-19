package com.github.monaboiste.urlshortener.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monaboiste.urlshortener.dto.ShortUrlDto;
import com.github.monaboiste.urlshortener.error.Error;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("integration")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShortUrlControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp(@Autowired final DataSource dataSource) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(true);
            ScriptUtils.executeSqlScript(conn, new ClassPathResource("sample-test-data.sql"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    @Transactional
    void shouldCreateNewShortUrlAndReturnCreatedShortUrlDto_Returns_201() throws Exception {
        final ShortUrlDto validWithAllFields = ShortUrlDto.builder()
                .url("example.com")
                .alias("sample-example")
                .build();

        final MockHttpServletResponse response = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(validWithAllFields)))
                .andDo(print())
                .andReturn()
                .getResponse();

        final ShortUrlDto actual = convertToPojo(response.getContentAsString(), ShortUrlDto.class);

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(actual.getId()).isNotEqualTo(0),
                () -> assertThat(actual.getUrl()).isEqualTo(validWithAllFields.getUrl()),
                () -> assertThat(actual.getAlias()).isEqualTo(validWithAllFields.getAlias()),
                () -> assertThat(actual.getRedirectingUrl()).isNotNull(),
                () -> assertThat(actual.getCreatedAt()).isNotNull()
        );
    }

    @Test
    void shouldFailOnCreatingShortUrlWhenTakenAlias_Returns_400() throws Exception {
        final ShortUrlDto invalidWithTakenAlias = ShortUrlDto.builder()
                .url("example.com")
                .alias("sample-1")
                .build();

        final MockHttpServletResponse response = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(invalidWithTakenAlias)))
                .andDo(print())
                .andReturn()
                .getResponse();

        final Error actual = convertToPojo(response.getContentAsString(), Error.class);

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(actual.getErrors().size()).isEqualTo(1),
                () -> assertThat(actual.getErrors().get(0).getField()).isEqualTo("alias")
        );
    }

    @Test
    void shouldGetAllShortUrls_Returns_200() throws Exception {
        final MockHttpServletResponse response = mockMvc.perform(
                get("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final ShortUrlDto[] actual = convertToPojo(response.getContentAsString(), ShortUrlDto[].class);

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(actual.length).isEqualTo(3)
        );
    }

    @Test
    void shouldFailOnCreatingShortUrlWhenNoRequiredUrlField_Returns_400() throws Exception {
        final ShortUrlDto invalidWithoutUrl = ShortUrlDto.builder()
                .alias("sample")
                .build();

        final MockHttpServletResponse response = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(invalidWithoutUrl)))
                .andDo(print())
                .andReturn()
                .getResponse();

        final Error actual = convertToPojo(response.getContentAsString(), Error.class);

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(actual.getErrors().size()).isEqualTo(1),
                () -> assertThat(actual.getErrors().get(0).getField()).isEqualTo("url")
        );
    }

    @Test
    void shouldCreateRandomAliasWhenNoAliasPresent_Returns_201() throws Exception {
        final ShortUrlDto validWithoutAlias = ShortUrlDto.builder()
                .url("example.com")
                .build();

        final MockHttpServletResponse response = mockMvc.perform(
                post("/short_urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertToJson(validWithoutAlias)))
                .andDo(print())
                .andReturn()
                .getResponse();

        final ShortUrlDto actual = convertToPojo(response.getContentAsString(), ShortUrlDto.class);

        assertAll(
                () -> assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(actual.getId()).isNotEqualTo(0),
                () -> assertThat(actual.getAlias()).isNotNull(),
                () -> assertThat(actual.getUrl()).isEqualTo(validWithoutAlias.getUrl()),
                () -> assertThat(actual.getRedirectingUrl()).isNotNull(),
                () -> assertThat(actual.getCreatedAt()).isNotNull()
        );
    }

    private <T> T convertToPojo(final String jsonString, Class clazz) throws JsonProcessingException {
        return (T) objectMapper.readValue(jsonString, clazz);
    }

    private <T> String convertToJson(final T pojo) throws JsonProcessingException {
        return objectMapper.writeValueAsString(pojo);
    }
}
