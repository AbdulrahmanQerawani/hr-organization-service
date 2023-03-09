package com.infinity.organization.utils;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.opentelemetry.context.Context.current;

@Component
@Slf4j
public class UserContextFilter implements Filter {
    private static final Logger LOGGER = log;
    private final Tracer tracer;

    public UserContextFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    public String getOTelTraceId() {
        // Get the current span from the open telemetry context
        Context context = current();
        Span currentSpan = Span.fromContext(context);
        // Get traceId from open telemetry Current Span
        return  currentSpan.getSpanContext().getTraceId();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {


        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContextHolder.getContext().setCorrelationId(getOTelTraceId());
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
        UserContextHolder.getContext().setOrganizationId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));
        UserContextHolder.getContext().setDepartmentId(httpServletRequest.getHeader(UserContext.DEPARTMENT_ID));
        UserContextHolder.getContext().setEmployeeId(httpServletRequest.getHeader(UserContext.EMPLOYEE_ID));

        LOGGER.debug("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        filterChain.doFilter(httpServletRequest, servletResponse);
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }
}
