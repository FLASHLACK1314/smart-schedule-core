package io.github.flashlack1314.smartschedulecore.utils;

import io.github.flashlack1314.smartschedulecore.models.vo.ResultVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ResultUtil工具类单元测试
 * 验证统一响应工具类的各种场景
 *
 * @author flash
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ResultUtil工具类测试")
class ResultUtilTest {

    private String testMessage;
    private String testData;

    @BeforeEach
    void setUp() {
        testMessage = "测试消息";
        testData = "测试数据";
    }

    @Test
    @DisplayName("成功响应测试 - 无数据")
    void testSuccess() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.success();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("成功响应测试 - 带消息")
    void testSuccessWithMessage() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.success(testMessage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals(testMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("成功响应测试 - 带数据")
    void testSuccessWithData() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.successWithData(testData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertEquals(testData, response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("成功响应测试 - 带消息和数据")
    void testSuccessWithMessageAndData() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.success(testMessage, testData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals(testMessage, response.getBody().getMessage());
        assertEquals(testData, response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("错误响应测试 - 默认错误")
    void testError() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.error();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("服务器内部错误", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("错误响应测试 - 自定义消息")
    void testErrorWithMessage() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.error(testMessage);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals(testMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("错误响应测试 - 自定义状态码和消息")
    void testErrorWithCodeAndMessage() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.error(HttpStatus.BAD_REQUEST, testMessage);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals(testMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("错误响应测试 - 数字状态码")
    void testErrorWithNumericCode() {
        ResponseEntity<ResultVO<String>> response = ResultUtil.error(422, testMessage);

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(422, response.getBody().getCode());
        assertEquals(testMessage, response.getBody().getMessage());
        assertNull(response.getBody().getData());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    @DisplayName("泛型测试 - 复杂数据类型")
    void testGenericData() {
        TestDataObject complexData = new TestDataObject("测试对象", 123);
        ResponseEntity<ResultVO<TestDataObject>> response = ResultUtil.successWithData(complexData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(200, response.getBody().getCode());
        assertEquals("success", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
        assertEquals("测试对象", response.getBody().getData().getName());
        assertEquals(123, response.getBody().getData().getValue());
    }

    @Test
    @DisplayName("时间戳测试 - 确保时间戳正确设置")
    void testTimestamp() {
        long beforeCall = System.currentTimeMillis();
        ResponseEntity<ResultVO<String>> response = ResultUtil.success();
        long afterCall = System.currentTimeMillis();

        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getTimestamp());
        assertTrue(response.getBody().getTimestamp() >= beforeCall);
        assertTrue(response.getBody().getTimestamp() <= afterCall);
    }

    /**
     * 测试用数据对象
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestDataObject {
        private String name;
        private Integer value;
    }
}