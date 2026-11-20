package com.example;

public class GreetingApp {
    public static String getMessage(String env) {
        return "Hello from Jenkins CI/CD! Running in environment: " + env;
    }

    public static void main(String[] args) {
        String env = args.length > 0 ? args[0] : "DEV";
        System.out.println(getMessage(env));
    }
}
