package com.ekla.webcompilerproject.WebBasedCompiler.entities;

public class JavaCode {
    private String code;
    private String input;
    private String output;

    public JavaCode(){}

    public void setCode(String code) {
        this.code = code;
    }
    public void setInput(String input) {
        this.input = input;
    }
    public void setOutput(String output) {
        this.output = output;
    }
    public String getCode() {
        return code;
    }
    public String getInput() {
        return input;
    }
    public String getOutput() {
        return output;
    }
}
