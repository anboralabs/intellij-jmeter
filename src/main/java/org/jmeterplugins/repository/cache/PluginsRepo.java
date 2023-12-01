package org.jmeterplugins.repository.cache;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class PluginsRepo implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(PluginsRepo.class);

    private final String repoJSON;
    private final long expirationTime;
    private final long lastModified;

    public PluginsRepo(String repoJSON, long expirationTime, long lastModified) {
        this.repoJSON = repoJSON;
        this.expirationTime = expirationTime;
        this.lastModified = lastModified;
    }

    public boolean isActual() {
        return expirationTime > System.currentTimeMillis();
    }

    public boolean isActual(long lastModified) {
        return isActual() && lastModified <= this.lastModified;
    }

    public long getExpirationTime() {
        return expirationTime;
    }

    public String getRepoJSON() {
        return repoJSON;
    }

    public void saveToFile(File file) {
        log.debug("Saving repo to file: " + file.getAbsolutePath());
        // Serialization
        try (FileOutputStream fout = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fout)) {
            FileUtils.touch(file);
            // Method for serialization of object
            out.writeObject(this);
        } catch (IOException ex) {
            log.warn("Failed for serialize repo", ex);
        }
    }

    public static PluginsRepo fromFile(File file) {
        log.debug("Loading repo from file: " + file.getAbsolutePath());
        // Deserialization
        try (FileInputStream fis = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fis);) {

            // Method for deserialization of object
            return (PluginsRepo) in.readObject();
        } catch (IOException | ClassNotFoundException ex) {
            log.warn("Failed for deserialize repo", ex);
            return null;
        }
    }
}
