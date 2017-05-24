package com.mobica.aws.s3.storage;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class AWSController {

    private final S3Service s3Service;

    public AWSController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @GetMapping("/{filename:.+}")
    public String getFileContent(@PathVariable String filename, Model model) {
        String content = s3Service.getFileContent(filename).orElse("Error");
        model.addAttribute("message", content);
        return "uploadForm";
    }


    @GetMapping("/")
    public String getListFiles(Model model) {
        List<String> allFiles = s3Service.getAllFiles();
        model.addAttribute("files", allFiles);
        return "uploadForm";
    }

    @PostMapping("/")
    public String singleFileUpload(@RequestParam("file") MultipartFile multipart,
                                   RedirectAttributes redirectAttributes) {

        String response = "File uploaded";

        try {
            s3Service.saveFile(multipart);
        } catch (Exception e) {
            response = "Error";
        }

        redirectAttributes.addFlashAttribute("message", response);
        return "redirect:/";
    }


}
