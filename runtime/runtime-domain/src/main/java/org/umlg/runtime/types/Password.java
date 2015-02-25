package org.umlg.runtime.types;

import org.apache.tinkerpop.gremlin.structure.Property;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.umlg.runtime.util.PasswordEncryptionService;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Date: 2014/08/02
 * Time: 8:39 PM
 */
public class Password implements UmlgType {

    private final String SALT = "_salt";
    private byte[] salt;
    private byte[] encryptedPassword;
    private String password;

    public Password() {
    }

    public Password(String password) {
        this.password = password;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getEncryptedPassword() {
        return encryptedPassword;
    }

    @Override
    public void setOnVertex(Vertex v, String persistentName) {
        final PasswordEncryptionService pes = new PasswordEncryptionService();
        try {
            Property<byte[]> saltProperty = v.property(persistentName + SALT);
            if (saltProperty.isPresent()) {
                this.salt = saltProperty.value();
            } else {
                this.salt = pes.generateSalt();
            }
            this.encryptedPassword = pes.getEncryptedPassword(this.password, this.salt);
            v.property(persistentName + SALT, this.salt);
            v.property(persistentName, this.encryptedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadFromVertex(Vertex v, String persistentName) {
        this.salt = v.<byte[]>property(persistentName + SALT).value();
        this.encryptedPassword = v.<byte[]>property(persistentName).value();
    }
}
