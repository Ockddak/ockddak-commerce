package com.ockddak.commerce;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
public class DbConnectionTest {

    // Spring 애플리케이션 컨텍스트를 주입받아 빈(Bean)이 올바르게 로드되었는지 확인
    @Autowired
    private ApplicationContext context;

    // 데이터베이스 연결을 위한 DataSource 빈을 주입받아 테스트
    @Autowired
    private DataSource dataSource;

    @Test
    @DisplayName("애플리케이션 컨텍스트 및 DB 연결 테스트")
    void applicationContextAndDbConnectionTest() {
        System.out.println("애플리케이션 컨텍스트 로딩 확인: " + context);
        Assertions.assertNotNull(context, "애플리케이션 컨텍스트가 null입니다.");

        System.out.println("데이터 소스 빈 확인: " + dataSource);
        Assertions.assertNotNull(dataSource, "DataSource 빈이 생성되지 않았습니다.");

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("DB 연결 성공!");
            Assertions.assertNotNull(connection, "DB 연결 객체가 null입니다.");
            System.out.println("DB 연결 URL: " + connection.getMetaData().getURL());
        } catch (SQLException e) {
            Assertions.fail("DB 연결에 실패했습니다: " + e.getMessage());
        }
    }

}
