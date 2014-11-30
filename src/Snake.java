/**
 * Created by Maria on 26.11.2014.
 */
public class Snake {
    public int x = 0;
    public int y = 400;
    public long value = 0;

    Snake() {}
    Snake(int x, int y, long value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    @Override
    public String toString() {
        return "x =" + x + ", y = " + y + ", value = " + value;
    }


}
