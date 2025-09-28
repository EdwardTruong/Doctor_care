package com.example.doctorcare.application.exception;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.doctorcare.core.web.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class AbstractGlobalExceptionHandler extends ResponseEntityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {
    private static final String X_MESSAGE_HEADER = "x-message";

    public static final String FIELD_ERROR_KEY = "fieldErrors";

    @Autowired
    private MessageSource messageSource;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        log.warn("Lỗi xác thực không thành công (Unauthorized): {}", authException.getMessage());
        // Ủy quyền cho phương thức xử lý tập trung để tạo và ghi response
        writeProblemDetailResponse(request, response, authException, HttpStatus.UNAUTHORIZED, "error.unauthorized");
    }

    @Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.access.AccessDeniedException accessDeniedException)
			throws IOException, ServletException {
		log.warn("Truy cập bị từ chối (Forbidden): {}", accessDeniedException.getMessage());
        // Ủy quyền cho phương thức xử lý tập trung để tạo và ghi response
        writeProblemDetailResponse(request, response, accessDeniedException, HttpStatus.FORBIDDEN, "error.forbidden");
	}
    
    // Xử lý ForbiddenException (403 Forbidden)
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleForbiddenException(ForbiddenException ex, WebRequest request) {
        log.warn("Forbidden access attempt: {}", ex.getMessage());
        return buildProblemDetailResponse(ex, HttpStatus.FORBIDDEN, request, "error.forbidden");
    }

    // Xử lý AccessDeniedException và AuthorizationDeniedException từ Spring Security (403 Forbidden)
    @ExceptionHandler({ org.springframework.security.access.AccessDeniedException.class, AuthorizationDeniedException.class })
    public ResponseEntity<ApiResponse<ProblemDetail>> handleAccessDeniedException(Exception ex, WebRequest request) {
        return buildProblemDetailResponse(ex, HttpStatus.FORBIDDEN, request);
    }

    // Xử lý ResourceNotFoundException (404 Not Found) - thay thế cho EntityNotFoundException
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return buildProblemDetailResponse(ex, HttpStatus.NOT_FOUND, request);
    }

    // Xử lý BadRequestException (400 Bad Request) - cho các lỗi nghiệp vụ từ client
    // Bao gồm các exception kế thừa như RoleInUseException, InvalidTokenException, PermissionNotFoundException
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return buildProblemDetailResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    // Xử lý IllegalArgumentException (400 Bad Request)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        log.warn("Bad request received: {}", ex.getMessage()); // Ghi log lỗi như một cảnh báo
        // Chuyển tiếp sang handler chuẩn
        return buildProblemDetailResponse(ex, HttpStatus.BAD_REQUEST, request, "error.illegalArgument");
    }

    // Xử lý IllegalStateException (409 Conflict)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        // IllegalStateException có thể chỉ ra lỗi từ client (ví dụ: tài nguyên trùng lặp) hoặc lỗi logic của server.
        // Sử dụng 409 Conflict là một lựa chọn mặc định hợp lý cho các trường hợp như "người dùng đã tồn tại".
        log.warn("Request conflicted with current state: {}", ex.getMessage());
        return buildProblemDetailResponse(ex, HttpStatus.CONFLICT, request, "error.conflict");
    }

    // Xử lý ConflictException (409 Conflict) - cho các xung đột trạng thái hoặc tài nguyên
    // Bao gồm các exception kế thừa như RoleAlreadyExistsException, DuplicateRecordException, DuplicateUsernameException
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleConflictException(ConflictException ex, WebRequest request) {
        return buildProblemDetailResponse(ex, HttpStatus.CONFLICT, request);
    }

    // Xử lý tất cả các ngoại lệ không được xử lý (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ProblemDetail>> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("An unhandled exception occurred: ", ex); // Ghi log lỗi để debug
        return buildProblemDetailResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request, "error.internalServerError");
    }

    /**
     * Ghi trực tiếp ProblemDetail vào HttpServletResponse.
     * Dùng cho các entry point của Spring Security (commence, handle) không trả về ResponseEntity.
     */
    private void writeProblemDetailResponse(HttpServletRequest request, HttpServletResponse response, Exception ex, HttpStatus status, String messageCode) throws IOException {
        String message = resolveMessage(messageCode, null, ex.getMessage(), request.getLocale());
        ProblemDetail problemDetail = createProblemDetail(ex, status, request.getRequestURI(), message);
        
        ApiResponse<ProblemDetail> apiResponse = buildApiResponse(problemDetail);
        
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }

    /**
     * Phương thức trung tâm để xây dựng một ResponseEntity chứa ProblemDetail.
     * Nó xử lý việc dịch message, tạo cấu trúc response nhất quán.
     *
     * @param ex          Exception được ném ra.
     * @param status      HttpStatus tương ứng.
     * @param request     WebRequest hiện tại.
     * @param messageCode (Tùy chọn) Mã lỗi để tra cứu trong message bundle.
     * @return ResponseEntity chứa ApiResponse đã được định dạng.
     */
    private ResponseEntity<ApiResponse<ProblemDetail>> buildProblemDetailResponse(Exception ex, HttpStatus status, WebRequest request, String... messageCode) {
        String resolvedMessageCode = getMessageCode(ex, messageCode);
        Object[] messageArgs = getMessageArgs(ex);
        String message = resolveMessage(resolvedMessageCode, messageArgs, ex.getMessage(), request.getLocale());

        String path = request.getDescription(false).substring(4); // Lấy URI từ request
        ProblemDetail problemDetail = createProblemDetail(ex, status, path, message);

        ApiResponse<ProblemDetail> apiResponse = buildApiResponse(problemDetail);
        HttpHeaders headers = new HttpHeaders();
        if(messageCode != null && messageCode.length > 0) {
            headers.add(X_MESSAGE_HEADER, StringUtils.join(messageCode));
        }

        return new ResponseEntity<>(apiResponse, headers, status);
    }

    /**
     * Helper method to create a consistent ApiResponse for ErrorResponseException types.
     * @param ex The ErrorResponseException instance.
     * @param request The current WebRequest.
     * @return A ResponseEntity containing an ApiResponse with ProblemDetail.
     */
    protected ResponseEntity<ApiResponse<ProblemDetail>> createErrorApiResponse(ErrorResponseException ex, WebRequest request) {
        // Tái cấu trúc để sử dụng lại logic chung, đảm bảo tính nhất quán.
        // ErrorResponseException đã chứa HttpStatus và ProblemDetail, ta chỉ cần truyền nó vào phương thức chung.
        return buildProblemDetailResponse(ex, (HttpStatus) ex.getStatusCode(), request, ex.getDetailMessageCode());
    }

    // === Private Helper Methods ===

    private String getMessageCode(Exception ex, String... defaultCode) {
        if (ex instanceof ResourceNotFoundException rnfEx && rnfEx.getMessageCode() != null) return rnfEx.getMessageCode();
        if (ex instanceof BadRequestException brEx && brEx.getMessageCode() != null) return brEx.getMessageCode();
        if (ex instanceof ConflictException cEx && cEx.getMessageCode() != null) return cEx.getMessageCode();
        if (ex instanceof ForbiddenException fEx && fEx.getMessageCode() != null) return fEx.getMessageCode();
        if (ex instanceof ErrorResponseException erEx && erEx.getDetailMessageCode() != null) return erEx.getDetailMessageCode();
        return (defaultCode != null && defaultCode.length > 0) ? defaultCode[0] : null;
    }

    private Object[] getMessageArgs(Exception ex) {
        if (ex instanceof ResourceNotFoundException rnfEx) return rnfEx.getArgs();
        if (ex instanceof BadRequestException brEx) return brEx.getArgs();
        if (ex instanceof ConflictException cEx) return cEx.getArgs();
        if (ex instanceof ForbiddenException fEx) return fEx.getArgs();
        if (ex instanceof ErrorResponseException erEx) return erEx.getDetailMessageArguments();
        return new Object[0];
    }

    private String resolveMessage(String code, Object[] args, String defaultMessage, java.util.Locale locale) {
        if (code == null) {
            return defaultMessage;
        }
        try {
            return messageSource.getMessage(code, args, locale);
        } catch (NoSuchMessageException e) {
            log.trace("Không tìm thấy message cho mã lỗi '{}'. Sử dụng message mặc định.", code);
            return defaultMessage;
        }
    }

    private ProblemDetail createProblemDetail(Exception ex, HttpStatus status, String path, String detail) {
        // Nếu exception là ErrorResponseException, nó đã có sẵn ProblemDetail, ta nên dùng lại nó.
        ProblemDetail problemDetail = (ex instanceof ErrorResponseException)
            ? ((ErrorResponseException) ex).getBody()
            : ProblemDetail.forStatus(status);

        problemDetail.setDetail(detail);
        problemDetail.setTitle(status.getReasonPhrase());
        problemDetail.setInstance(java.net.URI.create(path));
        // Thêm các thông tin khác nếu cần, ví dụ:
        // problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    private ApiResponse<ProblemDetail> buildApiResponse(ProblemDetail problemDetail) {
        return ApiResponse.<ProblemDetail>builder()
                .status(problemDetail.getStatus())
                .message(problemDetail.getDetail())
                .path(problemDetail.getInstance().toString())
                .data(problemDetail)
                .build();
    }
}
