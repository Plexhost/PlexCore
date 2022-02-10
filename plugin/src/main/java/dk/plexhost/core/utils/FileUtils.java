package dk.plexhost.core.utils;

public class FileUtils {
    public static boolean isYamlFile(String paramString){
        return StringUtils.endsWith(paramString, "yml", "yaml");
    }
}
