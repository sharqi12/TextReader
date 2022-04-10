import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Screen {
    @XmlAttribute
    String touch;
    String size;
    String resolution;
    String type;

    public Screen(String touch, String size, String resolution, String type) {
        this.touch = touch;
        this.size = size;
        this.resolution = resolution;
        this.type = type;
    }

    public Screen() {
    }
}
