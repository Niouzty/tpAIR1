package fr.uit.univparis8.tpair.tpair1.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class ServiceProfilerAspect {
    private static final Logger log = LoggerFactory.getLogger(ServiceProfilerAspect.class);

    @Around("execution(* fr.uit.univparis8.tpair.tpair1.service..*(..))")
    public Object profile(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature sig = (MethodSignature) pjp.getSignature();
        String method = sig.getDeclaringType().getSimpleName() + "." + sig.getName();
        Map<String, Object> args = simplify(sig.getParameterNames(), pjp.getArgs());

        long start = System.nanoTime();
        log.info("svc.in method={} args={}", method, args);
        try {
            Object result = pjp.proceed();
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.info("svc.out method={} tookMs={} result={}", method, tookMs, summarize(result));
            return result;
        } catch (Throwable ex) {
            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            log.error("svc.err method={} tookMs={} type={} message={}",
                    method, tookMs, ex.getClass().getSimpleName(), ex.getMessage());
            throw ex;
        }
    }

    private Map<String, Object> simplify(String[] names, Object[] values) {
        Map<String, Object> out = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            String name = (names != null && i < names.length) ? names[i] : "arg" + i;
            out.put(name, isSensitive(name) ? "***" : summarize(values[i]));
        }
        return out;
    }

    private boolean isSensitive(String n) {
        String lower = n == null ? "" : n.toLowerCase();
        return lower.contains("password") || lower.contains("token") || lower.contains("secret");
    }

    private Object summarize(Object v) {
        if (v == null) return null;
        if (v instanceof String) {
            String s = (String) v;
            return s.length() > 80 ? s.substring(0, 77) + "..." : s;
        }
        if (v instanceof Number || v instanceof Boolean || v instanceof Enum<?>) return v;
        if (v instanceof Collection) {
            Collection<?> c = (Collection<?>) v;
            return v.getClass().getSimpleName() + "(size=" + c.size() + ")";
        }
        if (v.getClass().isArray()) return v.getClass().getComponentType().getSimpleName() + "[](size=" + Array.getLength(v) + ")";
        return v.getClass().getSimpleName();
    }
}
