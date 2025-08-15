package com.pahanaedu.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class JSONUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T parseJsonRequest(jakarta.servlet.http.HttpServletRequest request, Class<T> clazz)
            throws IOException {
        StringBuilder buffer = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line);
        }
        return objectMapper.readValue(buffer.toString(), clazz);
    }

    public static void sendJsonResponse(HttpServletResponse response, int status,
                                        String message, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        JsonResponse jsonResponse = new JsonResponse(status, message, data);
        objectMapper.writeValue(response.getWriter(), jsonResponse);
    }

    private static class JsonResponse {
        private int status;
        private String message;
        private Object data;

        public JsonResponse(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.data = data;
        }

        // Getters
        public int getStatus() { return status; }
        public String getMessage() { return message; }
        public Object getData() { return data; }
    }
}