package top.verytouch.vkit.mydoc.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OS {

    WINDOWS("windows", "explorer", "C:\\Windows\\Fonts\\"),
    MAC("mac", "open", "/System/Library/Fonts/"),
    LINUX("linux", "xdg-open", "/usr/share/fonts/");

    private final String os;
    private final String exploreCmd;
    private final String fontDir;

    public static OS now() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            return WINDOWS;
        } else if (os.contains("mac")) {
            return MAC;
        } else if (os.contains("linux")) {
            return LINUX;
        }
        return LINUX;
    }

}
