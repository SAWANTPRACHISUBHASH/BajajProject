package com.prachiSawantBajajProject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.Map;

public class GenerateMD5WithPRN {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    public static String getPathToJson(String[] args) {
        return args[1];
    }

    public static String getPrnNumber(String[] args) {
        return args[0];
    }

    private static String findDestination(JsonNode rootNode) {
        if (rootNode.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = rootNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().equals("destination")) {
                    return field.getValue().asText();
                } else {
                    String result = findDestination(field.getValue());
                    if (result != null) {
                        return result;
                    }
                }
            }
        } else if (rootNode.isArray()) {
            for (JsonNode node : rootNode) {
                String result = findDestination(node);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static String getDestinationValue(String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(new File(path));
            return findDestination(rootNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String generateMD5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void execute(String[] args) {
        if (args.length == 2) {
            String prnNumber = getPrnNumber(args);
            String pathToJson = getPathToJson(args);
            String destinationValue = getDestinationValue(pathToJson);
            String randomString = generateRandomString(LENGTH);
            String hashableText = prnNumber + destinationValue + randomString;
            String md5Hash = generateMD5Hash(hashableText);
            String output = md5Hash + ";" + randomString;
            System.out.println(output);
        }
    }
}