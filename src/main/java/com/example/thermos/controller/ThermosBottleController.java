package com.example.thermos.controller;

import com.example.thermos.model.ThermosBottle;
import com.example.thermos.service.ThermosBottleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/thermos")
public class ThermosBottleController {

    @Autowired
    private ThermosBottleService thermosBottleService;

    // Hiển thị danh sách bình giữ nhiệt
    @GetMapping
    public String listThermosBottles(Model model, @AuthenticationPrincipal OAuth2User principal,
                                   @RequestParam(required = false) String search) {
        model.addAttribute("user", principal);
        
        List<ThermosBottle> bottles;
        if (search != null && !search.trim().isEmpty()) {
            bottles = thermosBottleService.searchThermosBottles(search.trim());
            model.addAttribute("searchKeyword", search);
        } else {
            bottles = thermosBottleService.getAllThermosBottles();
        }
        
        model.addAttribute("bottles", bottles);
        return "thermos/list";
    }

    // Hiển thị form thêm mới
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bottle", new ThermosBottle());
        model.addAttribute("isEdit", false);
        return "thermos/form";
    }

    // Xử lý thêm mới
    @PostMapping("/new")
    public String createThermosBottle(@Valid @ModelAttribute("bottle") ThermosBottle bottle,
                                    BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", false);
            return "thermos/form";
        }

        String id = thermosBottleService.saveThermosBottle(bottle);
        if (id != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Thêm mới bình giữ nhiệt thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi thêm mới!");
        }
        return "redirect:/thermos";
    }

    // Hiển thị form chỉnh sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        ThermosBottle bottle = thermosBottleService.getThermosBottleById(id);
        if (bottle == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bình giữ nhiệt!");
            return "redirect:/thermos";
        }
        
        model.addAttribute("bottle", bottle);
        model.addAttribute("isEdit", true);
        return "thermos/form";
    }

    // Xử lý cập nhật
    @PostMapping("/edit/{id}")
    public String updateThermosBottle(@PathVariable String id, @Valid @ModelAttribute("bottle") ThermosBottle bottle,
                                    BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "thermos/form";
        }

        String updatedId = thermosBottleService.updateThermosBottle(id, bottle);
        if (updatedId != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật bình giữ nhiệt thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi cập nhật!");
        }
        return "redirect:/thermos";
    }

    // Hiển thị chi tiết
    @GetMapping("/view/{id}")
    public String viewThermosBottle(@PathVariable String id, Model model, RedirectAttributes redirectAttributes) {
        ThermosBottle bottle = thermosBottleService.getThermosBottleById(id);
        if (bottle == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy bình giữ nhiệt!");
            return "redirect:/thermos";
        }
        
        model.addAttribute("bottle", bottle);
        return "thermos/view";
    }

    // Xóa bình giữ nhiệt
    @PostMapping("/delete/{id}")
    public String deleteThermosBottle(@PathVariable String id, RedirectAttributes redirectAttributes) {
        boolean deleted = thermosBottleService.deleteThermosBottle(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Xóa bình giữ nhiệt thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Có lỗi xảy ra khi xóa!");
        }
        return "redirect:/thermos";
    }
}