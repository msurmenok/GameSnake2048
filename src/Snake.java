/**
 * Created by Maria on 26.11.2014.
 */
public class Snake {
    public int x = 0;
    public int y = 400;
    public int value = 0;

    Snake() {}
    Snake(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public String toString() {
        return "x =" + x + ", y = " + y + ", value = " + value;
    }
}
