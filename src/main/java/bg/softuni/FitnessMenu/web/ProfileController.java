package bg.softuni.FitnessMenu.web;


import bg.softuni.FitnessMenu.model.view.UserProfileViewModel;
import bg.softuni.FitnessMenu.service.UserService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Autowired
    public ProfileController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @GetMapping("/view")
    public String viewProfile(Principal principal, Model model) {
        UserProfileViewModel userProfileViewModel = modelMapper.map(userService.getUserByUsername(principal.getName()), UserProfileViewModel.class);
        model.addAttribute("userProfileViewModel", userProfileViewModel);
        return "profile";
    }

    @GetMapping("/edit")
    public String showEditProfile(Principal principal, Model model) {
        UserProfileViewModel userProfileViewModel = modelMapper.map(userService.getUserByUsername(principal.getName()), UserProfileViewModel.class);
        model.addAttribute("userProfileViewModel", userProfileViewModel);
        return "editProfile";
    }

    @PostMapping("/update")
    public String saveProfile(@Valid UserProfileViewModel userProfileViewModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Principal principal) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userProfileViewModel", userProfileViewModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userProfileViewModel", bindingResult);
            return "editProfile";
        }

        userService.editProfileInfo(userProfileViewModel, principal.getName());
        return "redirect:/profile/view";
    }
}

