package data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;

public class JsonUtilities<T> {

	public T loadFromJson(String file, Class<T> clazz) throws IOException
	{
        Gson gson = new Gson();

        InputStream inputStream = getClass().getResourceAsStream(file);
        String jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        T data = gson.fromJson(jsonString, clazz);

        return data;
	}
}
