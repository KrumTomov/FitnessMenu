package bg.softuni.FitnessMenu.interceptor;

import bg.softuni.FitnessMenu.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginCounterInterceptor implements HandlerInterceptor {

    private final UserService userService;

    @Autowired
    public LoginCounterInterceptor(UserService userService) {
        this.userService = userService;
    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        String username = request.getRemoteUser();

        Boolean loginCountIncremented = (Boolean) session.getAttribute("loginCountIncremented");

        if (username != null && loginCountIncremented == null) {
            userService.incrementLoginCount(username);
            userService.markUserAsActive(username);

            session.setAttribute("loginCountIncremented", true);
        }

        return true;
    }
}
