package de.tum.in.ase.eist.tokengeneratormicroservice;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping(value = "/tokenGenerator/")
public class TokenGeneratorController {
    //TODO L03P01 Facade Pattern 2.1: Implement the getToken method.
    @PostMapping("getToken")
    public String generateToken(@RequestBody Employee employee) {
        String saltedToken = tokenHash(employee.getName() + employee.getId());
        return saltedToken;
    }

    public static String tokenHash(String input) {
        try {
            // Create an instance of the SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Apply the salt to the input string
            String saltedInput = input + "EIST2023";

            // Apply the hash function to the salted input
            byte[] hashedBytes = digest.digest(saltedInput.getBytes(StandardCharsets.UTF_8));

            // Convert the hashed bytes to a hexadecimal representation
            StringBuilder stringBuilder = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                stringBuilder.append(Integer.toString((hashedByte & 0xff) + 0x100, 16).substring(1));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

}

