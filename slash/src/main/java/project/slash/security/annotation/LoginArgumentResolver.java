package project.slash.security.annotation;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import project.slash.common.exception.BusinessException;
import project.slash.security.exception.AuthErrorCode;

@Component
public class LoginArgumentResolver implements HandlerMethodArgumentResolver {

   @Override
   public boolean supportsParameter(MethodParameter parameter) {
      return parameter.getParameterType().equals(String.class) && parameter.hasParameterAnnotation(Login.class);
   }

   @Override
   public String resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory
   ) {
      return getUserIdFromContext();
   }

   private String getUserIdFromContext() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null || !(authentication.getPrincipal() instanceof String)) {
         throw new BusinessException(AuthErrorCode.UNAUTHORIZED_USER);
      }

      return (String) authentication.getPrincipal();
   }
}
