package bg.softuni.FitnessMenu.scheduler;


import bg.softuni.FitnessMenu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InactiveUsersCleanupScheduler {

    private final UserService userService;

    @Autowired
    public InactiveUsersCleanupScheduler(UserService userService) {
        this.userService = userService;
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void cleanupInactiveUsers() {
        userService.deactivateInactiveUsers();
    }
}
