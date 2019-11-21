package pl.piotrchowaniec.gallery.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.piotrchowaniec.gallery.models.services.UserSession;
import pl.piotrchowaniec.gallery.models.utils.Mappings;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

@Configuration
public class WebMvcConfig extends HandlerInterceptorAdapter implements WebMvcConfigurer {

    private final UserSession userSession;

    private static final List<String> allowedUrls = Arrays.asList(Mappings.LOGIN);

    @Autowired
    public WebMvcConfig(UserSession userSession) {
        this.userSession = userSession;
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", Mappings.HOME);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String requestUrlAsString = request.getRequestURL().toString();
        if (userSession.isLoggedIn()) {
            return true;
        }
        if (allowedUrls.stream().anyMatch(s -> requestUrlAsString.contains(s))){
            return super.preHandle(request, response, handler);
        }
        response.sendRedirect(Mappings.LOGIN);
        return false;
    }
}
