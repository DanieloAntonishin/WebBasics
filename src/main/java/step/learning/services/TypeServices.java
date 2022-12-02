package step.learning.services;

import com.google.inject.Singleton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TypeServices {     // класс для константынх значений расширений и не только
    private final Map<String, String> imageTypes;  // расширение для картинок на Uploading

    public TypeServices() {
        imageTypes = new HashMap<>();
        imageTypes.put(".bmp", "image/bmp");
        imageTypes.put(".gif", "image/gif");
        imageTypes.put(".jpg", "image/jpeg");
        imageTypes.put(".jpeg", "image/jpeg");
        imageTypes.put(".png", "image/png");
    }

    /**
     * Checks if extension given corresponds to image type
     * @param extension file extension with dot
     * @return boolean
     */
    public boolean isImage(String extension){
        return imageTypes.containsKey(extension);
    }

    /**
     * Define MIME type by file extension
     * @param extension file extension
     * @return MIME type or null if not include
     */
    public String getMimeByStringExtension(String extension)
    {
        if(imageTypes.containsKey(extension)){
            return imageTypes.get(extension);
        }
        return null;
    }
}
