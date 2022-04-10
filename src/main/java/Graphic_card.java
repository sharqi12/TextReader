import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Graphic_card {
    String name;
    String memory;

    public Graphic_card(String name, String memory) {
        this.name = name;
        this.memory = memory;
    }

    public Graphic_card() {
    }
}
