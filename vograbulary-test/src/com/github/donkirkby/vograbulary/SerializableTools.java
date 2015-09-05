package com.github.donkirkby.vograbulary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableTools {
    public static <T extends Serializable> byte[] serialize(T obj)
            throws IOException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(obj);
        objectStream.close();
        return byteStream.toByteArray();
    }

    public static <T extends Serializable> T deserialize(
            byte[] bytes,
            Class<T> clazz)
            throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object obj = objectStream.readObject();
        return clazz.cast(obj);
    }
}
