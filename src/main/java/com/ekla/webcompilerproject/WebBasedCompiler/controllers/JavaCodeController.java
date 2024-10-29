package com.ekla.webcompilerproject.WebBasedCompiler.controllers;

import com.ekla.webcompilerproject.WebBasedCompiler.entities.JavaCode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class JavaCodeController {

    @GetMapping("/")
    public String returnDefaultPage(Model model){
        String defaultCode = "public class Main {\r\n" +
                "    public static void main(String[] args) {\r\n" +
                "        System.out.println(\"Hello, World!\");\r\n" +
                "    }\r\n" +
                "}";
        model.addAttribute("defaultCode", defaultCode);
        return "JavaWebCompiler";
    }

    @PostMapping("/submitCode")
    @ResponseBody
    public String submitCode(@RequestBody JavaCode javaCode, Model model){
        model.addAttribute("javaCode", javaCode);
        return executeJavaCode(javaCode);
    }

    // Executes the Java Code
    private String executeJavaCode(JavaCode javaCode){
        String runProcessOutput;
        int runProcessResult;
        try {

            // Create Temporary File and Store the Code
            Path tempDirectoryPath = Files.createTempDirectory("JavaCodeExecution");
            Path tempFilePath = Paths.get(tempDirectoryPath.toString(),"Main.java");
            Files.write(tempFilePath,javaCode.getCode().getBytes());

            // Compiling the Code
            Process compiledProcess = new ProcessBuilder("javac", tempFilePath.toString()).redirectErrorStream(true).start();

            // Output after Compiling the Code
            String compiledProcessOutput = new String(compiledProcess.getInputStream().readAllBytes()).replace("\n","\r\n");
            int compiledProcessResult = compiledProcess.waitFor();
            if (compiledProcessResult != 0) {
                return "Compilation Error:\r\n" + compiledProcessOutput;
            }

            // Running the Compiled Class Now after successful compilation.
            Process runProcess = new ProcessBuilder("java","-cp", tempDirectoryPath.toString(),"Main").redirectErrorStream(true).start();
            runProcessOutput = new String(runProcess.getInputStream().readAllBytes()).replace("\n","\r\n");
            runProcessResult = runProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Run Process Output:\r\n" + runProcessOutput + "\r\nRun Process Result:\r\n" + runProcessResult;
    }
}
