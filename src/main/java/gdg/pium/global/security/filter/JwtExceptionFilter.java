package gdg.pium.global.security.filter;

import gdg.pium.global.constant.Constants;
import gdg.pium.global.exception.CommonException;
import gdg.pium.global.exception.ErrorCode;
import gdg.pium.global.exception.JsonWebTokenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static gdg.pium.global.util.ExceptionUtil.getLineNumber;
import static gdg.pium.global.util.ExceptionUtil.getSimpleName;

@Slf4j
public class JwtExceptionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        try {
            filterChain.doFilter(request, response);

        } catch (SecurityException e) {
            log.error("FilterException throw SecurityException When [{}] {} At {} : {}",
                    requestMethod,
                    requestURI,
                    getLineNumber(e),
                    e.getMessage());

            request.setAttribute("exception", ErrorCode.ACCESS_DENIED);
            filterChain.doFilter(request, response);

        } catch (JsonWebTokenException e) {
            log.error("FilterException throw JsonWebTokenException When [{}] {} At {} : {}",
                    requestMethod,
                    requestURI,
                    getLineNumber(e),
                    e.getMessage());

            request.setAttribute("exception", e.getErrorCode());
            filterChain.doFilter(request, response);

        } catch (CommonException e) {
            Long userId = getUserIdOrNull(request);
            log.error("FilterException throw CommonException By User(id:{}) When [{}] {} At {} : {}",
                    userId,
                    requestMethod,
                    requestURI,
                    getLineNumber(e),
                    e.getMessage());

            request.setAttribute("exception", e.getErrorCode());
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            Long userId = getUserIdOrNull(request);
            log.error("FilterException throw Exception {} By User(id:{}) When [{}] {} At {} : {}",
                    userId,
                    getSimpleName(e),
                    requestMethod,
                    requestURI,
                    getLineNumber(e),
                    e.getMessage());

            request.setAttribute("exception", ErrorCode.INTERNAL_SERVER_ERROR);
            filterChain.doFilter(request, response);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Constants.NO_NEED_AUTH_URLS.contains(request.getRequestURI());
    }

    private Long getUserIdOrNull(HttpServletRequest request) {
        return (Long) request.getAttribute("userId");
    }
}
