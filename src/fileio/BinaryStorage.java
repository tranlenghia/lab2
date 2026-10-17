package fileio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BinaryStorage<T> implements Storage<T> {

    @Override
    public boolean save(String fileName, T data) {
        if (fileName == null || fileName.trim().isEmpty()
                || data == null) {
            return false;
        }

        try (ObjectOutputStream output =
                new ObjectOutputStream(
                        new BufferedOutputStream(
                                new FileOutputStream(fileName)))) {

            output.writeObject(data);
            return true;

        } catch (IOException e) {
            System.out.println("Cannot save file: " + fileName);
            System.out.println("Reason: " + e.getMessage());
            return false;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public T load(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) {
            return null;
        }

        try (ObjectInputStream input =
                new ObjectInputStream(
                        new BufferedInputStream(
                                new FileInputStream(fileName)))) {

            return (T) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Cannot load file: " + fileName);
            System.out.println("Reason: " + e.getMessage());
            return null;
        }
    }
}